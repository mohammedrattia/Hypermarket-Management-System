package com.hypermarket.modules.components;

import com.hypermarket.entities.*;
// import javafx.beans.Observable;
// import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductPageController {
    @FXML
    private Label productNameLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label offerLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label productID;
    @FXML
    private ImageView productImage;

    public void setProduct(Product product) {
        productNameLabel.setText(product.getName());
        categoryLabel.setText("Category : " + product.getCategory());
        priceLabel.setText("Price : $" + product.getPrice());
        priceLabel.setText("Offer : $" + product.getOffer());
        quantityLabel.setText("Quantity : " + product.getQuantity());
        sizeLabel.setText("Size : " + product.getSize());
        durationLabel.setText("Duration : " + product.getDuration());
        productID.setText("Product ID : " + product.getProductID());
    }
}
