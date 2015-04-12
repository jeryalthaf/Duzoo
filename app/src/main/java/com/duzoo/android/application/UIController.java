
package com.duzoo.android.application;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.fragment.CommentFragment;
import com.duzoo.android.fragment.FeedFragment;
import com.duzoo.android.fragment.HomeFragment;
import com.duzoo.android.fragment.InterestsFragment;
import com.duzoo.android.fragment.ProfileFragment;
import com.duzoo.android.fragment.MessagesFragment;
import com.duzoo.android.fragment.SettingsFragment;
import com.duzoo.android.fragment.SignUpFragment;

/**
 * Created by RRaju on 3/25/2015.
 */
public class UIController {

    protected DuzooActivity       duzooActivity;
    protected DuzooActivity.State duzooState;
    protected DuzooActivity.State nextExpectedState;

    protected FeedFragment        feedFragment;
    protected HomeFragment        homeFragment;
    protected CommentFragment     commentFragment;
    protected InterestsFragment   interestsFragment;
    protected MessagesFragment messagesFragment;
    protected SignUpFragment      signUpFragment;
    protected SettingsFragment    settingsFragment;
    protected ProfileFragment     profileFragment;
    protected ActionBar           mActionBar;

    protected FragmentManager     mFragmentManager;
    protected Context             mContext;

    public UIController(DuzooActivity duzooActivity) {
        this.duzooActivity = duzooActivity;
    }

    public void onCreate(ActionBar actionBar, FragmentManager fragmentManager) {
        mActionBar = actionBar;
        mFragmentManager = fragmentManager;
        mContext = duzooActivity.getApplicationContext();
    }

    public void onBackPressed() {

    }

    public void onAppStateChanged(DuzooActivity.State state, Bundle bundle) {
        switch (state) {
            case Feed:
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
            case Profile:
                break;
            case Settings:
                break;
            case Messages:
                break;
        }
    }

    private void switchToCommentsFragment() {

        mFragmentManager.beginTransaction().replace(R.id.container, CommentFragment.newInstance())
                .commit();
        mActionBar.setTitle("Comments");
    }

    private void switchToSignupFragment() {
        boolean isSignedUp = DuzooPreferenceManager.getBooleanKey("signed_up");
        if (!isSignedUp) {
            mActionBar.hide();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFragment.newInstance()).commit();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switchToInterestFragment();
                }
            }, 2000);
        }
    }

    private void switchToHomeFragment() {
        mFragmentManager.beginTransaction().replace(R.id.container, HomeFragment.newInstance())
                .commit();
        mActionBar.setTitle("Duzoo");
    }

    private void switchToInterestFragment() {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, InterestsFragment.newInstance()).commit();
        mActionBar.setTitle("Interests");
        if (!mActionBar.isShowing())
            mActionBar.show();
    }
}
