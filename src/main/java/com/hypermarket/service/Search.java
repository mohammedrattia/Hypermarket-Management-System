package com.hypermarket;

import javafx.collections.transformation.FilteredList;

public class Search {
    // TODO: function that takes filteredData and filter it
    // based on newValue of a specific Property
    public static <T> FilteredList<T> addFilter(FilteredList<T> filteredData, String newValue,
            String filterOnProperty) {
        System.out.println("hellooooooo the function is here ");
        System.out.println(((Object) filteredData.get(0)).toString());
        System.out.println(filterOnProperty);
        return filteredData;
    }
}
