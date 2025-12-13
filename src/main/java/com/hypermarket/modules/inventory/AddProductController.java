package com.hypermarket.modules.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
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

    // @FXML
    // private ImageView productImageView;

    // private String imagePath = null; 

    @FXML
    private Button clearBtn;


    @FXML
    public void initialize() {
        clearBtn.setOnAction(event -> {
            clearAllFields();
        });
    }

    @FXML
    void clearAllFields() {
        nameField.clear();
        sizeField.clear();
        priceField.clear();
        categoryField.clear();
        descriptionField.clear();
        // productImageView.setImage(null);
        
        // imagePath = null;
        
        nameField.requestFocus();
    }
}
