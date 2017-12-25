package cz.vut.fit.zoo.model;

/**
 * Class contains id.
 * Is parental class for other models.
 * @author xstat24
 */
public class Model {
    private String id;

    protected Model(String id){
        this.id = id;
    }

    /**
     *
     * @param id of the object
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return id of the object
     */
    public String getId(){
        return id;
    }
}
