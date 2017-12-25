package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.model.*;
import cz.vut.fit.zoo.zooShapes.ZooCircle;
import cz.vut.fit.zoo.zooShapes.ZooPolygon;
import cz.vut.fit.zoo.zooShapes.ZooPolyline;
import cz.vut.fit.zoo.zooShapes.ZooRectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import oracle.spatial.geometry.JGeometry;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Class for drawing of shapes to graphical interface.
 * @author xfouka01, xstast24
 */
public class DrawObjects {

    private List<Model> listOfZooObjects;
    private Pane drawPane;

    public DrawObjects(List<Model> listOfZooObjects, Pane drawPane) {
        this.listOfZooObjects = listOfZooObjects;
        this.drawPane = drawPane;
    }

    /**
     * Method for drawing circle based on lake
     * @param lake lake to be drawn
     */
    public void drawLake(Lake lake){
        JGeometry geometry = lake.getGeometry();

        double[] coordinates;

        coordinates = geometry.getOrdinatesArray();

        int middley = (int)(((int)coordinates[1] + (int)coordinates[3])/2);
        int middlex = (int)coordinates[0];
        int r = Math.abs(((int)coordinates[1] - (int)coordinates[3])/2);

        ZooCircle circle = new ZooCircle(lake);
        circle.setCenterX(middlex);
        circle.setCenterY(middley);
        circle.setRadius(r);
        circle.setFill(Color.BLUE);
        circle.setId(lake.getId());

        drawPane.getChildren().add(circle);
        // lake can be covered with footpath
        circle.toBack();
        GUI.displayedObjects.add(circle);
    }

    /**
     * Method for drawing rectangle based on hutch.
     * @param hutch Hutch to be drawn.
     */
    public void drawHutch(Hutch hutch){
        JGeometry geometry = hutch.getGeometry();

        double[] coordinates;

        coordinates = geometry.getOrdinatesArray();

        int leftUpPointx = (int)coordinates[0];
        int leftUpPointy = (int)coordinates[1];

        //TODO Je tohle jiste? Overit!
        int width = (int)coordinates[2] - leftUpPointx;
        int height = (int)coordinates[3] - leftUpPointy;

        ZooRectangle rectangle = new ZooRectangle(hutch);
        rectangle.setX(leftUpPointx);
        rectangle.setY(leftUpPointy);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(Color.SADDLEBROWN);
        rectangle.setId(hutch.getId());

        drawPane.getChildren().add(rectangle);
        GUI.displayedObjects.add(rectangle);
    }

    /**
     * Method for drawing polygon based on garden.
     * @param garden Garden to be drawn.
     */
    public void drawGarden(Garden garden){

        JGeometry geometry = garden.getGeometry();

        double[] coordinates;
        coordinates = geometry.getOrdinatesArray();

        Double[] coordinates_tras = new Double[coordinates.length];

        for (int i = 0; i < coordinates.length; i++) {
            coordinates_tras[i] = coordinates[i];
        }

        ZooPolygon polygon = new ZooPolygon();
        polygon.getPoints().addAll(coordinates_tras);
        polygon.setFill(Color.GREEN);
        polygon.setId(garden.getId());

        polygon.setModel(garden);

        drawPane.getChildren().add(polygon);
        GUI.displayedObjects.add(polygon);
    }

    /**
     * Method for drawing polyline based on footpath.
     * @param footpath Footpath to be drawn.
     */
    public void drawFootpath(Footpath footpath){

        JGeometry geometry = footpath.getGeometry();

        double[] coordinates;

        coordinates = geometry.getOrdinatesArray();

        ZooPolyline polyline = new ZooPolyline(footpath, coordinates);
        polyline.setStroke(Color.YELLOW);
        polyline.setStrokeWidth(20);
        polyline.setId(footpath.getId());


        drawPane.getChildren().add(polyline);
        GUI.displayedObjects.add(polyline);
    }

    /**
     * Method for drawing circle based on plant.
     * @param plant Plant to be drawn.
     */
    public void drawPlant(Plant plant){

        JGeometry geometry = plant.getGeometry();

        double[] coordinates;

        coordinates = geometry.getLabelPointXYZ();

        ZooCircle circle = new ZooCircle();
        circle.setCenterX((int)coordinates[0]);
        circle.setCenterY((int)coordinates[1]);
        circle.setRadius(10);
        circle.setFill(Color.RED);
        circle.setId(plant.getId());
        circle.setModel(plant);

        drawPane.getChildren().add(circle);
        GUI.displayedObjects.add(circle);
    }

    /**
     * Method for updating classes' list of database obejcts.
     * @param listOfZooObjects Database objects.
     */
    public void updateListOfModels(List<Model> listOfZooObjects){
        this.listOfZooObjects = listOfZooObjects;
    }

    /**
     * Method for drawing of all dabase objects based on local list.
     */
    public void drawAllObjects(){
        drawPane.getChildren().clear();
        GUI.displayedObjects.clear();
        for (Model item:listOfZooObjects) {
            if(item instanceof Lake)
                this.drawLake((Lake)item);
            else if (item instanceof Hutch)
                this.drawHutch((Hutch)item);
            else if (item instanceof Garden)
                this.drawGarden((Garden)item);
            else if (item instanceof Footpath)
                this.drawFootpath((Footpath)item);
            else if (item instanceof Plant)
                this.drawPlant((Plant)item);
        }
    }

    /**
     * Method for rotation of image clockwise by 90 degrees,
     * @param img Image to be rotated.
     * @return Rotated image.
     */
    public static BufferedImage rotateImageClockwise90(BufferedImage img) {
        if (img == null) return null;

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI / 2, img.getWidth() / 2,
                img.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_BILINEAR);
        img = op.filter(img, null);

        return img;
    }
}
