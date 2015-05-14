package com.duzoo.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.UiChangeListener;

/**
 * Created by RRaju on 4/17/2015.
 */
public class SplashFragment extends Fragment {

    private UiChangeListener mUIChangeListener;

    public SplashFragment() {
    }

    public static SplashFragment newInstance() {
        final SplashFragment splashFragment = new SplashFragment();
        return splashFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUIChangeListener.onAppStateChange(DuzooActivity.state.Signup,null);
            }
        },3000);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        if (activity instanceof UiChangeListener) {
            mUIChangeListener = (UiChangeListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Calling activity should implement ChatUIChangeListener");
        }
    }
}
