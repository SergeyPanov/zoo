package cz.vut.fit.zoo.zooShapes;
import cz.vut.fit.zoo.model.Model;
import javafx.scene.shape.Polyline;

/**
 * Class Extends Polyline class.
 * Is used as graphical representation of footpath.
 * @author xfouka01, xstat24
 */
public class ZooPolyline extends Polyline {
    private Model model;

    public ZooPolyline() {
        this.model = null;
    }

    public ZooPolyline(Model model) {
        this.model = model;
    }

    public ZooPolyline(Model model, double... points){
        super(points);
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
