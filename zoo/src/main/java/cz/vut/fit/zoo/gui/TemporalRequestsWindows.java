package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Plant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Class for showing results of temporal query.
 * @author xfouka01, xstast24
 */
public class TemporalRequestsWindows {
    private DatabaseService databaseService;
    private Long oldestPlantAge = null;

    TemporalRequestsWindows(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    /**
     * Display a window with table containing all plants, that have or had the same live time.
     * Plants may be already dead (well we have to know the live time, so they should be dead)
     * or might had been replanted somewhere else - still counts, it is the same plant.
     */
    public void displayPlantsWithSameLiveTime() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Květiny se stejnou délkou života");
        window.setWidth(1000);
        window.setHeight(400);

        TableColumn<Plant, Long> ageColumn = new TableColumn<>("Věk");
        ageColumn.setMinWidth(50);
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Plant, String> nameColumn = new TableColumn<>("Jméno");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Plant, String> typeColumn = new TableColumn<>("Druh");
        typeColumn.setMinWidth(150);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Plant, Date> fromColumn = new TableColumn<>("Od");
        fromColumn.setMinWidth(50);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn<Plant, Date> toColumn = new TableColumn<>("Do");
        toColumn.setMinWidth(50);
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

        TableColumn<Plant, Date> tFromColumn = new TableColumn<>("tOd");
        tFromColumn.setMinWidth(225);
        tFromColumn.setCellValueFactory(new PropertyValueFactory<>("tFrom"));

        TableColumn<Plant, Date> tToColumn = new TableColumn<>("tDo");
        tToColumn.setMinWidth(225);
        tToColumn.setCellValueFactory(new PropertyValueFactory<>("tTo"));


        ObservableList<Plant> plants = getSameAgePlants();

        TableView<Plant> table = new TableView<>();
        table.setItems(plants);
        table.getColumns().addAll(ageColumn, nameColumn, typeColumn, fromColumn, toColumn, tFromColumn, tToColumn);

        // create OK button
        Button ok = new Button("OK");
        ok.setOnAction(event -> window.close());

        //create whole layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(ok);
        borderPane.setBottom(table);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }

    /**
     * Display window with table of all animals, that are older than the oldest plant.
     * Display animals' ages and other properties (not photos).
     */
    public void displayOlderAnimalsThanOldestPlant() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zvířata starší než nejstarší květina");
        window.setWidth(800);
        window.setHeight(400);

