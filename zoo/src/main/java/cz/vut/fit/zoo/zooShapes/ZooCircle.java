package cz.vut.fit.zoo.zooShapes;

import cz.vut.fit.zoo.model.Model;
import javafx.scene.shape.Circle;

/**
 * Class Extends Circle class.
 * Is used as graphical representation of lake and plant.
 * @author xfouka01, xstat24
 */
public class ZooCircle extends Circle {
    private Model model;

    public ZooCircle() {
        this.model = null;
    }

    public ZooCircle(Model model) {
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
