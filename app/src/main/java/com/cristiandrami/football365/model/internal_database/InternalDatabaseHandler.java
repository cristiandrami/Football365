package com.cristiandrami.football365.model.internal_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
/**
 * This class is used to implement an internal SQL database, that is used to store on device some useful information
 * On Database creation all SQL tables needed will be created.
 *
 *
 * @see UtilitiesStrings
 * @author Cristian D. Dramisino
 *
 */
public class InternalDatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase internalDB;


    public InternalDatabaseHandler(@Nullable Context context) {
        super(context, UtilitiesStrings.INTERNAL_DATABASE_NAME, null, 1);
    }

    //Is called when we try to access the first time to database
    //In this method we can create the concrete database

    /**
     * This method is called when we try to access the first time to database, the concrete database
     * is created here*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UtilitiesStrings.CREATE_NEWS_TABLE_INTERNAL_DATABASE);
    }

    /**this is called when the database version number is called.
     * if we change the database this function will called and prevent the breaking of devices*/

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /**this is not used because i don't to implement it*/
    }



    /**
     * This method is used to store the football news obtained by an API call, here is passed as
     * parameter an object {@link NewsDatabaseModel}*/
    public boolean insertDailyNews(NewsDatabaseModel newsToInsert) {
        internalDB= this.getWritableDatabase();
        ContentValues valuesToStore= new ContentValues();

        if(newsToInsert!=null) {
            valuesToStore.put(UtilitiesStrings.NEWS_COLUMN_NEWS_NAME_INTERNAL_DATABASE, newsToInsert.getDailyNews());
            valuesToStore.put(UtilitiesStrings.NEWS_COLUMN_DATE_NAME_INTERNAL_DATABASE, newsToInsert.getDate());
        }

        long result=internalDB.insert(UtilitiesStrings.NEWS_TABLE_NAME_INTERNAL_DATABASE,null, valuesToStore);

        internalDB.close();
        return result!=-1;
    }


    /**
     * This method is used to retrieve the football news stored on internal DB, it returns
     * an object {@link NewsDatabaseModel}
     * If the DB is empty is returned an {@link NewsDatabaseModel} with no informations */
    public NewsDatabaseModel getNews() {
        NewsDatabaseModel news= new NewsDatabaseModel();
        String query="SELECT * FROM "+ UtilitiesStrings.NEWS_TABLE_NAME_INTERNAL_DATABASE;
        internalDB= this.getReadableDatabase();
        Cursor databaseCursor= internalDB.rawQuery(query, null);

        if(databaseCursor.moveToFirst()){
            news.setDailyNews(databaseCursor.getString(0));
            news.setDate(databaseCursor.getLong(1));
        }else{
            news.setDailyNews("");
            news.setDate(0);
        }

        internalDB.close();

        return news;
    }

    /**
     * This method is used to delete the football news stored on internal DB */
    public void deleteNews(){
        internalDB= this.getWritableDatabase();
        internalDB.execSQL("delete from " + UtilitiesStrings.NEWS_TABLE_NAME_INTERNAL_DATABASE);
        internalDB.close();

    }

    public void dropTableNews(){
        internalDB= this.getWritableDatabase();
        internalDB.execSQL("DROP TABLE IF EXISTS "+ UtilitiesStrings.NEWS_TABLE_NAME_INTERNAL_DATABASE);
    }

    public void insertCompetitionsOnDB(){
        internalDB= this.getWritableDatabase();
        internalDB.execSQL(UtilitiesStrings.CREATE_NEWS_TABLE_INTERNAL_DATABASE);
        internalDB.close();

    }

}
