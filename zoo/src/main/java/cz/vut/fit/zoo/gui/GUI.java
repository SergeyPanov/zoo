package cz.vut.fit.zoo.gui;


import cz.vut.fit.zoo.model.*;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import cz.vut.fit.zoo.zooShapes.*;

import javafx.scene.shape.Circle;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Class Extends Application class.
 * Most important class of GUI. Sets up graphical objects, mediates interaction
 * between database and user.
 * @author xfouka01, xstast24
 */
@Component
public class GUI extends Application {

    private DatabaseService databaseService;

    private Circle currentCircle;
    private Rectangle currentRectangle;
    private Polyline currentPolyline;
    private ZooPolygon currentPolygon;

    private Stage window;
    private Scene mainScene;
    private BorderPane mainLayout;
    private GridPane loginLayout;
    private Pane drawPane;

    private Menu createItemMenu;
    private MenuItem menuItemLake;
    private MenuItem menuItemAnimal;
    private MenuItem menuItemPlant;
    private MenuItem menuItemHutch;
    private MenuItem menuItemGarden;
    private MenuItem menuItemFootpath;

    private DrawObjects drawObjects;
    static List<Shape> displayedObjects = new ArrayList<>();

    // used to detect intentional inserting of objects
    private Boolean holdingCtrl = false;
    // used when moving objects
    private double tmpX, tmpY, oldX, oldY, oldTranslateX, oldTranslateY;
    private List<ZooCircle> tmpPlants = new ArrayList<>();


    /**
     * Method for launching gui.
     * @param args arguments of gui.
     */
    public void gui(String[] args) {
        launch(args);
    }

    /**
     * Method that is invoked when gui launched.
     * @param primaryStage first stage shown.
     * @throws Exception thrown by GUI class.
     */
    @Override
    public void start(Stage primaryStage) throws Exception  {
        databaseService = new DatabaseService();
        databaseService.downloadDataFromDatabase();

        drawPane = new Pane();

        drawObjects = new DrawObjects(databaseService.getListOfModels(),
                drawPane);
        drawObjects.drawAllObjects();

        setupLoginWindow(primaryStage);
        setupWorkingWindow();

        mainLayout.setCenter(drawPane);

        Scene loginScene = new Scene(loginLayout, 1000, 700);

        mainScene = new Scene(mainLayout, 1000, 700);
        mainScene.getStylesheets().add("css/style.css");
        window.setScene(loginScene);

        window.show();

        window.setOnCloseRequest(event -> {
            event.consume();
            window.close();
        });
    }

    /**
     * Method for clearing database and drawing of changes.
     */
    private void clearEverything(){
        databaseService.deleteAll();
        drawObjects.updateListOfModels(databaseService.getListOfModels());
        drawObjects.drawAllObjects();
    }

    /**
     * Method for filling database with sample data and drawing of the data.
     */
    private void initEverything(){
        clearEverything();
        databaseService.initData();
        databaseService.downloadDataFromDatabase();
        drawObjects.updateListOfModels(databaseService.getListOfModels());
        drawObjects.drawAllObjects();

    }

    /**
     * Method for setting up of the login window and checking users login
     * and password.
     * @param primaryStage first stage shown.
     */
    private void setupLoginWindow(Stage primaryStage){
        this.window = primaryStage;
        this.window.setTitle("Zoologická zahrada");

        loginLayout = new GridPane();
        loginLayout.setHgap(10);
        loginLayout.setVgap(8);
        Button loginButton = new Button();
        loginButton.setText("START");
        GridPane.setConstraints(loginButton, 1,2);
        // login button click listener
        loginButton.setOnAction(e -> {
            window.setScene(mainScene);
            });

        loginLayout.getChildren().addAll(loginButton);
        loginLayout.setAlignment(Pos.CENTER);
    }

