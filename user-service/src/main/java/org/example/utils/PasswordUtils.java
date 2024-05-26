package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class PasswordUtils {

    public static String encodeBase64(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String decodeBase64(String password) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(password);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
