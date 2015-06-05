
package com.duzoo.android.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.application.UIController;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.fragment.FeedFragment;
import com.duzoo.android.fragment.MessagesFragment;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

public class HomeViewPagerActivity extends ActionBarActivity implements UiChangeListener {

    private View mToolbarView;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UIController.getInstance().finish();
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //       super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpagertab);
        UIController.getInstance().homeViewPagerActivity = this;

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_home_page));

        mToolbarView = findViewById(R.id.toolbar_home_page);
        ViewCompat.setElevation(mToolbarView, getResources().getDimension(R.dimen.toolbar_elevation));
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.grey));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_invite) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String text = "Hey, I invited you to follow Man U via Duzoo. Use code 1404 to use the app ."
                    + " https://goo.gl/oGcacI";
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.setPackage("com.whatsapp");
            try {
                startActivity(sendIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this,"Whatsapp not installed",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onAppStateChange(DuzooActivity.state state, Bundle bundle) {

    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"Feed", "Messages", "Starred"};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = null;
            final int pattern = position % 3;
            switch (pattern) {
                default:
                case 0: {
                    f = FeedFragment.newInstance(false);
                    break;
                }
                case 1: {
                    f = MessagesFragment.newInstance();
                    break;
                }
                case 2: {
                    f = FeedFragment.newInstance(true);
                }
                break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
