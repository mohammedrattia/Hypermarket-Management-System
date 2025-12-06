package com.hypermarket.entities;

import com.hypermarket.data.FileManager;

public class Batch {

    public Batch(String recordLine) {
        parseString(recordLine);
    }

    @Override
    public String toString() {
        // return this.attribute01 + FileManager.delimeter + this.attribute02 + FileManager.delimeter + FileManager.dateFormat.format(this.attribute03) + FileManager.delimeter + FileManager.dateTimeFormat.format(this.attribute04) + FileManager.delimeter + this.attribute05.toString() + ....;
        // attribute03 type is Date (it has date only and time is set to 00:00:00)
        // attribute04 type is Date (it has both date and time) 
        // attribute05 type is Role (only for user to know his role) 
        return "ُExample";
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.delimeter);
        // Look at the following examples and make the parseString Function
        try {
            // this.attribute01 = values[0]; // Read String
            // this.attribute02 = Integer(values[1]); // Convert String to Int    // attribute02 is int
            // this.attribute03 = FileManager.dateFormat.parse(values[2]); // Read Date only    // attribute03 type is Date (it has date only and time is set to 00:00:00)
            // this.attribute04 = FileManager.dateTimeFormat.parse(values[3]); // Read Date + Time    // attribute04 type is Date (it has both date and time) 
            // this.attribute05 = Role.valueOf(values[4].toUpperCase().trim()); // Convert String to Role (must be UpperCase)    // attribute05 type is Role (only for user to know his role)
        } 
        catch (IllegalArgumentException e) {
            System.err.println("Error Chosing Role: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

}
