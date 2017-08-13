package com.example.satyakresna.sunshineiak;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
