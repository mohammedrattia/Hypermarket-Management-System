package com.hypermarket.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileManager {
    private static final String BASEPATH;
    static {
        File localData = new File("data");
        if (localData.exists() && localData.isDirectory()) {
            BASEPATH = "data" + File.separator;
        } else {
            String userHome = System.getProperty("user.home");
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows: C:\Users\Name\AppData\Local\HypermarketSystemData\
                BASEPATH = userHome + File.separator + "AppData" + File.separator +
                        "Local" + File.separator + "HypermarketSystemData" + File.separator;
            } else {
                // Linux/Mac: /home/name/.HypermarketSystemData/
                BASEPATH = userHome + File.separator + ".HypermarketSystemData" + File.separator;
            }
        }
        File directory = new File(BASEPATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static final ArrayList<String> IMAGEEXTENSIONS = new ArrayList<>(List.of("*.png", "*.jpg", "*.jpeg"));
    private static final String FILEEXTENSION = ".txt";
    public static final String PRODUCT_IMAGE_PATH = BASEPATH + "ProductImages/";
    public static final String USER_IMAGE_PATH = BASEPATH + "ProfileImages/";
    public static final String RECEIPTS_PATH = BASEPATH + "Receipts/";
    public static final String DELIMETER = ";";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void writeFile(String filename, ObservableList<?> data) {
        validateDataFolders();
        try (FileWriter fileFW = new FileWriter(BASEPATH + filename + FILEEXTENSION);
                BufferedWriter fileBW = new BufferedWriter(fileFW);
                PrintWriter filePW = new PrintWriter(fileBW);) {
            for (Object object : data) {
                filePW.println(object.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!!");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error Happened!!");
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<String> readFile(String filename) {
        validateDataFolders();
        ObservableList<String> records = FXCollections.observableArrayList();
        String line;
        try (FileReader fileFR = new FileReader(BASEPATH + filename + FILEEXTENSION);
                BufferedReader fileBR = new BufferedReader(fileFR);) {
            while ((line = fileBR.readLine()) != null) {
                records.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!!");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error Happened!!");
            System.out.println(e.getMessage());
        }
        return records;
    }

    public static void copyImage(File source, File destination) throws IOException {
        Files.copy(Paths.get(source.getPath()), Paths.get(destination.getPath()), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void validateDataFolders() {
        // The Data Folder for txt files
        File dataFolder = new File(BASEPATH);
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                System.out.println("ERROR: Could not create directory: " + BASEPATH);
                return;
            }
        }
        // The Folder for Users Images
        File productsImagesFolder = new File(PRODUCT_IMAGE_PATH);
        if (!productsImagesFolder.exists()) {
            if (!productsImagesFolder.mkdir()) {
                System.out.println("ERROR: Could not create directory: " + PRODUCT_IMAGE_PATH);
                return;
            }
        }
        // The Folder for Products Images
        File usersImagesFolder = new File(USER_IMAGE_PATH);
        if (!usersImagesFolder.exists()) {
            if (!usersImagesFolder.mkdir()) {
                System.out.println("ERROR: Could not create directory: " + USER_IMAGE_PATH);
                return;
            }
        }
        // The Folder for Receipts
        File receiptsFolder = new File(RECEIPTS_PATH);
        if (!receiptsFolder.exists()) {
            if (!receiptsFolder.mkdir()) {
                System.out.println("ERROR: Could not create directory: " + RECEIPTS_PATH);
                return;
            }
        }
    }
}
