package com.example.satyakresna.sunshineiak.database;

import android.provider.BaseColumns;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class ForecastContract {
    public static final class ForecastEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "forecast";
        //City detail
        public static final String COLUMN_CITY_NAME = "city_name";
        //Forecast detail
        public static final String COLUMN_EPOCH_TIME = "epoch_time";
        public static final String COLUMN_MAX_TEMP = "max_temperautre";
        public static final String COLUMN_MIN_TEMP = "min_temperautre";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        public static final String COLUMN_WEATHER_DESC = "weather_descriptiom";
        public static final String COLUMN_WIND_SPEED = "windspeed";
        //Aditional info
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
