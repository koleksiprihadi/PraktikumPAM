package com.krisnaprihadiyanto.tabbedaplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.krisnaprihadiyanto.tabbedaplication.databse.DatabaseHandler;

public class FavActivity extends AppCompatActivity {
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DatabaseHandler dbcenter;
    public static MainActivity ma;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        dbcenter = new DatabaseHandler(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM fav",null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView)findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();
    }

}
