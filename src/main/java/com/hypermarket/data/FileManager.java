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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileManager {
    private static final String BASEPATH = "data/";
    private static final String FILEEXTENSION = ".txt";
    public static final String IMAGE_PATH = "data/ProfileImages/";
    public static final String DELIMETER = ";";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void writeFile(String filename, ObservableList<?> data) {
        File dataFolder = new File(BASEPATH);
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                System.out.println("ERROR: Could not create directory: " + BASEPATH);
                return;
            }
        }

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
}
