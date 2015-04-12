
package com.duzoo.android.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.duzoo.android.application.UIController;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.R;
import com.duzoo.android.datasource.ParseLink;

public class DuzooActivity extends ActionBarActivity implements UiChangeListener{

    private boolean                  isSignedUp;
    public ActionBar mActionBar;

    private UIController mUiController;

    @Override
    public void onBackPressed() {
        mUiController.onBackPressed();
    }

    public enum state {
        Feed,
        Comments,
        Messages,
        Signup,
        Interests,
        Profile,
        Settings,
        Home,
        NewPost
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUiController = new UIController(this);
        mActionBar = getSupportActionBar();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mUiController.onCreate(mActionBar,fragmentManager);
        mUiController.onAppStateChanged(state.Signup, null);

        if(isNetworkAvailable())
           updateParse();
    }

    private void updateParse() {
        ParseLink parseLink = new ParseLink(getApplicationContext());
        parseLink.getInterests();
        parseLink.getPosts();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAppStateChange(state state, Bundle bundle) {
        mUiController.onAppStateChanged(state,bundle);
    }

    @Override
    public void onToolbarStateChanged(state state, Bundle bundle) {

    }
}
