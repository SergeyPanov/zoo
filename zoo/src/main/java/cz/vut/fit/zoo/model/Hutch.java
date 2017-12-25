package cz.vut.fit.zoo.model;

import oracle.jdbc.internal.OracleResultSet;
import oracle.spatial.geometry.JGeometry;


// Kotec
/**
 * Class represents an hutch animals are living in.
 * Is mapped on "Kotec" table in the database.
 * Class contains static variables with requests on the table "Kotec".
 * @author xpanov00
 */
public class Hutch extends Model{
    public static final String GET_ALL = "SELECT * FROM KOTEC";
    public static final String DELETE = "DELETE FROM KOTEC WHERE id = '%s'";
    public static final String GET = "SELECT * FROM KOTEC WHERE id = '%s'";
    public static final String STORE = "INSERT INTO KOTEC(ID, GEOMETRIE) VALUES(" +
            " '%s'," +
            " SDO_GEOMETRY(2003, NULL, NULL," +
            " SDO_ELEM_INFO_ARRAY(%s)," +
            " SDO_ORDINATE_ARRAY(%s)" +
            ")" +
            ")";

    public static final String GET_HUTCHES_WITH_THE_BIGGEST_SPACE = "SELECT vzor.* , SDO_GEOM.SDO_AREA(vzor.geometrie, 1) plocha " +
            "FROM KOTEC vzor " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 FROM KOTEC srovnej " +
            "    WHERE " +
            "          (SDO_GEOM.SDO_AREA(srovnej.geometrie, 1) > SDO_GEOM.SDO_AREA(vzor.geometrie, 1)) AND " +
            "          (vzor.ID <> srovnej.ID) " +
            ")";

    /**
     * Geometry of hutch(rectangle).
     */
    private JGeometry geometry;

    public Hutch(){
        super("");
    }

    public Hutch(OracleResultSet resultSet) throws Exception {
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
