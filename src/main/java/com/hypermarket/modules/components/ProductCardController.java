package com.hypermarket.modules.components;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

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
        productIDCard.setText("#" + product.getProductID());
        productNameCard.setWrapText(true);
        productNameCard.setMaxHeight(40);

        if (product.getName().length() > 30) {
            Tooltip productNameTooltip = new Tooltip(product.getName());
            productNameTooltip.setShowDelay(Duration.millis(300));
            Tooltip.install(productNameCard, productNameTooltip);
        }

        String formattedProductPrice = String.format("%.2f", product.getPrice());
        String formattedDiscountedProductPrice = String.format("%.2f", product.getDiscountedPrice());
        if (product.getOffer() != null && product.getOffer().getEndDate().after(new Date())) {
            originalPriceCard.setText("Price Before: " + formattedProductPrice + " $");
            originalPriceCard.setVisible(true);
            originalPriceCard.setManaged(true);

            productPriceCard.setText("Now: " + formattedDiscountedProductPrice + " $");
        } else {
            originalPriceCard.setVisible(false);
            originalPriceCard.setManaged(false);

            productPriceCard.setText("Price: " + formattedProductPrice + " $");
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
