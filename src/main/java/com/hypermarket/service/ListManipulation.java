package com.hypermarket.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ListManipulation {
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

    public static <T> T searchObjectWithID(ObservableList<T> list, String id)
            throws IllegalArgumentException, IllegalAccessException {
        if (list == null || list.isEmpty()) {
            return null;
        }
        System.out.println("my id:" + id);
        T firstItem = list.getFirst();
        Class<?> listClass = firstItem.getClass();
        Field[] fields = listClass.getDeclaredFields();
        Field idField = null;

        for (Field field : fields) {
            System.out.println("the Field: " + field.getName());
            if (field.getName().toLowerCase().trim().contains("id")) {
                idField = field;
                break;
            }
        }

        if (idField == null) {
            return null;
        }
        idField.setAccessible(true);
        System.out.println("id Field: " + idField.getName());
        for (T t : list) {
            System.out.println("the User ID: " + t.toString());
            if (id.equals(String.valueOf(idField.get(t))))
                return t;
        }
        return null;
    }
}
