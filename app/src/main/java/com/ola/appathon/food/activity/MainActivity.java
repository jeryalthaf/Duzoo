
package com.ola.appathon.food.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.ola.appathon.food.fragment.HomeFragment;
import com.ola.appathon.food.fragment.SignUpFragment;
import com.ola.appathon.food.R;

public class MainActivity extends ActionBarActivity {

    private boolean                  isSignedUp;
    public ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        isSignedUp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isSignedUp",false);
        mActionBar = getSupportActionBar();
        // show signup screen if new user , else take to home screen
        if(!isSignedUp) {
            fragmentManager.beginTransaction().replace(R.id.container, SignUpFragment.newInstance(getApplicationContext(),mActionBar)).commit();
        }else {
            getSupportActionBar().setTitle("Duzoo");
            fragmentManager.beginTransaction().replace(R.id.container, HomeFragment.newInstance(mActionBar)).commit();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

}
