package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.configuration.ServicesHolder;
import cz.vut.fit.zoo.model.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;
import oracle.spatial.geometry.JGeometry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for interaction with database.
 * @author xfouka01, xstast24
 */
public class DatabaseService {

    protected List<Model> listOfModels = new ArrayList<>();

    public List<Plant> getPlantsInPeriod(Date from, Date to){
        return ServicesHolder.getInstance().getPlantService()
                .getAliveinPeriod(from, to);
    }

    /**
     * Method for deleting  of all objects in database.
     */
    public void deleteAll(){
        for (Model item:listOfModels) {
            if(item instanceof Lake)
                ServicesHolder.getInstance().getLakeService()
                        .delete(item.getId());
            else if (item instanceof Hutch)
                ServicesHolder.getInstance().getHutchService()
                        .delete(item.getId());
            else if (item instanceof Garden)
                ServicesHolder.getInstance().getGardenService()
                        .delete(item.getId());
            else if (item instanceof Footpath)
                ServicesHolder.getInstance().getFootpathService()
                        .delete(item.getId());
            else if (item instanceof Plant)
                ServicesHolder.getInstance().getPlantService()
                        .delete(item.getId());
        }
        downloadDataFromDatabase();
    }

    /**
     * Method for creating and storing sample data to database.
     */
    public void initData() {
        // LAKE
        Lake lake = new Lake();
        lake.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 4},
                new double[]{
                        772, 643,
                        772, 455,
                        916, 499}));

        Lake lake2 = new Lake();
        lake2.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 4},
                new double[]{
                        845, 260,
                        845, 14,
                        968, 137}));

        Lake lake3 = new Lake();
        lake3.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 4},
                new double[]{
                        447, 258,
                        447, 171,
                        490, 215}));

        ServicesHolder.getInstance().getLakeService().store(lake);
        ServicesHolder.getInstance().getLakeService().store(lake2);
        ServicesHolder.getInstance().getLakeService().store(lake3);


        // HUTCH
        Hutch hutch = new Hutch();
        hutch.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        10, 10,
                        60, 60}));
        Hutch hutch2 = new Hutch();
        hutch2.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        70, 70,
                        120, 120}));

        Hutch hutch3 = new Hutch();
        hutch3.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        10, 370,
                        300, 400}));

        Hutch hutch4 = new Hutch();
        hutch4.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        10, 470,
                        300, 500}));

        hutch = ServicesHolder.getInstance().getHutchService().store(hutch);
        hutch2 = ServicesHolder.getInstance().getHutchService().store(hutch2);
        hutch3 = ServicesHolder.getInstance().getHutchService().store(hutch3);
        hutch4 = ServicesHolder.getInstance().getHutchService().store(hutch4);


        // DATE PARSER
        DateFormat format = new SimpleDateFormat("d M y", Locale.ENGLISH);


        // ANIMAL
        try {
            Animal animal1 = new Animal();
            animal1.setName("Tlapka");
            animal1.setSpecies("lev");
            animal1.setFrom(format.parse("1 1 2016"));
            animal1.setTo(null);
            animal1.setHutchId(hutch2.getId());
            animal1.setPhoto(getPhoto("./src/main/resources/images/animals/lion.png"));

            Animal animal2 = new Animal();
            animal2.setName("Punťa");
            animal2.setSpecies("lev");
            animal2.setFrom(format.parse("2 9 2017"));
            animal2.setTo(null);
            animal2.setHutchId(hutch2.getId());
            animal2.setPhoto(getPhoto("./src/main/resources/images/animals/lion.png"));

            Animal animal3 = new Animal();
            animal3.setName("Slávista");
            animal3.setSpecies("zebra");
            animal3.setFrom(format.parse("5 10 2017"));
            animal3.setTo(null);
            animal3.setHutchId(hutch3.getId());
            animal3.setPhoto(getPhoto("./src/main/resources/images/animals/zebra.png"));

            Animal animal4 = new Animal();
            animal4.setName("Přechod");
            animal4.setSpecies("zebra");
            animal4.setFrom(format.parse("1 1 2017"));
            animal4.setTo(format.parse("1 10 2017"));
            animal4.setHutchId(hutch3.getId());
            animal4.setPhoto(getPhoto("./src/main/resources/images/animals/zebra.png"));

            Animal animal5 = new Animal();
            animal5.setName("Kulička");
            animal5.setSpecies("slon");
            animal5.setFrom(format.parse("1 10 2017"));
            animal5.setTo(null);
            animal5.setHutchId(hutch.getId());
            animal5.setPhoto(getPhoto("./src/main/resources/images/animals/elephant.png"));

            Animal animal6 = new Animal();
            animal6.setName("Ferrari");
            animal6.setSpecies("želva");
            animal6.setFrom(format.parse("1 1 1950"));
            animal6.setTo(format.parse("5 5 2015"));
            animal6.setHutchId(hutch4.getId());
            animal6.setPhoto(getPhoto("./src/main/resources/images/animals/turtle.png"));

            ServicesHolder.getInstance().getAnimalService().store(animal1);
            ServicesHolder.getInstance().getAnimalService().store(animal2);
            ServicesHolder.getInstance().getAnimalService().store(animal3);
            ServicesHolder.getInstance().getAnimalService().store(animal4);
            ServicesHolder.getInstance().getAnimalService().store(animal5);
            ServicesHolder.getInstance().getAnimalService().store(animal6);
        } catch (Exception e) {
            System.out.println("Zvířata utekla při převozu do ZOO");
        }


        //GARDEN
        Garden garden = new Garden();
        garden.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 1},
                new double[]{
                        527, 268,
                        534, 200,
                        615, 242,
                        643, 248}));

        Garden garden2 = new Garden();
        garden2.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 1},
                new double[]{
                        354, 14,
                        351, 75,
                        549, 74,
                        541, 15}));

        Garden garden3 = new Garden();
        garden3.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 1},
                new double[]{
                        592, 36,
                        736, 34,
                        773, 241,
                        459, 123,
                        597, 100}));

        Garden garden4 = new Garden();
        garden4.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 1},
                new double[]{
                        394, 368,
                        700, 361,
                        720, 600,
                        398, 579}));

        garden = ServicesHolder.getInstance().getGardenService().store(garden);
        garden2 = ServicesHolder.getInstance().getGardenService().store(garden2);
        garden3 = ServicesHolder.getInstance().getGardenService().store(garden3);
        garden4 = ServicesHolder.getInstance().getGardenService().store(garden4);


        // PLANTS
        try {
            Plant plant = new Plant();
            plant.setName("Kaktus old");
            plant.setType("kaktus");
            plant.setFrom(format.parse("3 10 2017"));
            plant.setTo(null);
            plant.settFrom(format.parse("1 1 2015"));
            plant.settTo(null);
            plant.setGeometry(new JGeometry(650, 100, 0));
            plant.setPhoto(getPhoto("./src/main/resources/images/plants/cactus.png"));
            plant.setGardenId(garden2.getId());
            plant = ServicesHolder.getInstance().getPlantService().store(plant);

            plant.setName("Kaktus new");
            plant.setFrom(format.parse("20 10 2017"));
            plant.setTo(null);
            plant.setGeometry(new JGeometry(660, 110, 0));
            ServicesHolder.getInstance().getPlantService().update(plant);


            Plant plant1 = new Plant();
            plant1.setName("List");
            plant1.setType("list");
            plant1.setFrom(format.parse("3 11 2017"));
            plant1.setTo(null);
            plant1.settFrom(format.parse("1 1 2016"));
            plant1.settTo(null);
            plant1.setGeometry(new JGeometry(690, 110, 0));
            plant1.setPhoto(getPhoto("./src/main/resources/images/plants/leaf.png"));
            plant1.setGardenId(garden2.getId());
            plant1 = ServicesHolder.getInstance().getPlantService().store(plant1);

            Plant plant2 = new Plant();
            plant2.setName("Zvonek");
            plant2.setType("zvonek");
            plant2.setFrom(format.parse("30 10 2017"));
            plant2.setTo(null);
            plant2.settFrom(format.parse("1 1 2015"));
            plant2.settTo(null);
            plant2.setGeometry(new JGeometry(500, 500, 0));
            plant2.setPhoto(getPhoto("./src/main/resources/images/plants/bell.jpeg"));
            plant2.setGardenId(garden4.getId());
            plant2 = ServicesHolder.getInstance().getPlantService().store(plant2);

            Plant plant3 = new Plant();
            plant3.setName("Slunečnice");
            plant3.setType("slunečnice");
            plant3.setFrom(format.parse("1 11 2017"));
            plant3.setTo(null);
            plant3.settFrom(format.parse("1 1 2016"));
            plant3.settTo(null);
            plant3.setGeometry(new JGeometry(550, 500, 0));
            plant3.setPhoto(getPhoto("./src/main/resources/images/plants/sunflower.png"));
            plant3.setGardenId(garden4.getId());
            plant3 = ServicesHolder.getInstance().getPlantService().store(plant3);

            Plant plant4 = new Plant();
            plant4.setName("Růže");
            plant4.setType("růže");
            plant4.setFrom(format.parse("1 11 2017"));
            plant4.setTo(null);
            plant4.settFrom(format.parse("1 1 2015"));
            plant4.settTo(null);
            plant4.setGeometry(new JGeometry(600, 430, 0));
            plant4.setPhoto(getPhoto("./src/main/resources/images/plants/red_flower.png"));
            plant4.setGardenId(garden4.getId());
            plant4 = ServicesHolder.getInstance().getPlantService().store(plant4);
        } catch (Exception e) {
            System.out.println("Někdo zapomněl zalít květiny...");
        }

        // FOOTPATH
        Footpath footpath = new Footpath();
        footpath.setGeometry(new JGeometry(2, 0, new int[]{1, 2, 1},
                new double[]{
                        401, 324,
                        970, 324
                }));

        Footpath footpath2 = new Footpath();
        footpath2.setGeometry(new JGeometry(2, 0, new int[]{1, 2, 1},
                new double[]{
                        946, 10,
                        920, 620
                }));

        Footpath footpath3 = new Footpath();
        footpath3.setGeometry(new JGeometry(2, 0, new int[]{1, 2, 1},
                new double[]{
                        339,391,
                        339,633,
                }));

        Footpath footpath4 = new Footpath();
        footpath4.setGeometry(new JGeometry(2, 0, new int[]{1, 2, 1},
                new double[]{
                        22,336,
                        335,339,
                        338,300
                }));

        ServicesHolder.getInstance().getFootpathService().store(footpath);
        ServicesHolder.getInstance().getFootpathService().store(footpath2);
        ServicesHolder.getInstance().getFootpathService().store(footpath3);
        ServicesHolder.getInstance().getFootpathService().store(footpath4);
    }

    /**
     * Get photo from path
     * @param path Path string
     * @return byte array photo
     */
    private byte[] getPhoto(String path) {
        Path imagePath = Paths.get(path);
        try {
            return Files.readAllBytes(imagePath);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Method for downloading all objects from database.
     */
    public void downloadDataFromDatabase(){
        listOfModels.clear();

        listOfModels.addAll(ServicesHolder.getInstance().getAnimalService()
                .getAll());
        listOfModels.addAll(ServicesHolder.getInstance().getFootpathService()
                .getAll());
        listOfModels.addAll(ServicesHolder.getInstance().getGardenService()
                .getAll());
        listOfModels.addAll(ServicesHolder.getInstance().getHutchService()
                .getAll());
        listOfModels.addAll(ServicesHolder.getInstance().getLakeService()
                .getAll());
        listOfModels.addAll(ServicesHolder.getInstance().getPlantService()
                .getAll());

    }

    /**
     * Method for generating animal object.
     * @param name Name of the animal.
     * @param species Species of the animal.
     * @param photo Photo of the animal.
     * @param date Date of creation of the animal object.
     * @param hutchId Id of the hutch, where is animal object stored.
     * @return Final animal object.
     */
    public Animal generateAnimal(String name, String species, byte[] photo,
                                 Date date, String hutchId){
        Animal animal = new Animal();
        animal.setName(name);
        animal.setSpecies(species);
        animal.setFrom(date);
        animal.setTo(null);
        animal.setHutchId(hutchId);
        animal.setPhoto(photo);

        return animal;
    }

    /**
     * Method for creating plant based on point.
     * @param root Point where is plant planted.
     * @param name Name of the plant.
     * @param type Type of the plant.
     * @param photo Photo of the plant-
     * @param gardenId Id of the garden where is plant planted.
     * @return Final plant object.
     */
    public Plant pointToPlant(Point2D root, String name, String type,
                              byte[] photo, String gardenId){
        Plant plant = new Plant();
        plant.setName(name);
        plant.setType(type);
        plant.setFrom(new Date());
        plant.setTo(null);
        plant.settFrom(new Date());
        plant.settTo(null);
        plant.setGeometry(new JGeometry(root.getX(), root.getY(), 0));

        plant.setPhoto(photo);
        plant.setGardenId(gardenId);

        return plant;
    }

    /**
     * Method for creating garden based on polygon
     * @param polygon Polygon to transform to garden
     * @return Final garden object.
     */
    public Garden polygonToGarden(Polygon polygon){
        Double translateX = polygon.getTranslateX();
        Double translateY = polygon.getTranslateY();
        double[] points = new double[polygon.getPoints().size()];
        List<Double> p = polygon.getPoints();

        for(int i = 0; i < polygon.getPoints().size(); i += 2){
            points[i] = p.get(i) + translateX;
            points[i+1] = p.get(i+1) + translateY;
        }

        Garden garden = new Garden();
        garden.setGeometry(new JGeometry(3, 0, new int[]{1,1003,1}, points));

        return garden;
    }

    /**
     * Method for creating footpath based on polyline.
     * @param polyline Polyline to transform to footpath.
     * @return Final footpath object.
     */
    public Footpath polylineToFootpath(Polyline polyline){
        double[] points = new double[polyline.getPoints().size()];
        List<Double> p = polyline.getPoints();

        for(int i = 0; i < polyline.getPoints().size(); i++){
            points[i] = p.get(i);
        }

        Footpath footpath = new Footpath();
        footpath.setGeometry(new JGeometry(2, 0, new int[]{1,2,1}, points));

        return footpath;
    }

    /**
     * Method for creating lake based od circle.
     * @param circle Circle to transform to lake.
     * @return Final lake object.
     */
    public Lake circleToLake(Circle circle){
        Lake lake = new Lake();
        lake.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 4},
                new double[]{
                        circle.getCenterX(), circle.getCenterY()+
                        circle.getRadius(),
                        circle.getCenterX(), circle.getCenterY()-
                        circle.getRadius(),
                        circle.getCenterX()+circle.getRadius(),
                        circle.getCenterY()}));

        return lake;
    }

    /**
     * Method for creating hutch based on rectangle.
     * @param rectangle Rectangle to transform to hutch.
     * @return Final hutch object.
     */
    public Hutch rectangleToHutch(Rectangle rectangle){
        Hutch hutch = new Hutch();
        hutch.setGeometry(new JGeometry(3, 0, new int[]{1, 1003, 3},
                new double[]{
                        rectangle.getX(), rectangle.getY(),
                        rectangle.getX()+rectangle.getWidth(),
                        rectangle.getY()+rectangle.getHeight()}));
        return hutch;
    }

    public List<Model> getListOfModels() {
        return listOfModels;
    }

    public Footpath storeFootpath(Footpath footpath){

        return ServicesHolder.getInstance().getFootpathService()
                .store(footpath);
    }

    public Hutch storeHutch(Hutch hutch){

        return ServicesHolder.getInstance().getHutchService().store(hutch);
    }

    public Lake storeLake(Lake lake){

        return ServicesHolder.getInstance().getLakeService().store(lake);
    }

    public Plant storePlant(Plant plant){

        return ServicesHolder.getInstance().getPlantService().store(plant);
    }

    public Garden storeGarden(Garden garden){

        return ServicesHolder.getInstance().getGardenService().store(garden);
    }

    public Animal storeAnimal(Animal animal){

        return ServicesHolder.getInstance().getAnimalService().store(animal);
    }

    public void deleteFootpath(Footpath footpath){
        ServicesHolder.getInstance().getFootpathService()
                .delete(footpath.getId());
    }

    public List<Plant> getAllPlantsFromGarden(String gardenId){
        return ServicesHolder.getInstance().getGardenService()
                .getPlants(gardenId);
    }

    public List<Animal> getAllAnimalsFromHutch(String hutchId){
        return ServicesHolder.getInstance().getHutchService()
                .getAnimals(hutchId);
    }

    public void deleteHutch(Hutch hutch){
        ServicesHolder.getInstance().getHutchService().delete(hutch.getId());
    }

    public void deleteLake(Lake lake){
        ServicesHolder.getInstance().getLakeService().delete(lake.getId());
    }

    public void deletePlant(Plant plant){
        ServicesHolder.getInstance().getPlantService().delete(plant.getId());
    }

    public void deleteGarden(Garden garden){
        ServicesHolder.getInstance().getGardenService().delete(garden.getId());
    }

    public void deleteAnimal(Animal animal){
        ServicesHolder.getInstance().getAnimalService().delete(animal.getId());
    }

    /**
     * Delete old Hutch and store the new Hutch, keeping all the original
     * animals in it.
     * @param oldHutch - hutch to be deleted
     * @param newHutch - hutch to be saved, keeps animals from the original
     *                 hutch
     * @return Hutch - updated (new) hutch returned by database
     */
    public Hutch updateHutch(Hutch oldHutch, Hutch newHutch) {
        // get all animals in old hutch
        List<Animal> animals = getAllAnimalsFromHutch(oldHutch.getId());
        //delete old hutch - deletes also all animals in it
        deleteHutch(oldHutch);
        //store new hutch and store all animals into it
        newHutch = storeHutch(newHutch);
        for (Animal animal : animals) {
            animal.setHutchId(newHutch.getId());
            storeAnimal(animal);
        }

        return newHutch;
    }

    /**
     * Method that updates plant in database.
     * @param plant to update.
     * @return Updated plant.
     */
    public Plant updatePlant(Plant plant) {
        plant = ServicesHolder.getInstance().getPlantService().update(plant);

        return plant;
    }

    /**
     * Return animals older than the oldest plant.
     * Plants may be already dead, but their original live time age still
     * counts.
     * @return Map<Long, List<Animal>> animals older than the oldest plant
     */
    public Map<Long, List<Animal>> getAnimalsOlderThanOldestPlant() {
        return ServicesHolder.getInstance().getAnimalService()
                .getOlderAnimalsThanOldestPlant();
    }

    /**
     * Return list of Plants with the same live time (same age).
     * Plant may be already dead - all plants are compared.
     * @return List<Plant> list of plants with the same live time
     */
    public List<Plant> getPlantsWithTheSameLiveTime() {
        Map<Long, List<Plant>> plantsMap = ServicesHolder.getInstance()
                .getPlantService().getListOfPlantWithSameLiveTime();

        // sort ages
        List<Long> sortedAges = new ArrayList<>(plantsMap.keySet());
        Collections.sort(sortedAges);

        // get list of animals for each age value and add them to result list
        List<Plant> plants = new ArrayList<>();
        for (Long age : sortedAges) {
            if (plantsMap.get(age).size() <= 1) continue; // get only more than one flowers with same age
            for (Plant plant : plantsMap.get(age)) {
                plant.setAge(age);
                plants.add(plant);
            }
        }

        return plants;
    }

    /**
     * Get hutches with largest area and the area
     * @return Area and list of hutches with this area size
     */
    public Map<Long, List<Hutch>> getBiggestHutches() {
        return ServicesHolder.getInstance().getHutchService()
                .getTheGiggestHutches();
    }

    /**
     * Get sum of area of all lakes.
     * @return Area of all lakes together.
     */
    public double getLakesSpace() {
        return ServicesHolder.getInstance().getLakeService().getLakeSpace();
    }

    /**
     * Get all gardens that touches lakes.
     * @return List of gardens in contact with any lake
     */
    public List<Garden> getGardensInContactWithLakes() {
        return ServicesHolder.getInstance().getGardenService()
                .getGardentInContactWithWater();
    }

    /**
     * Get IDs of all paths that are crossing any path anywhere = all paths that have crossroads on them.
     * @return Set of IDs of paths that have crossroads
     */
    public Set<String> getCrossingPathsIds() {
        List<Map<String, String>> crosses = ServicesHolder
                .getInstance().getFootpathService().getIntersectFootpathes();

        Set<String> pathIds = new HashSet<>();

        for (Map<String, String> crossing : crosses) {
            pathIds.addAll(crossing.keySet());
            pathIds.addAll(crossing.values());
        }

        return pathIds;
    }

    /**
     * Get ID of garden that is in contact with the largest lake (meaning area).
     * @return ID of the largest lake
     */
    public String getGardenInContactWithBiggestLake(){
        return ServicesHolder.getInstance().getGardenService()
                .getGardenIDWithTheBiggestLakeInContact();
    }

    /**
     * Get Gardens that intersects Lakes.
     * @return String1 is ID of garden, String2 is ID of Lake, Long is
     * intersecting area
     */
    public Map<Map<String, String>, Long> getGardenIntersectionsWithLakes() {
        return ServicesHolder.getInstance().getGardenService()
                .getIntersectionWithLakes();
    }
}
