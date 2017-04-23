package com.example.rizwan.sunrise;

import java.util.Date;

/**
 * Created by rizwan on 23/3/16.
 */
public class Weather {
    int id;
    long date;
    String desc;
    double temp;
    double templow;
    String humidity;

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setTemplow(double templow) {
        this.templow = templow;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemplow() {
        return templow;
    }

    public String getHumidity() {
        return humidity;
    }
}
