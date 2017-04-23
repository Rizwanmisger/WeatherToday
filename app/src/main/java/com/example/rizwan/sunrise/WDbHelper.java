package com.example.rizwan.sunrise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rizwan on 23/3/16.
 */
public class WDbHelper extends SQLiteOpenHelper{

   static String DBname = "weatherDataBase3";
    String Tablename = "weatherTable";


    public WDbHelper(Context context)
    {
        super(context, DBname, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  weatherTable(id integer primary key autoincrement,date long unique , desc text ,temp double , templow double ,humidity text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS weatherTable");
        onCreate(db);
    // db.execSQL("create table  weatherTable(id integer primary key  ,date text , desc text ,temp double ,templow double ,humidity text)");
    }

    public void insert(SQLiteDatabase db,Weather weather)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",weather.getDate());
        contentValues.put("desc",weather.getDesc());
        contentValues.put("temp",weather.getTemp());
        contentValues.put("templow", weather.getTemplow());
        contentValues.put("humidity",weather.getHumidity());
        db.insertOrThrow(Tablename, null, contentValues);
      //  db.execSQL("insert into " + Tablename + " (date,desc,temp,templow) values(" + weather.getDate() + "," + weather.getDesc() + "," + weather.getTemp()+","+weather.getTemplow()+")");

    }

    public void insertWeather(SQLiteDatabase db,Weather[] data)
    {
        String sel = new Long(data[0].getDate()).toString();
        db.delete(Tablename, "date < ?", new String[]{sel});
        for(int i=0 ; i < data.length ; i++)
        {
            insert(db,data[i]);
        }
    }
    public void emptyTable(SQLiteDatabase db)
    {
        db.delete(Tablename,null,null);
    }
}
