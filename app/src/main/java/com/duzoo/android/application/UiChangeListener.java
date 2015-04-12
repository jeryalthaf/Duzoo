package com.duzoo.android.application;

import android.os.Bundle;

import com.duzoo.android.activity.DuzooActivity;

/**
 * Created by RRaju on 3/25/2015.
 */
public interface UiChangeListener {

    public void onAppStateChange(DuzooActivity.State state, Bundle bundle);

    public void onToolbarStateChanged(DuzooActivity.State state, Bundle bundle);

}