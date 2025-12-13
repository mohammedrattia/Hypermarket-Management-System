package com.hypermarket.modules.components;

import com.hypermarket.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
        // expDateProduct.setText("Expiration Date: " + product.getExpiryDate());
        
        // try {
        //    if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
        //        productImage.setImage(new Image(product.getImagePath()));
        //    }
        // } catch (Exception e) {
        //    System.out.println("Could not load image for product: " + product.getName());
        // }
    }
}
