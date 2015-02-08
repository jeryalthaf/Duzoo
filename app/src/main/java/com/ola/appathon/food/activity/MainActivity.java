
package com.ola.appathon.food.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ola.appathon.food.fragment.FoodFragment;
import com.ola.appathon.food.fragment.HomeFragment;
import com.ola.appathon.food.fragment.SettingsFragment;
import com.ola.appathon.food.fragment.SignUpFragment;
import com.ola.appathon.food.fragment.NavigationDrawerFragment;
import com.ola.appathon.food.R;
import com.ola.appathon.food.fragment.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence             mTitle;
    private boolean                  isSignedUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        isSignedUp = sp.getBoolean("isSignedUp", false);
        boolean preventProfile = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()).getBoolean("loggedOut", false);
        if(preventProfile)
            showLoginDialog();
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (!isSignedUp)
                {
                    mTitle = "Sign Up";
                    fragment = SignUpFragment.newInstance(getApplicationContext());
                }
                else {
                    mTitle = "Recent Giveaways";
                    fragment = HomeFragment.newInstance();
                }
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                fragment = FoodFragment.newInstance();
                break;
            case 2:
                if (!preventProfile)
                {
                    mTitle = getString(R.string.title_section2);
                    fragment = ProfileFragment.newInstance();
                }
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                fragment = SettingsFragment.newInstance();
                break;

        }
        if (!(position == 2 && preventProfile)||!(position<2&&!isNetworkAvailable()))
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        else
            Toast.makeText(getApplicationContext(), "Sorry, user logged out", Toast.LENGTH_SHORT)
                    .show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please login to contine");
        builder.setPositiveButton("Login",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean("loggedOut",false);
                editor.commit();
                String user = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user","9999");
                String pass = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("pass","9999");
                ParseUser.logInInBackground(user,pass);
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
