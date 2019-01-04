package com.app.medicfarma.helpers;

import android.content.Context;
import android.database.Cursor;
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

    //Method to get token
    public String getAuthToken(){
        SQLiteDatabase db = this.getReadableDatabase();
        //definiendo el row
        String[] projection = {
                InternalControlDB.TablaToken._ID,
                InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN,
                InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE,
                InternalControlDB.TablaToken.COLUMN_NAME_EXPIRES_IN,
                InternalControlDB.TablaToken.COLUMN_NAME_REFRESH_TOKEN
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaToken.TABLE_NAME_TOKEN,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();

            String oAuth =
                    c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE))+" "+
                    c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN));

            return  oAuth;
        }else{
            return "";
        }

    }



}
