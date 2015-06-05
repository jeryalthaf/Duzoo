
package com.duzoo.android.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.activity.HomeViewPagerActivity;
import com.duzoo.android.activity.NewPostActivity;
import com.duzoo.android.activity.CommentsActivity;
import com.duzoo.android.fragment.SignUpFragment;
import com.duzoo.android.fragment.SplashFragment;
import com.duzoo.android.util.DuzooConstants;

/**
 * Created by RRaju on 3/25/2015.
 */
public class UIController {

    public static DuzooActivity duzooActivity;
    public static DuzooActivity.state duzooState;
    public static NewPostActivity newPostActivity;
    public static HomeViewPagerActivity homeViewPagerActivity;
    public static UIController uiController;

    public static CommentsActivity commentsActivity;
    protected static SignUpFragment signUpFragment;
    protected static SplashFragment splashFragment;

    protected static FragmentManager mFragmentManager;
    protected static Context mContext;

    public UIController(DuzooActivity duzooActivity) {
        this.duzooActivity = duzooActivity;
    }

    public void onCreate(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mContext = duzooActivity.getApplicationContext();
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    public static void onAppStateChanged(DuzooActivity.state state, Bundle bundle) {
        switch (state) {
            case Splash:
                switchToSplashFragment();
                break;
            case Signup:
                switchToSignupFragment();
                break;
            case Home:
                switchToHomeFragment();
                break;
            case Comments:
                switchToCommentsFragment();
                break;
            case Interests:
                switchToInterestFragment();
                break;
            case NewPost:
                switchToNewPostFragment();
                break;
        }
    }

    public static void switchToSplashFragment() {
        duzooState = DuzooActivity.state.Splash;
        splashFragment = SplashFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.container, splashFragment)
                .commit();
    }

    public static void switchToCommentsFragment() {
        duzooState = DuzooActivity.state.Comments;
        Intent intent = new Intent(homeViewPagerActivity, CommentsActivity.class);
        homeViewPagerActivity.startActivity(intent);
    }

    public static void switchToSignupFragment() {

        duzooState = DuzooActivity.state.Signup;
        boolean isSignedUp = DuzooPreferenceManager.getBooleanKey(DuzooConstants.KEY_SIGNED_UP);
        if (!isSignedUp) {
            signUpFragment = SignUpFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, signUpFragment).commit();
        } else {
            switchToHomeFragment();
        }
    }

    public static void switchToHomeFragment() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                duzooState = DuzooActivity.state.Home;
                Intent intent = new Intent(duzooActivity, HomeViewPagerActivity.class);
                duzooActivity.startActivity(intent);
            }
        }, 4000);

    }

    public static void switchToInterestFragment() {

        /*duzooState = DuzooActivity.state.Interests;
        interestsFragment = InterestsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.container, interestsFragment).commit();
        mActionBar.setTitle("Duzoo");
        if (!mActionBar.isShowing())
            mActionBar.show();
        mActionBar.setDisplayHomeAsUpEnabled(false);*/
        switchToHomeFragment();
    }

    public static void switchToNewPostFragment() {

        duzooState = DuzooActivity.state.NewPost;
        Intent intent = new Intent(homeViewPagerActivity, NewPostActivity.class);
        homeViewPagerActivity.startActivity(intent);

    }

    public static void finish() {

        try {
            duzooActivity.finish();
            duzooState = null;
            uiController = null;

            commentsActivity = null;
            signUpFragment = null;
            splashFragment = null;

            mFragmentManager = null;
            mContext = null;
        } catch (NullPointerException ex) {
        }

    }
}