        // create table
        TableColumn<Animal, Long> ageColumn = new TableColumn<>("Věk");
        ageColumn.setMinWidth(50);
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Animal, String> nameColumn = new TableColumn<>("Jméno");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Animal, String> typeColumn = new TableColumn<>("Druh");
        typeColumn.setMinWidth(200);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("species"));

        TableColumn<Animal, Date> fromColumn = new TableColumn<>("Od");
        fromColumn.setMinWidth(50);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn<Animal, Date> toColumn = new TableColumn<>("Do");
        toColumn.setMinWidth(50);
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));


        ObservableList<Animal> animals = getOldAnimals();

        TableView<Animal> table = new TableView<>();
        table.setItems(animals);
        table.getColumns().addAll(ageColumn, nameColumn, typeColumn, fromColumn, toColumn);

        // create table name and ok button
        Label tableName = new Label(String.format("Zvířata " +
                "starší než %d dní", oldestPlantAge));

        Button ok = new Button("OK");
        ok.setOnAction(event -> window.close());

        HBox topLabel = new HBox();
        topLabel.setSpacing(10);
        topLabel.getChildren().addAll(tableName, ok);

        //create whole layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topLabel);
        borderPane.setBottom(table);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }

    /**
     * Display a window where user sets boundaries of a time period,
     * for which they wants to see plants that lived in the period.
     */
    public void displayPlantsLifePeriodRequest(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zobrazit rostliny živé v období");
        window.setWidth(350);
        window.setHeight(150);

        Label odLabel = new Label("Od");
        GridPane.setConstraints(odLabel, 0,0);

        Label doLabel = new Label("Do");
        GridPane.setConstraints(doLabel, 1,0);

        DatePicker odDatePicker = new DatePicker();
        GridPane.setConstraints(odDatePicker, 0,1);

        DatePicker doDatePicker = new DatePicker();
        GridPane.setConstraints(doDatePicker, 1,1);

        Button closeButton = new Button("OK");
        GridPane.setConstraints(closeButton, 0,2);

        Button stornoButton = new Button("Zrušit");
        GridPane.setConstraints(stornoButton, 1,2);

        GridPane layout = new GridPane();

        layout.setHgap(10);
        layout.setVgap(8);
        layout.getChildren().addAll(odLabel, doLabel, odDatePicker, doDatePicker, closeButton, stornoButton);

        stornoButton.setOnAction(event -> {
            event.consume();
            window.close();
        });

        closeButton.setOnAction(event -> {
            if((odDatePicker.getValue() == null) || (doDatePicker.getValue() == null)) {
                WarningWindow.display("Varování", "Některá pole nejsou vyplněna");
                return;
            }
            event.consume();
            window.close();

            List<Plant> plantsInPeriod = databaseService.getPlantsInPeriod(
                    Date.from(odDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(doDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

            displayAllPlantsAliveInPeriod(plantsInPeriod, odDatePicker.getValue(), doDatePicker.getValue());
        });

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Get list of plants with same live time.
     * @return list of plants in format convenient for table view
     */
    private ObservableList<Plant> getSameAgePlants() {
        ObservableList<Plant> plants = FXCollections.observableArrayList();
        plants.addAll(databaseService.getPlantsWithTheSameLiveTime());

        return plants;
    }

    /**
     * Display a window with a table of all plants alive in a period.
     */
    private void displayAllPlantsAliveInPeriod(List<Plant> selectedPlants, LocalDate from, LocalDate to){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zobrazení květin v časovém období");
        window.setWidth(1000);
        window.setHeight(400);

        TableColumn<Plant, String> nameColumn = new TableColumn<>("Jméno");
        nameColumn.setMinWidth(175);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Plant, String> typeColumn = new TableColumn<>("Druh");
        typeColumn.setMinWidth(175);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Plant, Date> fromColumn = new TableColumn<>("Od");
        fromColumn.setMinWidth(50);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn<Plant, Date> toColumn = new TableColumn<>("Do");
        toColumn.setMinWidth(50);
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

        TableColumn<Plant, Date> tFromColumn = new TableColumn<>("tOd");
        tFromColumn.setMinWidth(225);
        tFromColumn.setCellValueFactory(new PropertyValueFactory<>("tFrom"));

        TableColumn<Plant, Date> tToColumn = new TableColumn<>("tDo");
        tToColumn.setMinWidth(225);
        tToColumn.setCellValueFactory(new PropertyValueFactory<>("tTo"));

        TableView table = new TableView<>();
        ObservableList<Plant> plants = FXCollections.observableArrayList();
        plants.addAll(selectedPlants);

        table.setItems(plants);
        table.getColumns().addAll(nameColumn, typeColumn, fromColumn, toColumn,
                tFromColumn, tToColumn);

        // create table info text
        String info = String.format("Rostliny za období:  %s   -   %s.", from.toString(), to.toString());
        Label text = new Label(info);

        // create layout
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(text, table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    /**
     * Return list of animals older than the oldest plant.
     * Also saves oldest plant age variable so it can be used later.
     * Plants may be already dead, but their original live time age still
     * counts.
     * @return ObservableList<Animal> list of animals older than the oldest
     * plant
     */
    private ObservableList<Animal> getOldAnimals() {
        Map<Long, List<Animal>> animalsMap =
                databaseService.getAnimalsOlderThanOldestPlant();

        // sort ages
        List<Long> sortedAges = new ArrayList<>(animalsMap.keySet());
        Collections.sort(sortedAges);

        // get list of animals for each age value and add them to result list
        List<Animal> animals = new ArrayList<>();
        for (Long age : sortedAges) {
            oldestPlantAge = age;
            animals.addAll(animalsMap.get(age));
        }

        ObservableList<Animal> animalsObs = FXCollections.observableArrayList();
        animalsObs.addAll(animals);

        return animalsObs;
    }
}
