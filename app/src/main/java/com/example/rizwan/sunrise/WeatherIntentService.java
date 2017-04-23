package com.example.rizwan.sunrise;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;


public class WeatherIntentService extends IntentService {

    android.os.Handler mhandler ;
    WDbHelper mdbHelper;
    SQLiteDatabase mdb;
    @Override
    public void onCreate() {
        super.onCreate();

    }


    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

       // Log.v("SERVICE  "," DATA INSERTED");
       MyRunnable runnable = new MyRunnable();
        try{
            mhandler = new android.os.Handler();
            mhandler.post(runnable);
        }
        catch(Exception E)
        {

        }

    }
public  void fetchWeather(){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String location = sharedPreferences.getString("location","Delhi,India");
        //create a runnable class and just call the class in this method and cut paste this code into that class
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = "hello";
        String[] result = null;
        try {
            Uri.Builder builder=new Uri.Builder();
            builder.scheme("http");
            builder.authority("api.openweathermap.org");
            builder.appendPath("data");
            builder.appendPath("2.5");
            builder.appendPath("forecast");
            builder.appendPath("daily");
            builder.appendQueryParameter("q", location);
            builder.appendQueryParameter("mode","json");
            builder.appendQueryParameter("units","celsius");
            builder.appendQueryParameter("cnt","7");
            builder.appendQueryParameter("appid","567222879c472fe143e750d50c322d29");
            String myUrl=builder.build().toString();
            URL url=new URL(myUrl);
            // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=London,uk&mode=json&units=metrics&cnt=7&appid=567222879c472fe143e750d50c322d29");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
            Log.v("Fetched Json", forecastJsonStr);
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray("list");
            Weather[] weather=new Weather[10];
            for(int i=0; i < weatherArray.length() ; i++)
            {

                JSONObject dayForecast = weatherArray.getJSONObject(i);
                long dateTime = dayForecast.getLong("dt");
                int humidity = dayForecast.getInt("humidity");
                Log.v("HUMIDITY  "," Hu"+humidity);
                 //String day = getReadableDateString(dateTime);
                JSONObject weatherObject = dayForecast.getJSONArray("weather").getJSONObject(0);
               String description = weatherObject.getString("main");
                JSONObject temperatureObject = dayForecast.getJSONObject("temp");
                double high = temperatureObject.getDouble("max");
                double low = temperatureObject.getDouble("min");
              // day = day.substring(0,3);
                if(weather[i]==null)
                    Log.v("SERVICE  ", "NULL WEATHER");
                Weather weather1 = new Weather();
                weather1.setDate(dateTime);
                weather1.setDesc(description);
                weather1.setTemp(high);
                weather1.setTemplow(low);
                weather1.setHumidity(( new Integer(humidity).toString()));

                weather[i]=weather1;


                Log.v("SERVICE  ", " DATA INSERTED");
            }
            try {
                mdbHelper = new WDbHelper(this);
                mdb = mdbHelper.getWritableDatabase();
                mdbHelper.insertWeather(mdb, weather);
            }
            catch(Exception e)
            {
                Log.v("Exception caught  " ,e.toString());
            }

        } catch (Exception e) {
            Log.e("WeatherIntentService", "Error", e);
            forecastJsonStr = null;
        } finally {
            Log.v("JSon RETURNED", forecastJsonStr);
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainActivityFragment", "Error Closiiiing Stream", e);
                }
            }

        }

    }

    private String getReadableDateString(long time) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

public class MyRunnable implements Runnable
{

    @Override
    public void run() {
       Log.v("SERVICE  RUNNABLE ", " CALL TO RUNNABLE");
        int delay = (1* 60) * 1000;
        fetchWeather();
        sendBroadcast(new Intent("Weather_Broadcast"));
        Log.v("SERVICE RUNNABLE  ", " INSIDE RUNNABLE");
        mhandler.postDelayed(this, delay);
        Log.v("SERVICE HANDLER  ", " HANDLER WORKING FINE");
    }
}



}




