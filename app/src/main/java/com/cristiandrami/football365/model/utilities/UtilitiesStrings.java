package com.cristiandrami.football365.model.utilities;

import com.cristiandrami.football365.model.registration.ValidationUser;

/**
 * This class is a class that contains only Strings, it is used to make clean the code
 * It is used to make flexible the code
 * It is a support class for others classes in the application
 * @author Cristian D. Dramisino
 */

public class UtilitiesStrings {

    public static final String SPLASH_TRANSACTION_LOGO_NAME = "logo_image";
    public static final String SPLASH_TRANSACTION_TEXT_NAME = "welcome_text";
    public static final String LOGIN_FAILED="Authentication failed. Please check your email or password...";


    public static final String FIREBASE_DOCUMENT_FIELD_EMAIL = "email";
    public static final String FIREBASE_DOCUMENT_FIELD_FIRST_NAME = "firstName";
    public static final String FIREBASE_DOCUMENT_FIELD_LAST_NAME = "lastName";



    public static final String INTERNAL_DATABASE_NAME ="football365_internal_database.db" ;
    public static final String CREATE_NEWS_TABLE_INTERNAL_DATABASE ="CREATE TABLE \"news\" ( \"news_string\"\tTEXT, \"insertion_date\"\tNUMERIC, PRIMARY KEY(\"news_string\",\"insertion_date\") )";


    public static final String NEWS_COLUMN_NEWS_NAME_INTERNAL_DATABASE = "news_string";
    public static final String NEWS_COLUMN_DATE_NAME_INTERNAL_DATABASE = "insertion_date";
    public static final String NEWS_TABLE_NAME_INTERNAL_DATABASE = "news";

    public static final String NEWS_API_JSON_ARTICLES_ARRAY_NAME ="articles" ;
    public static final String NEWS_API_JSON_SOURCE_OBJECT_NAME = "source";
    public static final String NEWS_API_JSON_ARTICLE_TITLE_FIELD = "title";
    public static final String NEWS_API_JSON_ARTICLE_IMAGE_FIELD ="urlToImage";
    public static final String NEWS_API_JSON_SOURCE_AUTHOR_FIELD = "name";
    public static final String NEWS_API_JSON_ARTICLE_URL_FIELD = "url";
    public static final String NEWS_API_ARTICLE_SORTING ="popularity" ;
    public static final String NEWS_API_JSON_CREATION_ARRAY_FILED = "{\"articles\" : ";
    public static final String NEWS_API_JSON_CREATION_ARRAY_CLOSING = "}";
    public static final String NEWS_API_KEY = "4ba69400af194410b9767bd4f83a013f";


    public static final String COMPETITION_API_URL = "https://api.football-data.org/v2/competitions?plan=TIER_ONE";
    public static final String COMPETITION_API_KEY ="c0c99cafe93949beb14871c37bacfa5f" ;
    public static final String COMPETITION_API_COMPETITIONS_ARRAY_NAME = "competitions";
    public static final String COMPETITION_API_JSON_AREA_OBJECT_NAME = "area";
    public static final String COMPETITION_API_JSON_COMPETITION_NAME_FIELD = "name";
    public static final String COMPETITION_API_JSON_COMPETITION_AREA_NAME_FIELD = "name";
    public static final String COMPETITION_API_JSON_COMPETITION_ID_FIELD = "id";
    public static final String FIREBASE_USERS_COLLECTION_NAME = "users";
}
