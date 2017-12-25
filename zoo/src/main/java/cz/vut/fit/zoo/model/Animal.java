package cz.vut.fit.zoo.model;

import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
// ZVIRE

/**
 * Class represents an animal is living in a hutch.
 * Is mapped on "Zvire" table in the database.
 * Class contains static variables with requests on the table "Zvire".
 * @author xpanov00
 */
public class Animal extends Model{

    public static final String GET_ALL = "SELECT * FROM ZVIRE";
    public static final String GET = "SELECT * FROM ZVIRE WHERE id = '%s'";
    public static final String STORE = "INSERT INTO ZVIRE(id, jmeno, druh, fotka, od, do, kotec_id)" +
            " values (" +
            " '%s'," +
            " '%s', " +
            " '%s', " +
            " ordsys.ordimage.init(), " +
            " TO_DATE('%s', 'yyyy/mm/dd'), " +
            " TO_DATE(%s, 'yyyy/mm/dd'), " +
            " '%s' " +
            ")";
    public static final String GET_PHOTO = "SELECT fotka FROM ZVIRE where id = '%s' for update";
    public static final String UPDATE_PHOTO_1 = "UPDATE ZVIRE set fotka = ? where id = '%s'";
    public static final String UPDATE_PHOTO_2 = "UPDATE ZVIRE z SET" +
            " z.fotka_si=SI_StillImage(z.fotka.getContent()) where id = '%s'"
            ;
    public static final String UPDATE_PHOTO_3 = "UPDATE ZVIRE z SET" +
            " z.fotka_ac=SI_AverageColor(z.fotka_si)," +
            " z.fotka_ch=SI_ColorHistogram(z.fotka_si)," +
            " z.fotka_pc=SI_PositionalColor(z.fotka_si)," +
            " z.fotka_tx=SI_Texture(z.fotka_si) where id = '%s'"
            ;
    public static final String DELETE = "DELETE FROM ZVIRE WHERE id = '%s'";
    public static final String GET_FROM_HUTCH = "SELECT * FROM ZVIRE WHERE kotec_id = '%s'";
    public static final String GET_ALIVE_ANIMALS = "SELECT * FROM ZVIRE WHERE DO is NULL ";
    /**
     * Name of the animal.
     */
    private String name = "";
    /**
     * Species of the animal.
     */
    private String species = "";
    /**
     * Date of birth.
     */
    private Date from;
    /**
     * Date of dead.
     */
    private Date to;
    /**
     * Id of hutch where living.
     */
    private String hutchId = "";
    /**
     * Holds photo of animal.
     */
    private byte[] photo;
    // used for easier handling of temporal requests
    private Long age = null;


    public Animal(){
        super("");
    }

    public  Animal (OracleResultSet resultSet) throws SQLException, IOException {
        super(resultSet.getString("ID"));

        this.setName(resultSet.getString("Jmeno"));
        this.setSpecies(resultSet.getString("Druh"));
        this.setFrom(resultSet.getDate("OD"));
        this.setTo(resultSet.getDate("DO"));
        this.setHutchId(resultSet.getString("KOTEC_ID"));

        OrdImage imageProxy = (OrdImage) resultSet.getORAData("FOTKA", OrdImage.getORADataFactory());
        Optional<byte[]> image;
        try{
            image = Optional.ofNullable(imageProxy.getDataInByteArray());
        }catch (Exception e){
            image = Optional.empty();
        }
        this.setPhoto(image.orElse(new byte[0]));
    }



    public Date getFrom() {
        return (java.util.Date)from;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getHutchId() {
        return hutchId;
    }

    public void setHutchId(String hutchId) {
        this.hutchId = hutchId;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getAge() {
        Date toDate;
        if (to == null) {
            toDate = new Date();
        } else {
            toDate = to;
        }

        long diff = Math.abs(toDate.getTime() - from.getTime());
        return diff / (24 * 60 * 60 * 1000);
    }
}
