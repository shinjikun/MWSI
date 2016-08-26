package com.indra.rover.mwsi.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by Indra on 8/26/2016.
 */
public  abstract  class ModelDao {

    public DatabaseHelper dbHelper;
    public SQLiteDatabase database;

    public ModelDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        try {

            dbHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
    }

    public abstract void open();
    public abstract void close();


}
