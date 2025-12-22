package com.hypermarket.modules.components;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
    private Label originalPriceCard;
    @FXML
    private Label productQuantityCard;
    @FXML
    private Label expDateProduct;
    @FXML
    private Label CategoryLabel;

    public void setData(Product product) {
        productNameCard.setText(product.getName());
        productIDCard.setText("ID: " + product.getProductID());

        if (product.getOffer() != null) {
            originalPriceCard.setText("Original Price: " + product.getPrice() + " $");
            originalPriceCard.setVisible(true);
            originalPriceCard.setManaged(true);

            productPriceCard.setText("Now: " + String.format("%.2f", product.getDiscountedPrice()) + " $");
        } else {
            originalPriceCard.setVisible(false);
            originalPriceCard.setManaged(false);

            productPriceCard.setText("Price: " + product.getPrice() + " $");
        }

        productQuantityCard.setText("Quantity: " + product.getQuantity());
        CategoryLabel.setText("Category: " + product.getCategory());

        productImage.setPreserveRatio(true);
        productImage.setSmooth(true);

        String imgName = product.getImageName();
        File dir = new File(FileManager.PRODUCT_IMAGE_PATH);

        File imageFile = new File(dir, imgName);

        if (!imageFile.exists())
            imageFile = new File(dir, imgName + ".png");
        if (!imageFile.exists())
            imageFile = new File(dir, imgName + ".jpg");
        if (!imageFile.exists())
            imageFile = new File(dir, imgName + ".jpeg");

        if (imageFile.exists()) {
            try (InputStream stream = new FileInputStream(imageFile)) {
                productImage.setImage(new Image(stream));
            } catch (Exception e) {
                e.printStackTrace();
                loadDefaultImage();
            }
        } else {
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        var stream = getClass().getResourceAsStream("/com/hypermarket/view/images/no_image.png");
        if (stream != null) {
            productImage.setImage(new Image(stream));
        }
    }
}
