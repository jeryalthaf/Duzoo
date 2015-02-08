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
        Parse.initialize(this, "rw2MviOVGpAsFE5i92XdXcb7LzgrmOkYF7hMpyNu",
                "9YA8NdBPHdyedZQj0QE0F1crfvycylscdg85I41f");
    }
}
