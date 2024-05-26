package org.example.utils;

public class ConverterUtils {

    public static boolean convertToBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0.0;
        }
        // Add more conditions here if needed
        return false;
    }
}
