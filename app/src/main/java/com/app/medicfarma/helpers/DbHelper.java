package com.app.medicfarma.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "farmacias.db";

    public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InternalControlDB.SQL_Token.CREATE_TABLE_TOKEN);
        db.execSQL(InternalControlDB.SQL_Usuario.CREATE_TABLE_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(InternalControlDB.SQL_Token.DELETE_TABLE_TOKEN);
        System.out.println(InternalControlDB.SQL_Usuario.DELETE_USUARIOS);

        db.execSQL(InternalControlDB.SQL_Token.DELETE_TABLE_TOKEN);
        db.execSQL(InternalControlDB.SQL_Usuario.DELETE_USUARIOS);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
