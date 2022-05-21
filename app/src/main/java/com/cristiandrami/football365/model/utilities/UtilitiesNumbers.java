package com.cristiandrami.football365.model.utilities;

/**
 * This class is a class that contains only numbers, it is used to make clean the code
 * It is used to make flexible the code
 * It is a support class for others classes in the application
 * @author Cristian D. Dramisino
 */
public class UtilitiesNumbers {
    private UtilitiesNumbers(){}
    public static final int MATCHES_DAYS = 7 ;
    public static final int MATCHES_PREVIOUS_DAYS = 3;
    public static long DAY_IN_MILLISECONDS= 24 * 60 * 60 * 1000;



    //every 4 hours The news will be updated
    public static final long NEWS_FREQUENCY_UPDATE = 4 * 60 * 60 * 1000;

}
