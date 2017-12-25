package cz.vut.fit.zoo.gui;

import cz.vut.fit.zoo.model.Animal;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Class to create and display window of animal and its properties.
 * Properties can be edited and animal then updated.
 * Animal can be also deleted from this window.
 * This window offers also image rotation for each animal.
 * @author xfouka01, xstast24
 */
public class AnimalDetailsWindow {
    private HutchDetailsWindow parent;
    private Animal animal;

    private TextField name;
    private TextField type;
    private Date fromDate;
    private Date toDate;
    private ImageView imageView;

    /**
     * Constructor
     * @param parent window with table of all animals
     * @param animal model of animal that is displayed and can be edited
     */
    AnimalDetailsWindow(HutchDetailsWindow parent, Animal animal) {
        this.parent = parent;
        this.animal = animal;
    }

    /**
     * Display the window with animal properties.
     * Animal properties can be edited, animal can be deleted, animal image can be rotated.
     */
    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Zvíře");

        // CREATE PROPERTIES panel
        GridPane properties = new GridPane();
        properties.setVgap(5);
        properties.setHgap(10);

        // attribute labels
        Label nameLabel = new Label("Jméno");
        properties.add(nameLabel, 0, 0);
        Label typeLabel = new Label("Druh");
        properties.add(typeLabel, 0, 1);
        Label fromLabel = new Label("Od");
        properties.add(fromLabel, 0, 2);
        Label toLabel = new Label("Do");
        properties.add(toLabel, 0, 3);

        // fields with properties values
        name = new TextField(animal.getName());
        properties.add(name, 1, 0);
        type = new TextField(animal.getSpecies());
        properties.add(type, 1, 1);

        fromDate = (Date)animal.getFrom();
        DatePicker from = new DatePicker(getLocalDate(fromDate));
        from.setOnAction(event -> fromDate = Date.valueOf(from.getValue()));
        properties.add(from, 1, 2);

        toDate = (Date)animal.getTo();
        DatePicker to = new DatePicker(getLocalDate(toDate));
        to.setOnAction(event -> toDate = Date.valueOf(to.getValue()));
        properties.add(to, 1, 3);

        // wrap it to HBox to set proper indentation
        HBox propertiesBox = new HBox();
        propertiesBox.setPadding(new Insets(10, 0, 20, 20));
        propertiesBox.getChildren().addAll(properties);

        // CREATE IMAGE panel
        Pane imageContainer = new Pane();

        // load image and display it
        Image image = new Image(new ByteArrayInputStream(animal.getPhoto()));
        imageView = new ImageView(image);
        imageContainer.getChildren().add(imageView);

        // CREATE IMAGE EDIT-CONTROLS panel
        VBox imageEdits = new VBox();
        imageEdits.setAlignment(Pos.CENTER_LEFT);
        imageContainer.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        imageEdits.setSpacing(10);
        imageEdits.setPadding(new Insets(10, 20, 20, 20));

        Button rotateLeft = new Button("⟲ VLEVO");
        rotateLeft.setOnAction(event -> {
            imageView.setRotate(imageView.getRotate() - 90);
        });

        Button rotateRight = new Button("⟳ VPRAVO");
        rotateRight.setOnAction(event -> {
            imageView.setRotate(imageView.getRotate() + 90);
        });

        imageEdits.getChildren().addAll(rotateLeft, rotateRight);

        // CREATE CONTROL BUTTONS
        HBox controls = new HBox();
        controls.setSpacing(20);
        controls.setAlignment(Pos.BOTTOM_CENTER);
        imageContainer.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        imageEdits.setPadding(new Insets(10, 20, 20, 20));

        Button ok = new Button("OK");
        ok.setOnAction(event -> {
            if (name.getText() == null || type.getText() == null ||
                    fromDate == null || toDate == null ||
                    imageView.getImage() == null) {
                WarningWindow.display("Varování", "Některá pole " +
                        "nejsou vyplněna");
                return;
            }

            updateAnimal();
            parent.updateTable();
            window.close();
        });

        Button cancel = new Button("Zrušit");
        cancel.setOnAction(event -> {
            window.close();
        });

        Button delete = new Button("Smazat");
        delete.setOnAction(event -> {
            Alert alert = new Alert (Alert.AlertType.WARNING, "Opravdu " +
                    "chcete smazat zvíře?", ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Potvrzení");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                parent.databaseService.deleteAnimal(animal);
                parent.updateTable();
                window.close();
            }
        });

        controls.getChildren().addAll(ok, cancel, delete);

        // CREATE WHOLE WINDOW LAYOUT
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(propertiesBox);
        borderPane.setLeft(imageEdits);
        borderPane.setCenter(imageContainer);
        borderPane.setBottom(controls);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);

        // adjust window size to display the whole photo
        window.setWidth(image.getWidth() + 100);
        window.setMinWidth(300);
        window.setHeight(image.getHeight() + 300);
        window.setMinHeight(300);

        window.show();
    }

    /**
     * Loads propertiees from curren rpoperties window and update animal in database with current values.
     */
    private void updateAnimal() {
        parent.databaseService.deleteAnimal(animal);

        animal.setName(name.getText());
        animal.setSpecies(type.getText());
        animal.setFrom(fromDate);
        if (toDate != null) animal.setTo(toDate);

        // rotate photo
        byte[] img = getRotatedImage(imageView.getImage());
        if (img != null) animal.setPhoto(img);

        parent.databaseService.storeAnimal(animal);
    }

    /**
     * Convert java.sql.Date to LocalDate
     * @param date - java.sql.Date (NOT java.util.Date)
     * @return LocalDate with corresponding time value
     */
    private LocalDate getLocalDate(Date date) {
        if (date == null) return null;
        return date.toLocalDate();
    }


    /**
     * Gets rotated image according to properties window
     * @param image - image of animal
     * @return rotated image as user specified
     */
    private byte[] getRotatedImage(Image image) {
        if (image == null) return null;

        int angle = (int)imageView.getRotate() % 360;
        angle += 360; // get positive rotation value
        // if no rotation, return original image
        if (angle <= 1) return getByteArray(image);

        // rotation needed, convert Image to BufferedImage
        BufferedImage rotatedImg = getBufferedImage(image);

        // rotate image as many times as needed
        while (angle > 1) {
            rotatedImg = DrawObjects.rotateImageClockwise90(rotatedImg);
            angle -= 90;
        }

        return getByteArray(rotatedImg);
    }

    /**
     * Method for converting image formats.
     * @param image - image of animal
     * @return Buffered image format
     */
    private BufferedImage getBufferedImage(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }

    private byte[] getByteArray(BufferedImage image) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteStream);
        } catch (Exception e) {
            WarningWindow.display("Varování", "Chyba při rotaci obrázku");
        }

        return byteStream.toByteArray();
    }

    /**
     * Convert image to byte array format - can be saved to database.
     * @param image - image of animal
     * @return image in byte array format
     */
    private byte[] getByteArray(Image image) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(getBufferedImage(image), "png", byteStream);
        } catch (Exception e) {
            WarningWindow.display("Varování", "Chyba při rotaci obrázku");
        }

        return byteStream.toByteArray();
    }
}
