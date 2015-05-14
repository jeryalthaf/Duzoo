
package com.duzoo.android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.R;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;

public class DuzooActivity extends ActionBarActivity implements UiChangeListener {

    public ActionBar mActionBar;
    private static UIController mUiController;

    @Override
    public void onBackPressed() {
    }

    public enum state {
        Splash,
        Home,
        Comments,
        Signup,
        Interests,
        Profile,
        Settings,
        NewPost
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUiController = new UIController(this);
        mActionBar = getSupportActionBar();
        mActionBar.hide();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mUiController.onCreate(fragmentManager);
        mUiController.onAppStateChanged(state.Splash, null);
        if (isNetworkAvailable())
            initDb();
    }

    private void initDb() {
        ParseLink.getPosts();
        ParseLink.getComments();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        else if (id == R.id.action_refresh)
            updateParse();
        else return false;
        return true;
    }

    private static void updateParse() {
        if (mUiController.duzooState == DuzooActivity.state.Home) {
            ParseLink.getPosts();
            if (DuzooPreferenceManager.getBooleanKey(DuzooConstants.KEY_SIGNED_UP))
                ParseLink.getMessages();
        } else if (mUiController.duzooState == DuzooActivity.state.Comments)
            ParseLink.getComments();
    }

    @Override
    public void onAppStateChange(state state, Bundle bundle) {
        mUiController.onAppStateChanged(state, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
