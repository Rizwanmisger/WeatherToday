package com.example.rizwan.sunrise;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rizwan on 25/3/16.
 */
public class WeatherAdapter extends SimpleCursorAdapter {
    public WeatherAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        long day = cursor.getLong(cursor.getColumnIndex("date"));
        TextView wDay = (TextView) view.findViewById(R.id.date);
        //adjust the way the time is displayed to make it human-readable
        wDay.setText(getReadableDateString(day));
    }
    private String getReadableDateString(long time) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        String ret = format.format(date).toString();
        String s = (new Long(time)).toString();
        Log.v("ADAPTER  "," SEE THIS  "+ s+ "  "+ ret);
        return ret;
    }
}
