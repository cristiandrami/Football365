package com.cristiandrami.football365.model.utilities;

import com.cristiandrami.football365.model.registration.ValidationUser;

/**
 * This class is a class that contains only Strings, it is used to make clean the code
 * It is used to make flexible the code, here are stored all string used in other classes
 * @author Cristian D. Dramisino
 */

public class UtilitiesStrings {

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



    public static final String INTERNAL_DATABASE_NAME ="football365_internal_database.db" ;
    public static final String CREATE_NEWS_TABLE_INTERNAL_DATABASE ="CREATE TABLE \"news\" ( \"news_string\"\tTEXT, \"insertion_date\"\tNUMERIC, PRIMARY KEY(\"news_string\",\"insertion_date\") )";


    public static final String NEWS_COLUMN_NEWS_NAME_INTERNAL_DATABASE = "news_string";
    public static final String NEWS_COLUMN_DATE_NAME_INTERNAL_DATABASE = "insertion_date";
    public static final String NEWS_TABLE_NAME_INTERNAL_DATABASE = "news";

    //every 8 hours The news will be updated
    public static final long NEWS_FREQUENCY_UPDATE = 28800000;
    public static final String API_JSON_ARTICLES_ARRAY_NAME ="articles" ;
    public static final String API_JSON_SOURCE_OBJECT_NAME = "source";
    public static final String API_JSON_ARTICLE_TITLE_FIELD = "title";
    public static final String API_JSON_ARTICLE_IMAGE_FIELD ="urlToImage";
    public static final String API_JSON_SOURCE_AUTHOR_FIELD = "name";
    public static final String API_JSON_ARTICLE_URL_FIELD = "url";
    public static final String API_ARTICLE_SORTING ="popularity" ;
    public static final String API_JSON_CREATION_ARRAY_FILED = "{\"articles\" : ";
    public static final String API_JSON_CREATION_ARRAY_CLOSING = "}";
}
