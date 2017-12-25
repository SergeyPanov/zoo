package cz.vut.fit.zoo.model;

import oracle.jdbc.OracleResultSet;
import oracle.spatial.geometry.JGeometry;


// Zahon
/**
 * Class represents an garden plants were planted.
 * Is mapped on "Zahon" table in the database.
 * Class contains static variables with requests on the table "Zahon".
 * @author xpanov00
 */
public class Garden extends Model{


    public static final String GET_ALL = "SELECT * FROM ZAHON";
    public static final String DELETE = "DELETE FROM ZAHON WHERE id = '%s'";
    public static final String GET = "SELECT * FROM ZAHON WHERE id = '%s'";
    public static final String STORE = "INSERT INTO ZAHON(ID, GEOMETRIE, JMENO) VALUES(" +
            " '%s'," +
            " SDO_GEOMETRY(2003, NULL, NULL," +
            " SDO_ELEM_INFO_ARRAY(%s)," +
            " SDO_ORDINATE_ARRAY(%s)" +
            ")," +
            " '%s')";

    public static final String GET_GARDENS_IN_CONTACT_WITH_WATER = "SELECT zahon.* " +
            "FROM ZAHON zahon, JEZERO voda " +
            "WHERE " +
            "      (SDO_RELATE(zahon.geometrie, voda.geometrie, 'mask=ANYINTERACT') = 'TRUE')";


    public static final String GET_GARDEN_WITH_THE_BIGGEST_LAKE_IN_CONTACT = "SELECT z.ID " +
            "FROM ZAHON z " +
            "WHERE (SDO_RELATE(geometrie, (SELECT vzor.geometrie " +
            "                                                          FROM JEZERO vzor WHERE  NOT EXISTS ( " +
            "                                                              SELECT 1 FROM JEZERO srovnej " +
            "                                                              WHERE " +
            "                                                                    (SDO_GEOM.SDO_AREA(srovnej.geometrie, 1) > SDO_GEOM.SDO_AREA(vzor.geometrie, 1)) AND\n" +
            "                                                                    (vzor.ID <> srovnej.ID) " +
            "                                                          )), 'mask=ANYINTERACT') = 'TRUE')";

    public static final String GET_INTERSECTION_WITH_LAKES = "SELECT z.ID, j.ID , SDO_GEOM.SDO_AREA(SDO_GEOM.SDO_INTERSECTION(z.GEOMETRIE, j.GEOMETRIE), 1)" +
            "  FROM ZAHON z, JEZERO j " +
            "WHERE SDO_GEOM.SDO_AREA(SDO_GEOM.SDO_INTERSECTION(z.GEOMETRIE, j.GEOMETRIE), 1) IS NOT NULL";

    /**
     * Geometry of garden(polygon).
     */
    private JGeometry geometry;
    /**
     * Name of garden.
     */
    private String name = "";

    public Garden(){
        super("");
    }

    public Garden(OracleResultSet resultSet) throws Exception {
        super(resultSet.getString("ID"));

        this.setName(resultSet.getString("JMENO"));
        this.setGeometry(JGeometry.load(resultSet.getBytes("GEOMETRIE")));
    }


    public JGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(JGeometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
