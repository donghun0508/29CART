package com.loopers.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ValidationUtils {

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T> T requireNonNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    public static void requirePositive(Long value, String message) {
        requireNonNull(value, message);

        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T, C extends Collection<T>> C requireNonEmpty(C collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    public static <T> List<T> requireNonEmpty(List<T> list, String message) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return list;
    }

    public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return map;
    }

    public static <T, C extends Collection<T>> C requireMinSize(C collection, int minSize, String message) {
        if (collection == null || collection.size() < minSize) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    public static <T, C extends Collection<T>> C requireSizeRange(C collection, int minSize, int maxSize, String message) {
        if (collection == null || collection.size() < minSize || collection.size() > maxSize) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    public static <T> T[] requireNonEmpty(T[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }
}
