package com.hypermarket.service;

import java.lang.reflect.Field;
import java.util.Comparator;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ListManipulation {
    // TODO: function that takes filteredData and filter it
    // based on newValue of a specific Property
    public static <T> void updateFilter(FilteredList<T> filteredData, String newValue,
            String filterOnProperty, Class<T> typeClass) {

        Field field;
        try {
            field = typeClass.getDeclaredField(filterOnProperty);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        filteredData.setPredicate(obj -> {

            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            try {
                Object currentValue = field.get(obj);

                String stringValue = String.valueOf(currentValue);

                return stringValue.toLowerCase().contains(newValue.toLowerCase());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public static <T> void updateSort(SortedList<T> sortedList, boolean ascending,
            String sortOnProperty, Class<T> typeClass) {

        Field field;
        try {
            field = typeClass.getDeclaredField(sortOnProperty);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        Comparator<T> comparator = (o1, o2) -> {
            try {
                Object v1 = field.get(o1);
                Object v2 = field.get(o2);

                if (v1 == null && v2 == null)
                    return 0;
                if (v1 == null)
                    return -1;
                if (v2 == null)
                    return 1;

                if (v1 instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> c1 = (Comparable<Object>) v1;
                    return c1.compareTo(v2);
                }

                return v1.toString().compareTo(v2.toString());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        sortedList.setComparator(comparator);
    }
}
