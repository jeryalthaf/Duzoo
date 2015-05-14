package com.duzoo.android.application;

import android.app.Application;
import android.content.Context;

import com.crittercism.app.Crittercism;
import com.duzoo.android.R;
import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by RRaju on 12/9/2014.
 */
public class MyApplication extends Application {

    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZfpnlBPzb2WAG0fSzFrQfZanIviSCjvXxBqOS0ks",
                "FA4o93AAe6bLmeHXlNn5W6vLRqJ5g5FTxFDTz74A");
        FacebookSdk.sdkInitialize(getApplicationContext());
        Crittercism.initialize(this, "5530a6d07365f84f7d3d6e67");
        context = this;
    }

    public static Context getContext() {
        return context;

    }
}
