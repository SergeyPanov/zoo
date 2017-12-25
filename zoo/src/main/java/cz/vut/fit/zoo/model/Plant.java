package cz.vut.fit.zoo.model;

import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;
import oracle.spatial.geometry.JGeometry;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
// Kvetina

/**
 * Class represents an plants from a garden..
 * Is mapped on "Kvetina" table in the database.
 * Class contains static variables with requests on the table "Kvetina".
 * @author xpanov00
 */
public class Plant extends Model {

    public static final String GET_ALL = "SELECT * FROM KVETINA WHERE t_do IS NULL";
    public static final String GET_ALIVE = "SELECT * FROM KVETINA WHERE DO IS NULL AND t_do IS NULL";
    public static final String GET_ALL_IN_PERIOD = "SELECT * FROM KVETINA WHERE T_DO IS NULL AND OD >= TO_DATE('%s', 'yyyy/mm/dd') AND ( DO <= TO_DATE('%s', 'yyyy/mm/dd') OR DO IS NULL )";
    public static final String GET_ALIVE_IN_PERIOD = "SELECT * FROM KVETINA WHERE DO IS NULL AND T_DO IS NULL AND OD <= TO_DATE('%s', 'yyyy/mm/dd') AND ( OD <= TO_DATE('%s', 'yyyy/mm/dd') )";
    public static final String GET = "SELECT * FROM KVETINA WHERE id = '%s' AND t_do IS NULL";
    public static final String GET_PHOTO = "SELECT fotka FROM KVETINA where id = '%s' for update";
    public static final String UPDATE_PHOTO_1 = "UPDATE KVETINA set fotka = ? where id = '%s'";
    public static final String GET_HISTORY = "SELECT * FROM KVETINA WHERE ID = '%s' OR new_plant_id = '%s' ORDER BY t_od";
    public static final String UPDATE_PHOTO_2 = "UPDATE KVETINA z SET" +
            " z.fotka_si=SI_StillImage(z.fotka.getContent()) where id = '%s'"
            ;

    public static final String UPDATE_PHOTO_3 = "UPDATE KVETINA z SET" +
            " z.fotka_ac=SI_AverageColor(z.fotka_si)," +
            " z.fotka_ch=SI_ColorHistogram(z.fotka_si)," +
            " z.fotka_pc=SI_PositionalColor(z.fotka_si)," +
            " z.fotka_tx=SI_Texture(z.fotka_si) where id = '%s'"
            ;


    public static final String STORE = "INSERT INTO KVETINA(id, jmeno, druh, geometrie,  fotka, od, do, t_od, t_do, new_plant_id, zahon_id)" +
            " values (" +
            " '%s'," +  // id
            " '%s', " + //jmeno
            " '%s', " + //druh
            " SDO_GEOMETRY(" +  // geometrie
            "    2001, NULL , SDO_POINT_TYPE(%f, %f, NULL )," +
            "    NULL , NULL" +
            ")," +
            " ordsys.ordimage.init(), " +   // fotka
            " TO_DATE('%s', 'yyyy/mm/dd'), " +  // od
            " TO_DATE(%s, 'yyyy/mm/dd'), " +    // do
            " TO_DATE(%s, 'yyyy/mm/dd HH:MI:SS'), " +  // t_od
            " TO_DATE(%s, 'yyyy/mm/dd HH:MI:SS'), " +    // t_do
            " '%s', " + // new_plant_id
            " '%s'" +   // zahon_id
            ")";

    public static final String DELETE =  "UPDATE KVETINA SET T_DO = TO_DATE('%s', 'yyyy/mm/dd HH:MI:SS') WHERE ID = '%s'";
    public static final String FULLY_DELETE = "DELETE FROM KVETINA WHERE ID = '%s'";
    public static final String GET_FROM_GARDEN = "SELECT * FROM KVETINA WHERE ZAHON_ID = '%s'";
    public static final String GET_HISTORY_OF_EACH = "SELECT DISTINCT k1.ID FROM KVETINA k1" +
            "  INNER JOIN KVETINA k2 ON (k1.ID = k2.NEW_PLANT_ID) OR (k1.ID = k2.ID AND k2.NEW_PLANT_ID IS NULL)";


    private String name = "";
    private String type = "";
    private JGeometry geometry;
    private byte[] photo;
    private Date from;
    private Date to;

    private Date tFrom;
    private Date tTo;

    // used for easier creation of temporal plant tables
    private Long age = null;

    private String newPlantId = "";
    private String gardenId = "";


    public Plant(){
        super("");
    }

    public Plant(OracleResultSet resultSet) throws Exception {
        super(resultSet.getString("ID"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        this.setName(resultSet.getString("JMENO"));
        this.setType(resultSet.getString("DRUH"));
        this.setGeometry(JGeometry.load(resultSet.getBytes("GEOMETRIE")));
        OrdImage imageProxy = (OrdImage) resultSet.getORAData("FOTKA", OrdImage.getORADataFactory());
        Optional<byte[]> image;
        try{
            image = Optional.ofNullable(imageProxy.getDataInByteArray());
        }catch (Exception e){
            image = Optional.empty();
        }
        this.setPhoto(image.orElse(new byte[0]));

        this.settFrom(format.parse(resultSet.getDate("T_OD").toString() + " " + resultSet.getTime("T_OD").toString()));

        try {
            this.settTo(format.parse(resultSet.getDate("T_DO").toString() + " " + resultSet.getTime("T_DO").toString()));
        }catch (Exception e){
            this.settTo(null);
        }
        this.setFrom(resultSet.getDate("OD"));
        this.setTo(resultSet.getDate("DO"));
        this.setGardenId(resultSet.getString("ZAHON_ID"));


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(JGeometry geometry) {
        this.geometry = geometry;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getGardenId() {
        return gardenId;
    }

    public void setGardenId(String gardenId) {
        this.gardenId = gardenId;
    }

    public Date gettFrom() {
        return tFrom;
    }

    /* Must be named with capital T, so it can be called by automatic table cell factories*/
    public Date getTFrom() {
        return tFrom;
    }

    public void settFrom(Date tFrom) {
        this.tFrom = tFrom;
    }

    public Date gettTo() {
        return tTo;
    }

    /* Must be named with capital T, so it can be called by automatic table cell factories*/
    public Date getTTo() {
        return tTo;
    }

    public void settTo(Date tTo) {
        this.tTo = tTo;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getAge() {
        return this.age;
    }

    public String getNewPlantId() {
        return newPlantId;
    }

    public void setNewPlantId(String newPlantId) {
        this.newPlantId = newPlantId;
    }
}
