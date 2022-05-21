package com.cristiandrami.football365.model.registration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to validate the registration fields used by User to create a new Account
 * In this class we use:
 * -    validateEmail(String email) that is used to validate the email, the email have to fit the regular expression emailValidationRegex
 * -    validateName(String name) that is used to validate the first and the last name, both haven't to be empty
 * -    validatePassword(String password) that is used to validate the password, the password have to fit the regular expression passwordValidationRegex
 * -    isValidUser(ValidationUser validationUser) that is used to find if all fields are valid
 *
 * Here we use, to compile and to check if strings fit the regular expressions, the static object Pattern
 *
 *
 * @return      ValidationUser, that is an object that contains information about if values are valid or not
 * @see         ValidationUser
 * @author Cristian D. Dramisino
 *
 */

public class SignUpValidator {
    private SignUpValidator(){}


    private static final String emailValidationRegex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    private static final Pattern emailPattern = Pattern.compile(emailValidationRegex);
    private static final String nameValidationRegex = "^[a-zA-Z\\s]*$";
    private static final Pattern namePattern = Pattern.compile(nameValidationRegex);


    public static ValidationUser validateRegistration(RegistrationUser newUser) {
        ValidationUser validationUser = new ValidationUser();

        validationUser.setFirstName(validateName(newUser.getFirstName()));
        validationUser.setLastName(validateName(newUser.getLastName()));
        validationUser.setEmail(validateEmail(newUser.getEmail()));
        validationUser.setPassword(validatePassword(newUser.getPassword()));
        validationUser.setRepeatedPassword(validateRepeatedPassword(newUser.getRepeatedPassword(), newUser.getPassword()));

        isValidUser(validationUser);

        return validationUser;
    }

    private static void isValidUser(ValidationUser validationUser) {
        if (!validationUser.isValidEmail() || !validationUser.isValidFirstName() || !validationUser.isValidLastName()
                || !validationUser.isValidRepeatedPassword() || !validationUser.isValidPassword()) {
            validationUser.setValidUser(false);
        }
    }

    private static boolean validateRepeatedPassword(String repeatedPassword, String password) {
        return password.equals(repeatedPassword);
    }

    private static boolean validatePassword(String password) {
        return PasswordValidator.validatePassword(password);
    }

    private static boolean validateEmail(String email) {
        email=email.trim();
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    private static boolean validateName(String name) {
        if(name.trim().isEmpty()){
            return false;
        }
        Matcher nameMatcher= namePattern.matcher(name);
        return nameMatcher.matches();
    }
}
