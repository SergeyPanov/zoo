package cz.vut.fit.zoo.model;

import oracle.jdbc.internal.OracleResultSet;
import oracle.spatial.geometry.JGeometry;
// Jezero
/**
 * Class represents an lake.
 * Is mapped on "Jezero" table in the database.
 * Class contains static variables with requests on the table "Jezero".
 * @author xpanov00
 */
public class Lake extends Model {
    public static final String GET = "SELECT * FROM JEZERO WHERE id = '%s'";
    public static final String GET_ALL = "SELECT * FROM JEZERO";
    public static final String STORE = "INSERT INTO JEZERO(ID, GEOMETRIE) VALUES(" +
            " '%s'," +
            " SDO_GEOMETRY(2003, NULL, NULL," +
            " SDO_ELEM_INFO_ARRAY(%s)," +
            " SDO_ORDINATE_ARRAY(%s)" +
            "))";
    public static final String DELETE = "DELETE FROM JEZERO WHERE id = '%s'";

    public static final String GET_LAKE_SPACE = "SELECT SUM(SDO_GEOM.SDO_AREA(GEOMETRIE, 1)) plocha_jezer " +
            "  FROM JEZERO";

    /**
     * Geometry(circle).
     */
    private JGeometry geometry;

    public Lake(){
        super("");
    }

    public Lake(OracleResultSet resultSet) throws Exception {
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
