package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Plant;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * DAO class for communication with "Plant" table.
 * Implements method declared in PlantDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class PlantDAOImpl  extends AbstractDao implements PlantDAO {

    /**
     * Date format management.
     */
    private final SimpleDateFormat dateFormat;

    /**
     * Date format manager for history tracking(different precision).
     */
    private final SimpleDateFormat tSimpleDateFormat;

    @Autowired
    public PlantDAOImpl(@Qualifier("simpleDateFormat") SimpleDateFormat dateFormat, @Qualifier("tSimpleDateFormat") SimpleDateFormat tSimpleDateFormat) {
        this.dateFormat = dateFormat;
        this.tSimpleDateFormat = tSimpleDateFormat;
    }

    /**
     * Method commits the query and return list of "Plant" objects.
     * @param query should should be commit.
     * @return list of "Plant" objects.
     */
    private List<Plant> getWithCondition(String query){
        ArrayList<Plant> plants = new ArrayList<>();

        try(Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        plants.add(new Plant(resultSet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plants;
    }

    /**
     * Uses getWithCondition() method returns list of objects.
     * @return list of all "Plant" objects.
     */
    @Override
    public List<Plant> getAll() {
        return getWithCondition(Plant.GET_ALL);
    }

    /**
     * Uses getWithCondition() method returns list of objects in period.
     * @param from beginning of period.
     * @param to end of period.
     * @return list of all "Plant" objects.
     */
    @Override
    public List<Plant> getAllInPeriod(Date from, Date to) {
        return getWithCondition(String.format(Plant.GET_ALL_IN_PERIOD, dateFormat.format(from), dateFormat.format(to)));
    }

    /**
     * Uses getWithCondition() method returns list of objects in period.
     * @param from beginning of period.
     * @param to end of period.
     * @return list of 'alive' "Plant" objects.
     */
    @Override
    public List<Plant> getAliveinPeriod(Date from, Date to) {
        return getWithCondition(String.format(Plant.GET_ALIVE_IN_PERIOD, dateFormat.format(from), dateFormat.format(to)));
    }

    /**
     * Uses getWithCondition() method returns list of objects.
     * @param id of object.
     * @return list of "Plant" objects represent the history of modifications of particular object.
     */
    @Override
    public List<Plant> getHistory(String id) {
        return getWithCondition(String.format(Plant.GET_HISTORY, id, id));
    }

    /**
     * Update exists object.
     * Also store the history of changes.
     * @param newPlant updated plant.
     * @return updated plant or null.
     */
    @Override
    public Plant update(Plant newPlant) {
        Plant old = this.get(newPlant.getId());

        try {
            old.settTo(tSimpleDateFormat.parse(tSimpleDateFormat.format(new Date())));
        } catch (ParseException e) {
            return null;
        }

        super.delete(String.format(Plant.FULLY_DELETE, newPlant.getId()));

        newPlant.settFrom(old.gettTo());
        newPlant = store(newPlant);

        old.setId("");
        old.setNewPlantId(newPlant.getId());
        store(old);

        return newPlant;
    }

    /**
     * @return all alive plants.
     */
    @Override
    public List<Plant> getAlive() {
        return getWithCondition(Plant.GET_ALIVE);
    }

    /**
     * Store single "Plant" object.
     * @param plant the object
     * @return stored object or null.
     */
    @Override
    public Plant store(Plant plant) {

        try(Connection connection = getDataSource().getConnection()) {

            connection.setAutoCommit(false);

            if (plant.getId().isEmpty()){
                plant.setId(UUID.randomUUID().toString());
            }

            Statement statement = connection.createStatement();

            // Insert new object with empty ORDImage object
            statement.executeUpdate(String.format(Plant.STORE,
                    plant.getId(),  // id
                    plant.getName() ,   // jmeno
                    plant.getType() ,   // druh
                    plant.getGeometry().getJavaPoint().getX(),
                    plant.getGeometry().getJavaPoint().getY(),
                    plant.getFrom() == null ? dateFormat.format(new Date()) : dateFormat.format(plant.getFrom()), // When plant is stored the 'from' time is set as now. Need to track the history of changes.
                    plant.getTo() == null ? String.valueOf("null") : "'" + dateFormat.format(plant.getTo()) + "'",   // It isn't known when the plant die. NULL value in 'to' parameter represents 'forever'.
                    plant.gettFrom() == null ? "'" + tSimpleDateFormat.format(new Date()) + "'" :"'" + tSimpleDateFormat.format(plant.gettFrom()) + "'",
                    plant.gettTo() == null ? String.valueOf("null") : "'" + tSimpleDateFormat.format(plant.gettTo()) + "'",
                    plant.getNewPlantId(),
                    plant.getGardenId()
            ));
            statement.close();

            plant.setFrom(plant.gettFrom() == null ? tSimpleDateFormat.parse(tSimpleDateFormat.format(new Date())) : plant.gettFrom());
            plant.settTo(null);

            // retrieve the previously created ORDImage object for future updating
            Statement statement1 = connection.createStatement();
            OracleResultSet resultSet = (OracleResultSet) statement1.executeQuery(String.format(Plant.GET_PHOTO, plant.getId()));
            resultSet.next();
            OrdImage imgProxy = (OrdImage) resultSet.getORAData("fotka", OrdImage.getORADataFactory());
            resultSet.close();
            statement1.close();

            // load the media data from a file to the ORDImage Java object
            imgProxy.loadDataFromByteArray(plant.getPhoto());
            imgProxy.setProperties();

            // update the table with ORDImage Java object (data already loaded)
            OraclePreparedStatement preparedStatement = (OraclePreparedStatement)
                    connection.prepareStatement(String.format(Plant.UPDATE_PHOTO_1, plant.getId()));
            preparedStatement.setORAData(1, imgProxy);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // update the table with StillImage object and features
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate(String.format(Plant.UPDATE_PHOTO_2, plant.getId()));
            statement2.executeUpdate(String.format(Plant.UPDATE_PHOTO_3, plant.getId()));
            statement2.close();
            connection.commit();

            return plant;

        } catch (SQLException | IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param id of the object.
     * @return "Plant" object identified by.
     */
    @Override
    public Plant get(String id) {
        Plant plant = null;
        try(Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Plant.GET, id))) {
                    if (resultSet.next()) {
                        plant = new Plant(resultSet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plant;
    }

    /**
     * Mark the object as "dead".
     * @param id of dead object.
     */
    @Override
    public void kill(String id) {
        Plant plant = this.get(id);
        plant.setTo(new Date());
        update(plant);
    }

    /**
     * Call parent method "delete" with own query for deleting object.
     * @param id identifier od object needs to be deleted.
     */
    @Override
    public void delete(String id){
        super.delete(String.format(Plant.DELETE,  tSimpleDateFormat.format(new Date()), id));
    }

    /**
     * @param id of the garden.
     * @return list of plants from the garden.
     */
    @Override
    public List<Plant> getFromGarden(String id) {
        return getWithCondition(String.format(Plant.GET_FROM_GARDEN, id));
    }

    /**
     * @return list of plants with the same live time.
     */
    @Override
    public Map<Long, List<Plant>> getListOfPlantWithSameLiveTime() {
        Map<Long, List<Plant>> map = new HashMap<>();

        try(Connection connection = getDataSource().getConnection()){

            try(Statement statement = connection.createStatement()){

                try(OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Plant.GET_HISTORY_OF_EACH)){
                    List<String> listOfDistenctIDs = new ArrayList<>();

                    // Get list of unique plants have ever been in our zoo.
                    while (resultSet.next()) {
                        listOfDistenctIDs.add(resultSet.getString("ID"));
                    }

                    // Get history of each unique plant
                    for (String id:
                         listOfDistenctIDs) {

                        List<Plant> plantHistory = getHistory(id);

                        Plant firstModifPlant = plantHistory.get(0);
                        Plant lastModifPlant = plantHistory.get(plantHistory.size() -1 );

                        Optional<Date> toDate = Optional.ofNullable(lastModifPlant.getTo());


                        //Cast milliseconds to days.
                        Long liveTimeInDays = Math.abs((toDate.orElse(new Date()).getTime() - firstModifPlant.getFrom().getTime())) / (1000*60*60*24);

                        Plant plant = new Plant();
                        plant.setId(id);
                        plant.setName(lastModifPlant.getName());
                        plant.setType(lastModifPlant.getType());
                        plant.setGeometry(lastModifPlant.getGeometry());
                        plant.setPhoto(lastModifPlant.getPhoto());
                        plant.setFrom(firstModifPlant.getFrom());
                        plant.setTo(lastModifPlant.getTo());
                        plant.settFrom(firstModifPlant.gettFrom());
                        plant.settTo(lastModifPlant.gettTo());


                        if (map.containsKey(liveTimeInDays)){
                            map.get(liveTimeInDays).add(plant);
                        }else {
                            map.put(liveTimeInDays, new ArrayList<>());
                            map.get(liveTimeInDays).add(plant);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<Long, List<Plant>> getTheOldests() {
        Map<Long, List<Plant>> map = getListOfPlantWithSameLiveTime();
        Map<Long, List<Plant>> map1 = new HashMap<>();

        if (!map.isEmpty()){
            Long aLong = Collections.max(map.keySet());
            map1.put(aLong, map.get(aLong));
        }

        return map1;
    }
}
