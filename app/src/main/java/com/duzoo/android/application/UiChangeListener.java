package com.duzoo.android.application;

import android.os.Bundle;
import android.view.Menu;

import com.duzoo.android.activity.DuzooActivity;

/**
 * Created by RRaju on 3/25/2015.
 */
public interface UiChangeListener {

    public void onAppStateChange(DuzooActivity.state state, Bundle bundle);

    public boolean onCreateOptionsMenu(Menu menu);
}