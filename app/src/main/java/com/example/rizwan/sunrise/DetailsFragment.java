package com.example.rizwan.sunrise;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    WDbHelper mdbHelper;
    SQLiteDatabase mdb;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int id = -1;
        View rootView= inflater.inflate(R.layout.fragment_details, container, false);
        TextView day=(TextView)rootView.findViewById(R.id.detailDay);
        TextView desc=(TextView)rootView.findViewById(R.id.detaildesc);
        TextView temp = (TextView)rootView.findViewById(R.id.detailMaxTemp);
        TextView lowtemp = (TextView)rootView.findViewById(R.id.detailMinTemp);
        TextView humidity = (TextView) rootView.findViewById(R.id.detailHumidity);
        id=getActivity().getIntent().getIntExtra("id",0);
        if(id == -1)
        {
            desc.setText("message not found");
        }

        else {
            Log.v("DETAIL ACTIVITY  ","ROW IDENTIFIED");
            mdbHelper = new WDbHelper(getActivity());
            mdb = mdbHelper.getReadableDatabase();
            String query = "SELECT * FROM weatherTable WHERE id="+id ;
           // Cursor cursor = mdb.query(mdbHelper.Tablename,null,null,)
            Cursor cursor = mdb.rawQuery(query,null);
            if(cursor == null || cursor.getCount()<1)
            {
                Log.v("DETAIL ACTIVITY  "," CURSOR EMPTY");
            }
            else {
                Log.v("DETAIL ACTIVITY  ", "Query Succesful");
                cursor.moveToFirst();
                long days = cursor.getLong(cursor.getColumnIndex("date"));
                String data = cursor.getString(2);
                day.setText(getReadableDateString(days));
                desc.setText(data);
                temp.setText(cursor.getString(3));
                lowtemp.setText(cursor.getString(4));
                humidity.setText(cursor.getString(5));
                Log.v("DETAIL MESSAGE", data);
            }

        }

        return rootView;
    }
    private String getReadableDateString(long time) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        String ret = format.format(date).toString();
        String s = (new Long(time)).toString();
        //Log.v("ADAPTER  "," SEE THIS  "+ s+ "  "+ ret);
        return ret;
    }
}
