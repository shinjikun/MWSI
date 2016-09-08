package com.indra.rover.mwsi;

import android.app.Application;

import com.squareup.otto.Bus;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Indra on 9/7/2016.
 */
public class MainApp extends Application {

    public static Bus   bus = new Bus();
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

    }
}
