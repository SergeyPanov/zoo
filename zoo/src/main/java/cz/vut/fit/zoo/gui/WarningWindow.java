package cz.vut.fit.zoo.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class for pop-up warning window with message for user.
 * @author xfouka01, xstast24
 */
public class WarningWindow {

    /**
     * Method for displaying the window.
     * @param title Title of the window.
     * @param message Message shown in the window.
     */
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(300);

        Label windowMessage = new Label(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(windowMessage, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene warningWindowScene = new Scene(layout);

        window.setScene(warningWindowScene);
        window.showAndWait();
    }

}
