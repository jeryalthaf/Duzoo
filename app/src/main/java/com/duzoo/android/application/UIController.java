
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
import com.duzoo.android.fragment.NewPostFragment;
import com.duzoo.android.fragment.ProfileFragment;
import com.duzoo.android.fragment.MessagesFragment;
import com.duzoo.android.fragment.SettingsFragment;
import com.duzoo.android.fragment.SignUpFragment;

/**
 * Created by RRaju on 3/25/2015.
 */
public class UIController {

    protected DuzooActivity       duzooActivity;
    protected DuzooActivity.state duzooState;

    protected FeedFragment        feedFragment;
    protected HomeFragment        homeFragment;
    protected CommentFragment     commentFragment;
    protected NewPostFragment     newPostFragment;
    protected InterestsFragment   interestsFragment;
    protected MessagesFragment    messagesFragment;
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

        switch (duzooState) {
            case Signup:
            case Interests:
                break;
            case Home:
                switchToInterestFragment();
                break;
            case Comments:
                if (commentFragment != null)
                    mFragmentManager.beginTransaction().remove(commentFragment);
                switchToHomeFragment();
                break;
            case NewPost:
                switchToHomeFragment();
                break;
            default:

                break;
        }
    }

    public void onAppStateChanged(DuzooActivity.state state, Bundle bundle) {
        switch (state) {
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

    private void switchToCommentsFragment() {

        duzooState = DuzooActivity.state.Comments;
        commentFragment = CommentFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.container, commentFragment)
                .commit();
        mActionBar.setTitle("Comments");
    }

    private void switchToSignupFragment() {

        duzooState = DuzooActivity.state.Signup;
        boolean isSignedUp = DuzooPreferenceManager.getBooleanKey("signed_up");
        if (!isSignedUp) {
            mActionBar.hide();
            signUpFragment = SignUpFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, signUpFragment).commit();
        }
        else {
            switchToInterestFragment();
        }
    }

    private void switchToHomeFragment() {
        duzooState = DuzooActivity.state.Home;
        homeFragment = HomeFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.container, homeFragment)
                .commit();
        mActionBar.setTitle("Duzoo");
    }

    private void switchToInterestFragment() {

        duzooState = DuzooActivity.state.Interests;
        interestsFragment = InterestsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.container, interestsFragment).commit();
        mActionBar.setTitle("Interests");
        if (!mActionBar.isShowing())
            mActionBar.show();
    }

    private void switchToNewPostFragment() {

        duzooState = DuzooActivity.state.NewPost;
        newPostFragment = NewPostFragment.newInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.container, newPostFragment).commit();
        mActionBar.setTitle("New Post");
        if (!mActionBar.isShowing())
            mActionBar.show();
    }
}
