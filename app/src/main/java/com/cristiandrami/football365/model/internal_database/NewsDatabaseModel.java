package com.cristiandrami.football365.model.internal_database;

/**
 * This class is a bean class to facilities the storing and retrieving of data from the internal datatbase
 * it is a bean class for the News table
 *
 *
 * @see InternalDatabaseHandler
 * @author Cristian D. Dramisino
 *
 */
public class NewsDatabaseModel {
    private String dailyNews;
    private long date;

    public NewsDatabaseModel() {
        /**This is an empty constructor*/
    }

    public String getDailyNews() {
        return dailyNews;
    }

    public void setDailyNews(String dailyNews) {
        this.dailyNews = dailyNews;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
