package com.hypermarket.modules.components;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Order;
import com.hypermarket.entities.OrderItem;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class ReceiptPrinter {

    private static final String DEST_FOLDER = FileManager.RECEIPTS_PATH;

    private static final String TEMPLATE_PATH = "/com/hypermarket/view/components/ReceiptTemplate.html";

    // Dimensions (80mm)
    private static final float PDF_WIDTH = 226f;
    private static final float PDF_HEIGHT = 400f;

    public static void printToPDF(Order order) {
        try {
            String htmlContent = loadTemplate();

            String date = order.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String sellerName = (order.getSeller() != null) ? order.getSeller().getFName() : "Unknown";

            StringBuilder itemsHtml = new StringBuilder();
            for (OrderItem item : order.getItems()) {
                String productName = (item.getProduct() != null) ? item.getProduct().getName() : "Unknown";

                itemsHtml.append("<tr>")
                        .append("<td>").append(productName).append("</td>")
                        .append("<td class='qty'>").append(item.getQuantity()).append("</td>")
                        .append("<td class='price'>").append(String.format("%.2f", item.getPriceThatDate()))
                        .append("</td>")
                        .append("<td class='price'>").append(String.format("%.2f", item.getSubTotal())).append("</td>")
                        .append("</tr>");
            }

            htmlContent = htmlContent
                    .replace("{{orderId}}", String.valueOf(order.getOrderID()))
                    .replace("{{date}}", date)
                    .replace("{{sellerName}}", sellerName)
                    .replace("{{itemsRows}}", itemsHtml.toString())
                    .replace("{{totalQty}}", String.valueOf(order.getTotalQuantity()))
                    .replace("{{totalPrice}}", String.format("%.2f", order.getTotalPrice()));

            String destPath = setupFilePath(order.getOrderID());
            PdfWriter writer = new PdfWriter(destPath);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(new PageSize(PDF_WIDTH, PDF_HEIGHT));
            HtmlConverter.convertToPdf(htmlContent, pdf, new ConverterProperties());
            pdf.close();

            System.out.println("Receipt Generated: " + destPath);

            File pdfFile = new File(destPath);
            Platform.runLater(() -> PDFViewerController.showPdfPreview(pdfFile));

        } catch (Exception e) {
            System.err.println("Error printing receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String loadTemplate() throws IOException {
        try (InputStream is = ReceiptPrinter.class.getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                throw new IOException("Template file not found at: " + TEMPLATE_PATH);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String setupFilePath(int orderId) {
        File directory = new File(DEST_FOLDER);
        if (!directory.exists())
            directory.mkdirs();
        return DEST_FOLDER + "Order_" + orderId + ".pdf";
    }
}