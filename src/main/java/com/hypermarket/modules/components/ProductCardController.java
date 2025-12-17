package com.hypermarket.modules.components;

import com.hypermarket.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.File;

public class ProductCardController {

    @FXML
    private HBox productCard;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productNameCard;
    @FXML
    private Label productIDCard;
    @FXML
    private Label productPriceCard;
    @FXML
    private Label productQuantityCard;
    @FXML
    private Label expDateProduct;

    private Runnable onDeleteAction;

    public void setOnDeleteAction(Runnable action) {
        this.onDeleteAction = action;
    }

    public void setData(Product product) {
        productNameCard.setText(product.getName());
        productIDCard.setText("ID: " + product.getProductID());
        productPriceCard.setText("Price: " + product.getPrice() + " $");
        productQuantityCard.setText("Quantity: " + product.getQuantity());

        productImage.setPreserveRatio(true);
        productImage.setSmooth(true);

        String imgName = product.getImageName();
        File dir = new File(System.getProperty("user.dir") + "/data/ProductImages");

        File imageFile = new File(dir, imgName + ".png");
        if (!imageFile.exists())
            imageFile = new File(dir, imgName + ".jpg");
        if (!imageFile.exists())
            imageFile = new File(dir, imgName + ".jpeg");

        if (imageFile.exists()) {
            productImage.setImage(new Image(imageFile.toURI().toString()));
        } else {
            var stream = getClass().getResourceAsStream("/com/hypermarket/view/images/no_image.png");
            if (stream != null) {
                productImage.setImage(new Image(stream));
            }
        }
    }
}
