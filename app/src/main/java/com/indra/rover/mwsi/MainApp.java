package com.indra.rover.mwsi;

import android.app.Application;

import com.squareup.otto.Bus;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class MainApp extends Application {

    public static Bus   bus = new Bus();
    public static String selectedMRU;
    public static boolean isEditMode;
    public static int total_records;
    @Override
    public void onCreate() {
        super.onCreate();
      //  Fabric.with(this, new Crashlytics());

    }
}