    /**
     * Method for setting up main working window.
     */
    private void setupWorkingWindow(){
        createItemMenu = new Menu("Přidat objekt");

        menuItemLake = new MenuItem("Jezero");
        createItemMenu.getItems().add(menuItemLake);

        menuItemAnimal = new MenuItem("Zvíře");
        createItemMenu.getItems().add(menuItemAnimal);

        menuItemPlant = new MenuItem("Květina");
        createItemMenu.getItems().add(menuItemPlant);

        menuItemHutch = new MenuItem("Kotec");
        createItemMenu.getItems().add(menuItemHutch);

        menuItemGarden = new MenuItem("Záhon");
        createItemMenu.getItems().add(menuItemGarden);

        menuItemFootpath = new MenuItem("Stezka");
        createItemMenu.getItems().add(menuItemFootpath);


        ChoiceBox<String> regimeSelector = new ChoiceBox<>();
        regimeSelector.getItems().add("Režim vkládání");
        regimeSelector.getItems().add("Režim přesunu");
        regimeSelector.getItems().add("Režim mazání");
        regimeSelector.getItems().add("Režim výběru");
        regimeSelector.setValue("Režim vkládání");
        setupListenersForInsertingObjects();

        // regime menu listener to actualize mode
        regimeSelector.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
            switch (newValue) {
                case "Režim vkládání":
                    dropAllListeners();
                    setupListenersForInsertingObjects();
                    createItemMenu.setDisable(false);
                    break;
                case "Režim přesunu":
                    dropAllListeners();
                    setupListenersForMovingObjects();
                    createItemMenu.setDisable(true);
                    break;
                case "Režim mazání":
                    dropAllListeners();
                    setupListenersForDeletingObjects();
                    createItemMenu.setDisable(true);
                    break;
                case "Režim výběru":
                    dropAllListeners();
                    setupListenersForSelectingObjects();
                    createItemMenu.setDisable(true);
                    break;
                default:
                    dropAllListeners();
                    createItemMenu.setDisable(true);
                    break;
            }
        });

        Menu regimeMenu = new Menu();
        regimeMenu.setId("transparent");
        regimeMenu.setGraphic(regimeSelector);

        // menu for TEMPORAL examples
        MenuItem temporal1 = new MenuItem("Živá zvířata starší než rostliny");
        temporal1.setOnAction(click -> {
            TemporalRequestsWindows temporalWindows = new TemporalRequestsWindows(databaseService);
            temporalWindows.displayOlderAnimalsThanOldestPlant();
        });

        MenuItem temporal2 = new MenuItem("Rostliny se stejnou délkou života");
        temporal2.setOnAction(click -> {
            TemporalRequestsWindows temporalWindows = new TemporalRequestsWindows(databaseService);
            temporalWindows.displayPlantsWithSameLiveTime();
        });

        MenuItem temporal3 = new MenuItem("Zobrazit rostliny živé v období...");
        temporal3.setOnAction(click -> {
            TemporalRequestsWindows temporalWindows = new TemporalRequestsWindows(databaseService);
            temporalWindows.displayPlantsLifePeriodRequest();
        });

        Menu temporalMenu = new Menu("Temporální dotazy");
        temporalMenu.getItems().addAll(temporal1, temporal2, temporal3);

        // menu for SPATIAL examples
        MenuItem spatial1 = new MenuItem("Největší kotce a jejich plocha");
        spatial1.setOnAction(click -> displayBiggestHutches());

        MenuItem spatial2 = new MenuItem("Plocha všech jezer");
        spatial2.setOnAction(click -> displayTotalLakeSpace());

        MenuItem spatial3 = new MenuItem("Všechny záhony sousedící s jezery");
        spatial3.setOnAction(click -> displayGardensInContactWithLakes());

        MenuItem spatial4 = new MenuItem("Křížící se cesty");
        spatial4.setOnAction(click -> displayCrossingPaths());

        MenuItem spatial5 = new MenuItem("Záhon vedle největšího jezera");
        spatial5.setOnAction(click -> displayGardenInContactWithBiggestLake());

        MenuItem spatial6 = new MenuItem("Překryvy jezer a záhonů a " +
                "jejich plochy");
        spatial6.setOnAction(click -> displayGardensIntersectionWithLakes());

        Menu spatialMenu = new Menu("Prostorové dotazy");
        spatialMenu.getItems().addAll(spatial1, spatial2, spatial3, spatial4,
                spatial5, spatial6);

        // Menu for dropping and initiating data
        MenuItem dropData = new MenuItem("Smazat databázi");
        dropData.setOnAction(click -> clearEverything());

        MenuItem initData = new MenuItem("Načíst ukázková data");
        initData.setOnAction(click -> initEverything());

        Menu dataMenu = new Menu("Data");
        dataMenu.getItems().addAll(dropData, initData);

        // create layout
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(regimeMenu, createItemMenu, temporalMenu,
                spatialMenu, dataMenu);
        menuBar.toFront();

        mainLayout = new BorderPane();
        mainLayout.setTop(menuBar);
    }

    /**
     * Method for dropping all listeners.
     */
    private void dropAllListeners(){
        dropListenersForInsertingObjects();
        dropListenersForMovingObjects();
        dropListenersForDeletingObjects();
        dropListenersForSelectingObjects();
    }

    /**
     * Method for dropping listeners associated with object insertion.
     */
    private void dropListenersForInsertingObjects(){
        drawPane.setOnMousePressed(null);
        drawPane.setOnMouseDragged(null);
        drawPane.setOnMouseReleased(null);

        mainScene.setOnKeyPressed(null);
        mainScene.setOnKeyReleased(null);
    }

    /**
     * Method for dropping listeners associated with object movement.
     */
    private void dropListenersForMovingObjects(){
        for (Shape item : displayedObjects){
            item.setOnMousePressed(null);
            item.setOnMouseDragged(null);
            item.setOnMouseReleased(null);
        }
    }

    /**
     * Method for dropping listeners associated with object deletion.
     */
    private void dropListenersForDeletingObjects(){
        for (Shape item : displayedObjects){
            item.setOnMouseClicked(null);
        }
    }

    /**
     * Method for dropping listeners associated with object selection.
     */
    private void dropListenersForSelectingObjects(){
        for (Shape item : displayedObjects){
            item.setOnMouseClicked(null);
        }
    }

    /**
     * Method for setting up listeners associated with object selection.
     */
    private void setupListenersForSelectingObjects(){
        for(Shape shape : displayedObjects) {
            shape.setOnMouseClicked(event -> {
                if(event.getSource() instanceof ZooPolygon){
                    GardenDetailsWindow gardenDetailsWindow =
                            new GardenDetailsWindow(databaseService,
                                    ((ZooPolygon) event.getSource())
                                            .getModel().getId());
                    gardenDetailsWindow.display();
                }
                else  if(event.getSource() instanceof ZooRectangle){
                    HutchDetailsWindow hutchDetailsWindow =
                            new HutchDetailsWindow(databaseService,
                                    ((ZooRectangle) event.getSource())
                                            .getModel().getId());
                    hutchDetailsWindow.display();
                }
            });
        }
    }

    /**
     * Method for setting up listeners associated with object deletion.
     */
    private void setupListenersForDeletingObjects(){
        for(Shape shape : displayedObjects){
            shape.setOnMouseClicked(event -> {
                if(event.getSource() instanceof ZooCircle){
                    if(((ZooCircle) event.getSource()).getModel()
                            instanceof Lake){
                        databaseService.deleteLake((Lake)((ZooCircle)
                                event.getSource()).getModel());
                    }
                    else if(((ZooCircle) event.getSource()).getModel()
                            instanceof Plant){
                        databaseService.deletePlant((Plant)((ZooCircle)
                                event.getSource()).getModel());
                    }
                }
                else if(event.getSource() instanceof ZooPolygon){
                    databaseService.deleteGarden((Garden)((ZooPolygon)
                            event.getSource()).getModel());
                }
                else if (event.getSource() instanceof ZooRectangle){
                    databaseService.deleteHutch((Hutch)((ZooRectangle)
                            event.getSource()).getModel());
                }
                else if (event.getSource() instanceof ZooPolyline){
                    databaseService.deleteFootpath((Footpath) ((ZooPolyline)
                            event.getSource()).getModel());
                }

                databaseService.downloadDataFromDatabase();
                drawObjects.updateListOfModels(databaseService
                        .getListOfModels());
                drawObjects.drawAllObjects();
                setupListenersForDeletingObjects();
            });
        }
    }

    /**
     * Method for setting up listeners associated with object movement.
     */
    private void setupListenersForMovingObjects(){
        for (Shape item : displayedObjects){
            // move CIRCLE
            if (item instanceof ZooCircle) {
                item.setOnMousePressed(clickEvent -> {
                    ((Circle) item).setCenterX(clickEvent.getX());
                    ((Circle) item).setCenterY(clickEvent.getY());
                    tmpX = clickEvent.getX();
                    tmpY = clickEvent.getY();
                });

                item.setOnMouseDragged(dragEvent -> {
                    ((Circle) item).setCenterX(dragEvent.getX());
                    ((Circle) item).setCenterY(dragEvent.getY());
                });

                item.setOnMouseReleased(releaseEvent -> {
                    ZooCircle object = (ZooCircle) releaseEvent.getSource();

                    // moving LAKE
                    if (object.getModel() instanceof Lake) {
                        // check for item collision, let user move
                        // lake only if not colliding with another item
                        for (Shape obj : displayedObjects) {
                            // collision with its original location is allowed
                            // collision with Gardens is allowed - floods
                            // (for easier example SELECT purposes later)
                            if (item.getId().equals(obj.getId()) ||
                                    (obj instanceof ZooPolygon)) {
                                continue;
                            }

                            // collision with footpath is allowed
                            if (obj instanceof ZooPolyline) {
                                object.toBack(); // footpath will be over
                                // the lake
                                continue;
                            }

                            // collision with other objects is not allowed
                            if (item.getBoundsInParent().intersects(
                                    obj.getBoundsInParent())) {
                                ((Circle) item).setCenterX(tmpX);
                                ((Circle) item).setCenterY(tmpY);
                                return;
                            }
                        }

                        // save changes to DATABASE
                        // delete original model from database
                        databaseService.deleteLake((Lake) object.getModel());
                        // actualize model of displayed object
                        object.setModel(databaseService.circleToLake(object));
                        // add new model to database
                        databaseService.storeLake((Lake) object.getModel());
                    }

                    // moving PLANT
                    if (object.getModel() instanceof Plant){
                        Point2D plantRoot = new Point2D(object.getCenterX(),
                                object.getCenterY());
                        Garden garden = null;
                        // check if planted in the Garden
                        for (Shape obj : displayedObjects) {
                            if ((obj instanceof ZooPolygon) && obj
                                    .getBoundsInParent().contains(plantRoot)) {
                                garden = (Garden) ((ZooPolygon)obj).getModel();
                                break;
                            }
                        }

                        // plant not planted in garden, reset original position
                        if (null == garden) {
                            ((Circle) item).setCenterX(tmpX);
                            ((Circle) item).setCenterY(tmpY);
                            return;
                        }

                        // is temporal object, can not be simply deleted
                        // and recreated, must be updated
                        Plant oldPlant = (Plant) object.getModel();
                        Plant newPlant = databaseService.pointToPlant(
                                plantRoot, oldPlant.getName(),
                                oldPlant.getType(), oldPlant.getPhoto(),
                                garden.getId());
                        newPlant.setId(oldPlant.getId());
                        newPlant = databaseService.updatePlant(newPlant);
                        object.setModel(newPlant);
                    }
                });
            }

            //move Rectangle
            if (item instanceof ZooRectangle) {
                item.setOnMousePressed(clickEvent -> {
                    Rectangle rectangle = (Rectangle) item;
                    // remember initial location in case of need
                    // to restore changes
                    tmpX = rectangle.getX();
                    tmpY = rectangle.getY();
                    // set new coordinates
                    rectangle.setX(clickEvent.getX() - rectangle.getWidth()/2);
                    rectangle.setY(clickEvent.getY() -
                            rectangle.getHeight()/2);
                });

                item.setOnMouseDragged(dragEvent -> {
                    Rectangle rectangle = (Rectangle) item;
                    rectangle.setX(dragEvent.getX() - rectangle.getWidth()/2);
                    rectangle.setY(dragEvent.getY() -
                            rectangle.getHeight()/2);
                });

                item.setOnMouseReleased(releaseEvent -> {
                    ZooRectangle object =
                            (ZooRectangle) releaseEvent.getSource();
                    // check collision with other items
                    for (Shape obj : displayedObjects) {
                        // collision with its original location is allowed
                        if (item.getId().equals(obj.getId())) {
                            continue;
                        }
                        // collision with other objects is not allowed,
                        // getBoundsInParent is used to reflect translation
                        // changes
                        if (item.getBoundsInParent().intersects(obj
                                .getBoundsInParent())) {
                            object.setX(tmpX);
                            object.setY(tmpY);
                            return;
                        }
                    }

                    Hutch oldHutch = (Hutch) object.getModel();
                    Hutch newHutch = databaseService.rectangleToHutch(object);
                    databaseService.updateHutch(oldHutch, newHutch);
                    object.setModel(newHutch);
                });
            }

            // move Polygon
            if (item instanceof ZooPolygon) {
                item.setOnMousePressed(clickEvent -> {
                    Polygon object = (Polygon) clickEvent.getSource();

                    // store Plants on this garden, so we can move them
                    // together with garden
                    tmpPlants.clear();
                    for (Shape obj : displayedObjects) {
                        if (obj instanceof ZooCircle) {
                            ZooCircle circle = (ZooCircle) obj;
                            if (circle.getModel() instanceof Plant) {
                                if (item.getBoundsInParent().contains(
                                        circle.getCenterX(),
                                        circle.getCenterY())){
                                    // add only Plants planted in this Garden
                                    tmpPlants.add((ZooCircle)obj);
                                }
                            }
                        }
                    }

                    oldX = clickEvent.getSceneX();
                    oldY = clickEvent.getSceneY();
                    oldTranslateX = object.getTranslateX();
                    oldTranslateY = object.getTranslateY();
                });

                item.setOnMouseDragged(dragEvent -> {
                    Polygon object = (Polygon) dragEvent.getSource();

                    double offsetX = dragEvent.getSceneX() - oldX;
                    double offsetY = dragEvent.getSceneY() - oldY;
                    double translateX = oldTranslateX + offsetX;
                    double translateY = oldTranslateY + offsetY;
                    object.setTranslateX(translateX);
                    object.setTranslateY(translateY);

                    // move Plants in it
                    for (ZooCircle plant : tmpPlants) {
                        // using only offset, cos after finished, the
                        // translation is nulled and just a real center
                        // is set
                        plant.setTranslateX(offsetX);
                        plant.setTranslateY(offsetY);
                    }
                });

                item.setOnMouseReleased(releaseEvent -> {
                    ZooPolygon object = (ZooPolygon) releaseEvent.getSource();

                    // check for items collision
                    for (Shape obj : displayedObjects) {
                        // collision with its original location is allowed
                        if (item.getId().equals(obj.getId())) {
                            continue;
                        }
                        // collision with plants is allowed, because plants
                        // on this garden are OK and
                        // plants in other gardens will have collision already,
                        // because of their gardens would collide
                        // collision with Lakes is allowed too
                        if (obj instanceof ZooCircle){
                            continue;
                        }

                        // collision with other objects is NOT allowed,
                        // getBoundsInParent is used to reflect translation
                        // changes
                        if (item.getBoundsInParent().intersects(
                                obj.getBoundsInParent())) {
                            // restore original location
                            object.setTranslateX(oldTranslateX);
                            object.setTranslateY(oldTranslateY);
                            // restore original location for plants in it
                            for (ZooCircle plant : tmpPlants) {
                                // using only offset, cos after finished,
                                // the translation is nulled and just a real
                                // center is set
                                plant.setTranslateX(0);
                                plant.setTranslateY(0);
                            }
                            return;
                        }
                    }

                    // move garden
                    double offsetX = releaseEvent.getSceneX() - oldX;
                    double offsetY = releaseEvent.getSceneY() - oldY;
                    double translateX = oldTranslateX + offsetX;
                    double translateY = oldTranslateY + offsetY;
                    object.setTranslateX(translateX);
                    object.setTranslateY(translateY);
                    // save changes to DATABASE
                    // delete original model from database
                    databaseService.deleteGarden((Garden) object.getModel());
                    // actualize model of displayed object
                    object.setModel(databaseService.polygonToGarden(object));
                    // add new model to database
                    Garden garden = databaseService.storeGarden(
                            (Garden) object.getModel());

                    // move all plants in the garden and store to DATABASE
                    for (ZooCircle plant : tmpPlants){
                        plant.setTranslateX(0);
                        plant.setTranslateY(0);
                        plant.setCenterX(plant.getCenterX() + offsetX);
                        plant.setCenterY(plant.getCenterY() + offsetY);
                        // not needed to delete plant, cos it was deleted with
                        // the Garden deletion before
                        // create the new one and set it as model to ZooCircle
                        Plant plantModel = (Plant) plant.getModel();
                        Point2D plantRoot = new Point2D(plant.getCenterX(),
                                plant.getCenterY());
                        plantModel = databaseService.pointToPlant(plantRoot,
                                plantModel.getName(), plantModel.getType(),
                                plantModel.getPhoto(), garden.getId());
                        plant.setModel(plantModel);
                        databaseService.storePlant(plantModel);
                    }
                });
            }

            // move Polyline
            if (item instanceof ZooPolyline) {
                item.setOnMousePressed(clickEvent -> {
                    Polyline object = (Polyline) clickEvent.getSource();
                    oldX = clickEvent.getSceneX();
                    oldY = clickEvent.getSceneY();
                    oldTranslateX = object.getTranslateX();
                    oldTranslateY = object.getTranslateY();
                });

                item.setOnMouseDragged(dragEvent -> {
                    Polyline object = (Polyline) dragEvent.getSource();

                    double offsetX = dragEvent.getSceneX() - oldX;
                    double offsetY = dragEvent.getSceneY() - oldY;
                    double translateX = oldTranslateX + offsetX;
                    double translateY = oldTranslateY + offsetY;
                    object.setTranslateX(translateX);
                    object.setTranslateY(translateY);
                });

                item.setOnMouseReleased(releaseEvent -> {
                    ZooPolyline object =
                            (ZooPolyline) releaseEvent.getSource();

                    // check for item collision, let user move footpath
                    // only if not colliding with another item
                    for (Shape obj : displayedObjects) {
                        // collision with its original location is allowed
                        // collision with lake and footpath is allowed
                        if (item.getId().equals(obj.getId()) ||
                                (obj instanceof ZooPolyline) ||
                                (obj instanceof ZooCircle)) {
                            item.toFront();
                            continue;
                        }
                        // collision with other objects is not allowed,
                        // getBoundsInParent is used to reflect translation
                        // changes
                        if (item.getBoundsInParent().intersects(
                                obj.getBoundsInParent())) {
                            object.setTranslateX(oldTranslateX);
                            object.setTranslateY(oldTranslateY);
                            return;
                        }
                    }

                    double offsetX = releaseEvent.getSceneX() - oldX;
                    double offsetY = releaseEvent.getSceneY() - oldY;
                    double translateX = oldTranslateX + offsetX;
                    double translateY = oldTranslateY + offsetY;
                    object.setTranslateX(translateX);
                    object.setTranslateY(translateY);

                    // save changes to DATABASE
                    // delete original model from database
                    databaseService.deleteFootpath((Footpath)
                            object.getModel());
                    // actualize model of displayed object
                    object.setModel(databaseService.polylineToFootpath(
                            object));
                    // add new model to database
                    databaseService.storeFootpath((Footpath)
                            object.getModel());
                });
            }
        }
    }

    /**
     * Method for setting up listeners associated with object insertion.
     */
    private void setupListenersForInsertingObjects(){
        menuItemLake.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) {
                    return;
                }

                currentCircle = new Circle();
                currentCircle.setFill(Color.GRAY);
                currentCircle.setCenterX(eventMousePressed.getX());
                currentCircle.setCenterY(eventMousePressed.getY());

                drawPane.getChildren().add(currentCircle);


                drawPane.setOnMouseDragged(eventMouseDragged -> {
                    if (!eventMouseDragged.isPrimaryButtonDown()) {
                        return;
                    }

                    currentCircle.setRadius(sqrt(pow(abs(
                            eventMousePressed.getY() -
                                    eventMouseDragged.getY()), 2) + pow(
                                            abs(eventMousePressed.getX() -
                                                    eventMouseDragged.getX()),
                            2)));
                });

                drawPane.setOnMouseReleased(eventMouseReleased -> {
                    drawPane.getChildren().remove(currentCircle);

                    drawObjects.drawLake(databaseService.storeLake(
                            databaseService.circleToLake(currentCircle)));
                    drawPane.getChildren().remove(currentCircle);
                });
            });
        });

        menuItemHutch.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) return;

                currentRectangle = new Rectangle();
                currentRectangle.setFill(Color.GRAY);
                currentRectangle.setX(eventMousePressed.getX());
                currentRectangle.setY(eventMousePressed.getY());

                drawPane.getChildren().add(currentRectangle);

                drawPane.setOnMouseDragged(eventMouseDragged -> {
                    if (!eventMouseDragged.isPrimaryButtonDown()) {
                    return;
                    }

                    double dx = eventMouseDragged.getX() -
                            currentRectangle.getX();
                    double dy = eventMouseDragged.getY() -
                            currentRectangle.getY();

                    if(dx < 0){
                        currentRectangle.setTranslateX(dx);
                        currentRectangle.setWidth(-dx);
                    }
                    else{
                        currentRectangle.setWidth(eventMouseDragged.getX() -
                                currentRectangle.getX());
                    }
                    if(dy < 0){
                        currentRectangle.setTranslateY(dy);
                        currentRectangle.setHeight(-dy);
                    }
                    else{
                        currentRectangle.setHeight(eventMouseDragged.getY() -
                                currentRectangle.getY());
                    }


                });

                drawPane.setOnMouseReleased(eventMouseReleased -> {
                    drawObjects.drawHutch(databaseService.storeHutch(
                            databaseService.rectangleToHutch(
                                    currentRectangle)));
                    drawPane.getChildren().remove(currentRectangle);
                });
            });
        });

        menuItemFootpath.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            // user has to hold Ctrl do draw a path
            mainScene.setOnKeyPressed(keyPressed -> {
                if (keyPressed.getCode() == KeyCode.CONTROL) {
                    holdingCtrl = true;
                    currentPolyline = new Polyline();
                    currentPolyline.setStroke(Color.GRAY);
                    drawPane.getChildren().add(currentPolyline);
                }
            });

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) return;
                if (!holdingCtrl) return;

                currentPolyline.setStrokeWidth(20);
                currentPolyline.getPoints().addAll(eventMousePressed.getX(),
                        eventMousePressed.getY());
            });

            drawPane.setOnMouseDragged(eventMouseDragged -> {
                if (holdingCtrl) currentPolyline.getPoints().addAll(
                        eventMouseDragged.getX(), eventMouseDragged.getY());
            });

            drawPane.setOnMouseReleased(eventMouseReleased -> {
                if (holdingCtrl) currentPolyline.getPoints().addAll(
                        eventMouseReleased.getX(), eventMouseReleased.getY());
            });

            mainScene.setOnKeyReleased(keyReleased -> {
                // user held down CTRL and now released it -> finish object
                // drawing
                if (keyReleased.getCode() == KeyCode.CONTROL) {
                    holdingCtrl = false;
                    // save to DB if path is not empty or not exceeding
                    // 999 points - ORACLE DB LIMIT (by default)
                    if (currentPolyline.getPoints().size() > 0 &&
                            currentPolyline.getPoints().size() <= 999) {
                        drawObjects.drawFootpath(databaseService.storeFootpath(
                                databaseService.polylineToFootpath(
                                        currentPolyline)));
                    }
                    drawPane.getChildren().remove(currentPolyline);
                }
            });
        });

        menuItemGarden.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            // user has to hold Ctrl to draw a polygon
            mainScene.setOnKeyPressed(keyPressed -> {
                if (keyPressed.getCode() == KeyCode.CONTROL) {
                    holdingCtrl = true;

                    currentPolygon = new ZooPolygon();
                    currentPolygon.setFill(Color.GRAY);
                    drawPane.getChildren().add(currentPolygon);
                }
            });

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) return;
                if (!holdingCtrl) return;

                currentPolygon.getPoints().addAll(eventMousePressed.getX(),
                        eventMousePressed.getY());
            });

            mainScene.setOnKeyReleased(keyReleased -> {
                // user held down CTRL and now released it -> finish
                // object drawing
                if (keyReleased.getCode() == KeyCode.CONTROL) {
                    holdingCtrl = false;

                    // Add to database only if polygon has at least
                    // 3 points (garden would be invisible otherwise)
                    if (currentPolygon.getPoints().size() >= 3){
                        drawObjects.drawGarden(databaseService.storeGarden(
                                databaseService.polygonToGarden(
                                        currentPolygon)));
                    }
                    drawPane.getChildren().remove(currentPolygon);
                }
            });
        });

        menuItemPlant.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            currentCircle = new Circle();
            currentCircle.setFill(Color.RED);
            currentCircle.setRadius(5);

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) {
                    return;
                }

                Point2D plantRoot = new Point2D(
                        eventMousePressed.getX(), eventMousePressed.getY());
                Boolean contains = false;
                ZooPolygon polygon = new ZooPolygon();
                for (Shape obj:displayedObjects) {
                    if((obj instanceof ZooPolygon) &&
                            (obj.contains(plantRoot))) {
                        contains = true;
                        polygon = (ZooPolygon) obj;
                    }
                }

                if(contains){
                    AddPlantWindow addPlantWindow = new AddPlantWindow();
                    addPlantWindow.display();

                    if(!addPlantWindow.getStorno()) {
                        byte[] photo = null;
                        Path path = addPlantWindow.getPath();
                        if (path != null) {
                            try {
                                photo = Files.readAllBytes(path);
                            } catch (java.io.IOException e) {
                                photo = null;
                            }
                        }

                        Plant plant = databaseService.pointToPlant(plantRoot,
                                addPlantWindow.getName(),
                                addPlantWindow.getSpecies(), photo,
                                polygon.getModel().getId());
                        drawObjects.drawPlant(plant);
                        databaseService.storePlant(plant);
                    }
                }
            });
        });

        menuItemAnimal.setOnAction(event -> {
            // clean other inserting listeners
            dropListenersForInsertingObjects();

            drawPane.setOnMousePressed(eventMousePressed -> {
                if (!eventMousePressed.isPrimaryButtonDown()) {
                    return;
                }

                Boolean contains = false;
                ZooRectangle rect = new ZooRectangle();

                for (Shape obj:displayedObjects) {
                    if((obj instanceof ZooRectangle)&&(obj.contains(
                            eventMousePressed.getX(),
                            eventMousePressed.getY()))) {
                        contains = true;
                        rect = (ZooRectangle) obj;
                    }
                }

                if(contains){
                    AddAnimalWindow addAnimalWindow = new AddAnimalWindow();
                    addAnimalWindow.display();

                    if(!addAnimalWindow.getStorno()) {
                        Path path = addAnimalWindow.getPath();
                        byte[] photo;
                        try {
                            photo = Files.readAllBytes(path);
                        } catch (java.io.IOException e) {
                            photo = null;
                        }
                        Animal animal = databaseService.generateAnimal(
                                addAnimalWindow.getName(),
                                addAnimalWindow.getSpecies(), photo,
                                addAnimalWindow.getDate(),
                                rect.getModel().getId());
                        databaseService.storeAnimal(animal);
                    }
                }
            });
        });
    }

    /**
     * Spatial example "Největší kotce a jejich plocha"
     * Paint largest hutches with pink color temporarily and notice user.
     */
    private void displayBiggestHutches() {
        Map<Long, List<Hutch>> biggestHutches =
                databaseService.getBiggestHutches();

        Long area = null;

        // there is only one Long key in this Map, because
        // only one value is the biggest Hutch area
        // but there may be multiple hutches with the same area
        List<Hutch> hutches = new ArrayList<>();
        for (Long areaValue : biggestHutches.keySet()) {
            area = areaValue; // remember to show to user later
            hutches.addAll(biggestHutches.get(areaValue));
        }

        // list to remember repainted objects
        List<Shape> paintedHutches = new ArrayList<>();

        // now repaint all biggest hutches to pink
        for (Shape object : displayedObjects) {
            if (object instanceof ZooRectangle) {
                for (Hutch hutch : hutches) {
                    if (hutch.getId().equals(
                            ((ZooRectangle)object).getModel().getId())) {
                        object.setFill(Color.DEEPPINK);
                        // remember the object to re-paint it back later
                        paintedHutches.add(object);
                    }
                }
            }
        }

        // let user know what happened and wait for confirmation
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle(null);
        infoDialog.setHeaderText(null);
        infoDialog.setContentText(String.format("Objekty přebarveny " +
                "na růžovo. Plocha: %d", area));
        infoDialog.showAndWait();

        // repaint objects back to their original color
        for (Shape obj : paintedHutches) {
            obj.setFill(Color.SADDLEBROWN);
        }
    }

    /**
     * Paint all lakes with pink color and display notification
     * with the total lake space.
     */
    private void displayTotalLakeSpace() {
        Long area = Math.round(databaseService.getLakesSpace());

        // remember to repaint later
        List<Shape> paintedLakes = new ArrayList<>();

        for (Shape object : displayedObjects) {
            if (object instanceof ZooCircle) {
                if (((ZooCircle)object).getModel() instanceof Lake) {
                    object.setFill(Color.DEEPPINK);
                    paintedLakes.add(object);
                }
            }
        }

        // let user know what happened and wait for confirmation
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle(null);
        infoDialog.setHeaderText(null);
        infoDialog.setContentText(String.format("Objekty přebarveny " +
                "na růžovo. Celková plocha: %d", area));
        infoDialog.showAndWait();

        for (Shape obj : paintedLakes) {
            obj.setFill(Color.BLUE);
        }
    }

    /**
     * Paint with pink color all gardens that are in contact with any lake.
     */
    private void displayGardensInContactWithLakes() {
        List<Garden> gardens = databaseService.getGardensInContactWithLakes();

        // remember to repaint later
        List<Shape> paintedGardens = new ArrayList<>();

        // repaint all corresponding gardens
        for (Shape object : displayedObjects) {
            if (object instanceof ZooPolygon) {
                for (Garden garden : gardens) {
                    if (garden.getId().equals(
                            ((ZooPolygon)object).getModel().getId())) {
                        object.setFill(Color.DEEPPINK);
                        paintedGardens.add(object);
                    }
                }
            }
        }

        // let user know what happened and wait for confirmation
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle(null);
        infoDialog.setHeaderText(null);
        infoDialog.setContentText("Objekty přebarveny na růžovo.");
        infoDialog.showAndWait();

        // repaint objects back
        for (Shape obj : paintedGardens) {
            obj.setFill(Color.GREEN);
        }
    }

    /**
     * Highlight all crossing paths with pink color
     */
    private void displayCrossingPaths() {
        Set<String> paths = databaseService.getCrossingPathsIds();

        List<Shape> paintedPaths = new ArrayList<>();

        // paint paths crossings any other path
        for (Shape object : displayedObjects) {
            if (object instanceof ZooPolyline) {
                if (paths.contains(((ZooPolyline)object).getModel().getId())) {
                    object.setStroke(Color.DEEPPINK);
                    paintedPaths.add(object);
                }
            }
        }

        // let user know what happened and wait for confirmation
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle(null);
        infoDialog.setHeaderText(null);
        infoDialog.setContentText("Objekty přebarveny na růžovo.");
        infoDialog.showAndWait();

        // repaint with original color
        for (Shape obj : paintedPaths) {
            obj.setStroke(Color.YELLOW);
        }
    }

    /**
     * Spatial example "Záhon vedle největšího jezera"
     * Temporarily paint with pink color garden that is in
     * contact with the largest lake.
     */
    private void displayGardenInContactWithBiggestLake() {
        String gardenId = databaseService.getGardenInContactWithBiggestLake();
        if (gardenId == null) return;

        for (Shape object : displayedObjects) {
            if (object instanceof ZooPolygon) {
                if (gardenId.equals(((ZooPolygon)object).getModel().getId())) {
                    // paint the object with pink color
                    object.setFill(Color.DEEPPINK);

                    // let user know what happened and wait for confirmation
                    Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
                    infoDialog.setTitle(null);
                    infoDialog.setHeaderText(null);
                    infoDialog.setContentText(String.format("Objekt " +
                            "přebarven na růžovo. ID záhonu: %s", gardenId));
                    infoDialog.showAndWait();

                    // reset original color
                    object.setFill(Color.GREEN);
                }
            }
        }
    }

    /**
     * Spatial example "Překryvy jezer a záhonů a jejich plochy"
     * Temporarily paint with pink color lakes that in contact with
     * garden and show its´ space.
     */
    private void displayGardensIntersectionWithLakes() {
        // first String is Garden ID, second is Lake ID and Long is area size
        Map<Map<String, String>, Long> intersects =
                databaseService.getGardenIntersectionsWithLakes();

        List<String> gardens = new ArrayList<>();
        List<String> lakes = new ArrayList<>();
        List<Long> areas = new ArrayList<>();

        List<Shape> paintedGardens = new ArrayList<>();
        List<Shape> paintedLakes = new ArrayList<>();

        // load all info
        for (Map<String, String> intersect : intersects.keySet()) {
            gardens.addAll(intersect.keySet());
            lakes.addAll(intersect.values());
            areas.add(intersects.get(intersect));
        }

        // paint intersecting objects
        for (Shape object : displayedObjects) {
            if (object instanceof ZooPolyline) {
                if (gardens.contains(
                        ((ZooPolyline)object).getModel().getId())) {
                    object.setFill(Color.DEEPPINK);
                    paintedGardens.add(object);
                }
            } else if (object instanceof ZooCircle) {
                if (lakes.contains(((ZooCircle)object).getModel().getId())) {
                    object.setFill(Color.DEEPPINK);
                    paintedLakes.add(object);
                }
            }
        }

        // prepare info string
        String info = String.format("Objekty přebarveny na " +
                "růžovo.%nZáhon ID,  Jezero ID,  plocha překryvu");
        for (int i=0; i < areas.size(); i++) {
            info = info + String.format("%n%s,  ", gardens.get(i));
            info = info + String.format("%s,  ", lakes.get(i));
            info = info + String.format("%d", areas.get(i));
        }

        // let user know what happened and wait for confirmation
        Stage infoDialog = new Stage();
        infoDialog.setTitle(null);
        infoDialog.setWidth(800);

        Label text = new Label(info);
        text.setAlignment(Pos.CENTER);

        Scene scene = new Scene(text);
        infoDialog.setScene(scene);
        infoDialog.showAndWait();

        // repaint with original color
        for (Shape obj : paintedGardens) {
            obj.setFill(Color.GREEN);
        }

        for (Shape obj : paintedLakes) {
            obj.setFill(Color.BLUE);
        }
    }
}
