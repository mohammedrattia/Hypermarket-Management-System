package com.hypermarket.data;

public interface Parser<T> {

    // Takes a String, returns an Object of type T
    T parse(String line);
}
