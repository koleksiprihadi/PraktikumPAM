package com.krisnaprihadiyanto.tabbedaplication.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "create table fav(no integer primary key AUTOINCREMENT, title text null);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}

