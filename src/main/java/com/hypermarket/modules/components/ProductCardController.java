package com.hypermarket.modules.components;

import com.hypermarket.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.File;

public class ProductCardController {

    @FXML private HBox productCard;
    @FXML private ImageView productImage;
    @FXML private Label productNameCard;
    @FXML private Label productIDCard;
    @FXML private Label productPriceCard;
    @FXML private Label productQuantityCard;
    @FXML private Label expDateProduct;
    
    private Runnable onDeleteAction;
    
    public void setOnDeleteAction(Runnable action) {
        this.onDeleteAction = action;
    }

    public void setData(Product product) {
        productNameCard.setText("Name: " + product.getName());
        productIDCard.setText("ID: " + product.getProductID());
        productPriceCard.setText("Price: " + product.getPrice() + " $");
        productQuantityCard.setText("Quantity: " + product.getQuantity());

        String imgName = product.getImageName();
        File file = new File("Data/ProductImages/" + imgName + ".png");

        if (file.exists()) {
            productImage.setImage(new Image(file.toURI().toString()));
        } else {
            try {
                String defaultPath = "/com/hypermarket/view/images/no_image.png";
                productImage.setImage(new Image(getClass().getResourceAsStream(defaultPath)));
            } catch (Exception e) {
                productImage.setImage(null);
            }
        }
    }
}
