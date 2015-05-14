
package com.duzoo.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duzoo.android.R;
import com.duzoo.android.application.UIController;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.fragment.FeedFragment;
import com.duzoo.android.fragment.MessagesFragment;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class HomeViewPagerActivity extends ActionBarActivity implements ObservableScrollViewCallbacks,UiChangeListener {

    private View mToolbarView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UIController.getInstance().finish();
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

        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}

            @Override
            public void onPageSelected(int i) {
                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        propagateToolbarState(toolbarIsShown());
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mToolbarView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mToolbarView).cancel();
            ViewHelper.setTranslationY(mToolbarView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = mToolbarView.getHeight();
        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = mToolbarView.getHeight();

        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            if (i == mPager.getCurrentItem()) {
                continue;
            }
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }
            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbarView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbarView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mToolbarView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mToolbarView).cancel();
            ViewPropertyAnimator.animate(mToolbarView).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mToolbarView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mToolbarView).cancel();
            ViewPropertyAnimator.animate(mToolbarView).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }

    @Override
    public void onAppStateChange(DuzooActivity.state state, Bundle bundle) {

    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"Feed", "Messages", "Starred"};

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = null;
            final int pattern = position % 3;
            switch (pattern) {
                case 0: {
                    f = FeedFragment.newInstance();
                    break;
                }
                case 1: {
                    f = MessagesFragment.newInstance();
                    break;
                }
                case 2:
                default: {
                    f = FeedFragment.newInstance();
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
