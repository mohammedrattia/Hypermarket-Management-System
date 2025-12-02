package com.hypermarket;

import javafx.beans.property.*;

public class Person {
    IntegerProperty age;
    StringProperty name;

    public Person(int age, String name) {
        this.age = new SimpleIntegerProperty(this, "age");
        this.name = new SimpleStringProperty(this, "name");
        setAge(age);
        setName(name);
    }

    public int getAge() {
        return age.get();
    }

    public String getName() {
        return name.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
