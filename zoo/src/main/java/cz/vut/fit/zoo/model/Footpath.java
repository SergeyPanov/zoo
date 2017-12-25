package cz.vut.fit.zoo.model;

import oracle.jdbc.internal.OracleResultSet;
import oracle.spatial.geometry.JGeometry;


/**
 * Class represents an footpath people can walk.
 * Is mapped on "Stezka" table in the database.
 * Class contains static variables with requests on the table "Stezka".
 * @author xpanov00
 */
public class Footpath extends Model {

    public static final String GET_ALL = "SELECT * FROM STEZKA";
    public static final String DELETE = "DELETE FROM STEZKA WHERE id = '%s'";
    public static final String GET = "SELECT * FROM STEZKA WHERE id = '%s'";
    public static final String STORE = "INSERT INTO STEZKA(ID, GEOMETRIE) VALUES(" +
            " '%s'," +
            " SDO_GEOMETRY(2002, NULL, NULL," +
            " SDO_ELEM_INFO_ARRAY(%s)," +
            " SDO_ORDINATE_ARRAY(%s)" +
            ")" +
            ")";

    public static final String GET_INTERSECT_FOOTPATHES = "SELECT s1.ID, s2.ID " +
            "  FROM STEZKA s1, STEZKA s2 " +
            "WHERE SDO_FILTER(s1.GEOMETRIE, s2.GEOMETRIE) = 'TRUE' AND s1.ID <> s2.ID";

    /**
     * Represents geometry of footpath(simple line).
     */
    private JGeometry geometry;

    public Footpath(){
        super("");
    }

    public Footpath(OracleResultSet resultSet) throws Exception {
        super(resultSet.getString("ID"));
        this.setGeometry(JGeometry.load(resultSet.getBytes("GEOMETRIE")));
    }


    public JGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(JGeometry geometry) {
        this.geometry = geometry;
    }
}
