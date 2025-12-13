package com.hypermarket.modules.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;

public class AddProductController {

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField categoryField;

    @FXML
    private ImageView productImageView;

    @FXML
    private Button clearBtn;

    @FXML
    private Button AddImageButton;

    private File selectedImageFile;
    private int placeholderID;
    private String imageName;

    @FXML
    public void initialize() {
        imageName = "user_" + String.format("%03d", placeholderID) + ".png";
        clearBtn.setOnAction(event -> {
            clearAllFields();
        });

        AddImageButton.setOnAction(event -> {
            handleImageSelection();
        });
    }


    @FXML
    void clearAllFields() {
        nameField.clear();
        sizeField.clear();
        priceField.clear();
        categoryField.clear();
        descriptionField.clear();
        productImageView.setImage(null);
        nameField.requestFocus();
    }


    @FXML
    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(AddImageButton.getScene().getWindow());

        if (file != null) {
            this.selectedImageFile = file;
            Image newImage = new Image(selectedImageFile.toURI().toString());
            productImageView.setImage(newImage);
        }
    }



    
}
