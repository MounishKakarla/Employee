package com.employee.util;

public class IdValidator {

    private static final String EMP_ID_REGEX = "EMP[0-9]+";

    public static boolean isValid(String id) {
        return id != null && id.matches(EMP_ID_REGEX);
    }
}
