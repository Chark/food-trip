package com.food.app.utils;

public final class Strings {

    /**
     * Trim and lowercase the provided string value.
     *
     * @param value string to normalize.
     * @return normalized string.
     */
    public static String normalize(String value) {
        return value.trim().toLowerCase();
    }
}