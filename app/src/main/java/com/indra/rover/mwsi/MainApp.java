package com.indra.rover.mwsi;

import android.app.Application;

import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;
import com.squareup.otto.Bus;


public class MainApp extends Application {

    public static Bus   bus = new Bus();
    public static String selectedMRU;
    public static boolean isEditMode;
    public static int total_records;
    public static boolean BTCONNECTED =false;
    @Override
    public void onCreate() {
        super.onCreate();
      //  Fabric.with(this, new Crashlytics());

    }




}
