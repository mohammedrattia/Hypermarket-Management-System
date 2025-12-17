package com.hypermarket.modules.components;

import com.hypermarket.entities.Order;
import com.hypermarket.entities.OrderItem;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class ReceiptPrinter {

    private static final String DEST_FOLDER = "receipts/";
    private static final String TEMPLATE_PATH = "src/main/resources/com/hypermarket/view/components/ReceiptTemplate.html";

    // Dimensions (80mm about 226 points)
    private static final float PDF_WIDTH = 226f;
    private static final float PDF_HEIGHT = 2000f;

    public static void printToPDF(Order order) {
        try {
            String htmlContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH)));

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

            // Set size to 80mm
            pdf.setDefaultPageSize(new PageSize(PDF_WIDTH, PDF_HEIGHT));

            HtmlConverter.convertToPdf(htmlContent, pdf, new ConverterProperties());

            System.out.println("Receipt Created: " + destPath);
            openFile(destPath);

        } catch (Exception e) {
            System.err.println("Error printing receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String setupFilePath(int orderId) {
        File directory = new File(DEST_FOLDER);
        if (!directory.exists())
            directory.mkdirs();
        return DEST_FOLDER + "Order_" + orderId + ".pdf";
    }

    private static void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}