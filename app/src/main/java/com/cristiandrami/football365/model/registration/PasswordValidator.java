package com.cristiandrami.football365.model.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String passwordValidationRegex ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$";
    private static final Pattern passwordPattern = Pattern.compile(passwordValidationRegex);

    public static boolean validatePassword(String password) {
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }
}
