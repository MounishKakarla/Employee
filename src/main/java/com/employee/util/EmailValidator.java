package com.employee.util;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_REGEX =
        "^[A-Za-z0-9+_.-]+@((gmail|hotmail|yahoo)\\.com|company\\.com)$";

    private static final Pattern PATTERN =
        Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public static boolean isValid(String email) {
        return email != null && PATTERN.matcher(email).matches();
    }
}
