package com.example.rizwan.sunrise;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    WeatherAdapter mforecastAdapter;
    String location  = null;
    WDbHelper mdbhelper;
    SQLiteDatabase mdb;
    MyBroadCastReceiver myBroadCastReceiver;
    ListView listView;
    SharedPreferences sharedPreferences ;
    QueryDatabase queryDatabase;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
        queryDatabase=new QueryDatabase();
        mforecastAdapter = new WeatherAdapter(getActivity(),R.layout.list_item,null,new String[] {"date","desc","temp"},new int[]{R.id.date,R.id.desc,R.id.temp},0);
        myBroadCastReceiver = new MyBroadCastReceiver();
        getActivity().registerReceiver(myBroadCastReceiver, new IntentFilter("Weather_Broadcast"));
        Intent intent =new Intent( getActivity(),WeatherIntentService.class);
        getActivity().startService(intent);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
       TextView textView=(TextView)rootview.findViewById(R.id.header);

       String header=sharedPreferences.getString("location", "Delhi");

        textView.setText("Weather Forecast For " + header);
        location=header;


        queryDatabase.execute();
         listView=(ListView)rootview.findViewById(R.id.listView);
       // listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                   listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = mforecastAdapter.getCursor();
                    cursor.moveToPosition(position);
                    int row = 99;
                    row = cursor.getInt(cursor.getColumnIndex("id"));
                    if (row == 99) {
                        Log.v("MAIN ACTIVITY  ", "  LISTVIEW ROW ?");
                    }
                    Log.v("MAIN ACTIVITY  ", "  LISTVIEW ROW not 99");
                    Intent intent = new Intent(getActivity(), Details.class);
                    intent.putExtra("id", row);
                    startActivity(intent);
                }
            });


        return rootview;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {

        }
        return true;
    }





class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
          QueryDatabase queryDatabase=new QueryDatabase();
        queryDatabase.execute();
    }
}

class QueryDatabase extends AsyncTask<Void,Void,Cursor>
{

    @Override
    protected Cursor doInBackground(Void... params) {
        mdbhelper=new WDbHelper(getActivity());
        mdb=mdbhelper.getReadableDatabase();
        int version=mdb.getVersion();
        String ver = " "+version;

        Cursor cursor = mdb.rawQuery("SELECT  rowid _id,* FROM weatherTable order by date" , null);
        return cursor;
    }


    @Override
    protected void onPostExecute(Cursor cursor) {
        if(cursor.getCount()<1)
        {
            Log.v("VEERSIONNNNNNN", "   LISTVIEW CURSOR EMPTY");
        }
        else
            Log.v("MAINACTIVITY  ", "   INSIDE POSTEXECUTE");
        super.onPostExecute(cursor);
        mforecastAdapter.swapCursor(cursor);
        listView.setAdapter(mforecastAdapter);
        Log.v("MAINACTIVITY", "   INSIDE POSTEXECUTE AFTER SWAP");



    }


}


}

