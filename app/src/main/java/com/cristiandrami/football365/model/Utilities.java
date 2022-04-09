package com.cristiandrami.football365.model;

import com.cristiandrami.football365.model.registration.ValidationUser;

/**
 * This class is a class that contains only Strings, it is used to make clean the code
 * It is used to make flexible the code, here are stored all string used in other classes
 * @author Cristian D. Dramisino
 */

public class Utilities {

    public static final String SPLASH_TRANSACTION_LOGO_NAME = "logo_image";
    public static final String SPLASH_TRANSACTION_TEXT_NAME = "welcome_text";
    public static final String OK = "ok";
    public static final String PASSWORD_INVALID = "Password not valid, the password have to:" +
            "\ninclude at least one digit number" +
            "\ninclude at least an upper case alphabet" +
            "\ninclude at least a special character ?=.*[@#$%^&-+=() " +
            "\nbe at least 8 characters and at most 20 characters";
    public static final String EMAIL_NOT_VALID = "This email is not valid..." ;
    public static final String LOGIN_HINT ="Login";
    public static final String SIGN_UP_HINT ="SigUp";
    public static final String LOGIN_FAILED="Authentication failed. Please check your email or password...";




    public static final String FIREBASE_DOCUMENT_FIELD_EMAIL = "email";
    public static final String FIREBASE_DOCUMENT_FIELD_FIRST_NAME = "firstName";
    public static final String FIREBASE_DOCUMENT_FIELD_LAST_NAME = "lastName";
}
