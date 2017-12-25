package cz.vut.fit.zoo.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Class for window where properties of added animal are set.
 * @author xfouka01, xstast24
 */
public class AddAnimalWindow {

    TextField nameInput;
    TextField speciesInput;
    LocalDate date;
    Path path;
    Boolean storno = false;

    /**
     * Method for displaying of the window.
     */
    public void display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Přidání zvířete");
        window.setWidth(500);
        window.setHeight(300);

        Label nameLabel = new Label("Jméno");
        GridPane.setConstraints(nameLabel, 0,0);
        Label speciesLabel = new Label("Druh");
        GridPane.setConstraints(speciesLabel, 0,1);
        Label dateLabel = new Label("Datum přidání");
        GridPane.setConstraints(dateLabel, 0,2);
        DatePicker datePicker = new DatePicker();
        GridPane.setConstraints(datePicker, 1,2);
        Label fileLabel = new Label("Obrázek");
        GridPane.setConstraints(fileLabel, 0,3);
        Button pathButton = new Button("Cesta");
        GridPane.setConstraints(pathButton, 1,3);
        pathButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(window);
            path = file.toPath();
        });

        datePicker.setOnAction(event -> {
            date = datePicker.getValue();
        });
        nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1,0);
        speciesInput = new TextField();
        GridPane.setConstraints(speciesInput, 1,1);

        Button closeButton = new Button("OK");
        GridPane.setConstraints(closeButton, 0,4);
        Button stornoButton = new Button("Zrušit");
        GridPane.setConstraints(stornoButton, 1,4);
        closeButton.setOnAction(event -> {
            if(nameInput == null || speciesInput == null || date == null ||
                    path == null) {
                WarningWindow.display("Varování", "Některá pole " +
                        "nejsou vyplněna");
                return;
            }
            window.close();
        });
        stornoButton.setOnAction(event -> {
            storno = true;
            window.close();
        });

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(8);
        layout.getChildren().addAll(nameLabel, nameInput, speciesLabel,
                speciesInput, datePicker, closeButton, dateLabel, fileLabel,
                pathButton, stornoButton);
        layout.setAlignment(Pos.CENTER);

        Scene addAnimalWidnowScene = new Scene(layout);

        window.setOnCloseRequest(event -> {
            event.consume();
            storno = true;
            window.close();
        });

        window.setScene(addAnimalWidnowScene);
        window.showAndWait();
    }

    public String getName() {
        return nameInput.getText();
    }

    public String getSpecies() {
        return speciesInput.getText();
    }

    public Date getDate() {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Path getPath() {
        return path;
    }

    public Boolean getStorno() {
        return storno;
    }
}
