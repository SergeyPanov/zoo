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

import java.io.File;
import java.nio.file.Path;

/**
 * Class for window where properties of added plant are set.
 * @author xfouka01, xstast24
 */
public class AddPlantWindow {

    TextField nameInput;
    TextField speciesInput;
    Path path;
    Boolean storno = false;

    /**
     * Method for displaying of the window.
     */
    public void display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Přidání květiny");
        window.setWidth(500);
        window.setHeight(300);

        Label nameLabel = new Label("Jméno");
        GridPane.setConstraints(nameLabel, 0,0);
        Label speciesLabel = new Label("Druh");
        GridPane.setConstraints(speciesLabel, 0,1);
        Label fileLabel = new Label("Obrázek");
        GridPane.setConstraints(fileLabel, 0,2);
        Button pathButton = new Button("Cesta");
        GridPane.setConstraints(pathButton, 1,2);
        pathButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(window);
            path = file.toPath();
        });

        nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1,0);
        speciesInput = new TextField();
        GridPane.setConstraints(speciesInput, 1,1);

        Button closeButton = new Button("OK");
        GridPane.setConstraints(closeButton, 0,3);
        Button stornoButton = new Button("Zrušit");
        GridPane.setConstraints(stornoButton, 1,3);
        closeButton.setOnAction(event -> {
            if(nameInput == null || speciesInput == null || path == null) {
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
                speciesInput, closeButton, fileLabel, pathButton,
                stornoButton);
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

    public Path getPath() {
        return path;
    }

    public Boolean getStorno() {
        return storno;
    }
}
