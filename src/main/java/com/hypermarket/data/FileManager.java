package com.hypermarket.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileManager {
    private static String basePath = "data/";
    private static String fileExtension = ".txt";
    public static final String DELIMETER = ";";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void writeFile(String filename, ArrayList<?> data) {
        File dataFolder = new File(basePath);
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                System.out.println("ERROR: Could not create directory: " + basePath);
                return;
            }
        }

        try (FileWriter fileFW = new FileWriter(basePath + filename + fileExtension);
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

    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> records = new ArrayList<>();
        String line;
        try (FileReader fileFR = new FileReader(basePath + filename + fileExtension);
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
}
