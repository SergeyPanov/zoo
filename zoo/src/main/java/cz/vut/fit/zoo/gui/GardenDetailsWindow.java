package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.model.Plant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

/**
 * Class for window showing plants on selected garden.
 * @author xfouka01, xstast24
 */
public class GardenDetailsWindow {

    DatabaseService databaseService;
    TableView<Plant> table;
    String gardenId;
    private ObservableList<Plant> plants;

    GardenDetailsWindow(DatabaseService databaseService, String gardenId){
        this.databaseService = databaseService;
        this.gardenId = gardenId;
    }

    /**
     * Method for displaying of the window.
     */
    public void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zobrazení květin");
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

        // create table and set row listeners (to open animals' photo window
        // on double click)
        table = new TableView<>();
        table.setRowFactory( tv -> {
            TableRow<Plant> row = new TableRow<>();
            row.setOnMouseClicked(click -> {
                if (click.getClickCount() == 2 && (!row.isEmpty())) {
                    Plant rowPlant = row.getItem();
                    PlantDetailsWindow plantWindow = new PlantDetailsWindow(
                            this, rowPlant);
                    plantWindow.display();
                }
            });

            return row ;
        });

        plants = getPlants();
        table.setItems(plants);
        table.getColumns().addAll(nameColumn, typeColumn, fromColumn, toColumn,
                tFromColumn, tToColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    /**
     * Method for getting all plants on selected garden.
     * @return All plants on selected garden.
     */
    public ObservableList<Plant> getPlants(){
        ObservableList<Plant> plants = FXCollections.observableArrayList();
        plants.addAll(databaseService.getAllPlantsFromGarden(gardenId));
        return plants;
    }

    /**
     * Method for updating content of the window.
     */
    public void updateTable() {
        plants.removeAll(plants);
        plants.addAll(databaseService.getAllPlantsFromGarden(gardenId));
    }
}
