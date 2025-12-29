package com.employee.util;

import java.util.UUID;

public class PasswordGenerator {

    public static String generate() {
        return "TEMP@" + UUID.randomUUID().toString().substring(0, 5);
    }
}
