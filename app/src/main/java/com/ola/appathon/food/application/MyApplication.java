package com.ola.appathon.food.application;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by RRaju on 12/9/2014.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "ZfpnlBPzb2WAG0fSzFrQfZanIviSCjvXxBqOS0ks",
                "FA4o93AAe6bLmeHXlNn5W6vLRqJ5g5FTxFDTz74A");
    }
}
