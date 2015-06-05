package com.duzoo.android.application;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

/**
 * Created by RRaju on 12/9/2014.
 */
public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);

        Parse.initialize(this, "ZfpnlBPzb2WAG0fSzFrQfZanIviSCjvXxBqOS0ks",
                "FA4o93AAe6bLmeHXlNn5W6vLRqJ5g5FTxFDTz74A");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        FacebookSdk.sdkInitialize(getApplicationContext());
        context = this;
    }

    public static Context getContext() {
        return context;

    }
}
