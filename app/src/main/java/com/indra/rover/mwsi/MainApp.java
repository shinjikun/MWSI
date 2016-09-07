package com.indra.rover.mwsi;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by Indra on 9/7/2016.
 */
public class MainApp extends Application {

    public static Bus   bus = new Bus();
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
