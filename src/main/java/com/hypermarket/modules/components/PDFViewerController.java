package com.hypermarket.modules.components;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PDFViewerController {

    @FXML
    private VBox pagesContainer;
    @FXML
    private Button closeButton;
    @FXML
    private Button saveButton;

    private File currentPdfFile;

    @FXML
    public void initialize() {
        closeButton.setOnAction(e -> closeWindow());
        saveButton.setOnAction(e -> saveFile());
    }

    public void loadPDF(File pdfFile) {
        this.currentPdfFile = pdfFile;

        new Thread(() -> {
            try (PDDocument document = PDDocument.load(pdfFile)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int pageCount = document.getNumberOfPages();

                for (int i = 0; i < pageCount; i++) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 150);
                    Image fxImage = SwingFXUtils.toFXImage(bim, null);

                    Platform.runLater(() -> {
                        ImageView imageView = new ImageView(fxImage);
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(450);
                        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
                        pagesContainer.getChildren().add(imageView);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void saveFile() {
        if (currentPdfFile == null || !currentPdfFile.exists())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Receipt PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Receipt_" + System.currentTimeMillis() + ".pdf");

        File dest = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
        if (dest != null) {
            try {
                Files.copy(currentPdfFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved to: " + dest.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public static void showPdfPreview(File pdfFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PDFViewerController.class.getResource("/com/hypermarket/view/components/PDFViewer.fxml"));
            Parent root = loader.load();

            PDFViewerController controller = loader.getController();
            controller.loadPDF(pdfFile);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Receipt Preview");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}