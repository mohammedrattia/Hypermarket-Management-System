package com.hypermarket;

import javafx.beans.property.*;

public class User {

    private StringProperty firstName;
    private StringProperty lastName;

    public void setFirstName(String value) {
        firstNameProperty().set(value);
    }

    public String getFirstName() {
        return firstNameProperty().get();
    }

    public StringProperty firstNameProperty() {
        if (firstName == null)
            firstName = new SimpleStringProperty(this, "firstName");
        return firstName;
    }

    public void setLastName(String value) {
        lastNameProperty().set(value);
    }

    public String getLastName() {
        return lastNameProperty().get();
    }

    public StringProperty lastNameProperty() {
        if (lastName == null)
            lastName = new SimpleStringProperty(this, "lastName");
        return lastName;
    }

    public User(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }
}
