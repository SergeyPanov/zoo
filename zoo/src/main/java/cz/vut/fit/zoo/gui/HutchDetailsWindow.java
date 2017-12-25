package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.model.Animal;
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
 * Class for window showing animals in selected hutch.
 * @author xfouka01, xstast24
 */
public class HutchDetailsWindow {

    DatabaseService databaseService;
    private TableView<Animal> table;
    private String hutchId;
    private ObservableList<Animal> animals;

    HutchDetailsWindow(DatabaseService databaseService, String hutchId){
        this.databaseService = databaseService;
        this.hutchId = hutchId;
    }

    /**
     * Method for displaying of the window.
     */
    public void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zobrazení zvířat");
        window.setWidth(850);
        window.setHeight(300);

        TableColumn<Animal, String> nameColumn = new TableColumn<>("Jméno");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Animal, String> typeColumn = new TableColumn<>("Druh");
        typeColumn.setMinWidth(200);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("species"));

        TableColumn<Animal, Date> fromColumn = new TableColumn<>("Od");
        fromColumn.setMinWidth(100);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn<Animal, Date> toColumn = new TableColumn<>("Do");
        toColumn.setMinWidth(100);
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

        // create table and set row listeners (to open animals' photo window
        // on double click)
        table = new TableView<>();
        table.setRowFactory( tv -> {
            TableRow<Animal> row = new TableRow<>();
            row.setOnMouseClicked(click -> {
                if (click.getClickCount() == 2 && (!row.isEmpty())) {
                    Animal rowAnimal = row.getItem();
                    AnimalDetailsWindow animalWindow = new AnimalDetailsWindow(
                            this, rowAnimal);
                    animalWindow.display();
                }
            });

            return row ;
        });

        animals = getAnimals();
        table.setItems(animals);
        table.getColumns().addAll(nameColumn, typeColumn, fromColumn,
                toColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();

    }

    /**
     * Method for getting all animals in selected hutch.
     * @return All animals in selected garden.
     */
    private ObservableList<Animal> getAnimals(){
        ObservableList<Animal> animals = FXCollections.observableArrayList();
        animals.addAll(databaseService.getAllAnimalsFromHutch(hutchId));
        return animals;
    }

    /**
     * Method for updating content of the window.
     */
    public void updateTable() {
        animals.removeAll(animals);
        animals.addAll(databaseService.getAllAnimalsFromHutch(hutchId));
    }
}
