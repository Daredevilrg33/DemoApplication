package com.example.rohitgupta3.demoapplication;

import android.app.Application;
import android.os.StrictMode;

public class ApplicationController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
