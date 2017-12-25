package cz.vut.fit.zoo.zooShapes;

import cz.vut.fit.zoo.model.Model;
import javafx.scene.shape.Rectangle;

/**
 * Class Extends Rectangle class.
 * Is used as graphical representation of hutch.
 * @author xfouka01, xstat24
 */
public class ZooRectangle extends Rectangle {

    private Model model;

    public ZooRectangle() {
        this.model = null;
    }

    public ZooRectangle(Model model) {
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
