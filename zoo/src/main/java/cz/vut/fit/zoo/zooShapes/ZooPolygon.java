package cz.vut.fit.zoo.zooShapes;

import cz.vut.fit.zoo.model.Model;
import javafx.scene.shape.Polygon;

/**
 * Class Extends Polygon class.
 * Is used as graphical representation of garden.
 * @author xfouka01, xstat24
 */
public class ZooPolygon extends Polygon {

    private Model model;

    public ZooPolygon() {
        this.model = null;
    }

    public ZooPolygon(Model model) {
        this.model = model;
    }

    /**
     *
     * @param model of the object.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     *
     * @return model of the object.
     */
    public Model getModel() {
        return model;
    }
}
