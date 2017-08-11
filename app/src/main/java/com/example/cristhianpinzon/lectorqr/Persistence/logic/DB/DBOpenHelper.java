package com.example.cristhianpinzon.lectorqr.Persistence.logic.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Cristhian Pinzon on 09/08/2017.
 */

public class DBOpenHelper extends SQLiteAssetHelper {
    private static  final String DATABASE_NAME = "bd_usuario.db";
    private static final int DATABASE_VERSION =     1;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
