
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ola.appathon.food.R;

public class HomeFragment extends Fragment {


    private FragmentTabHost mTabHost;
    static ActionBar actionBar;

    public static HomeFragment newInstance(ActionBar actionBar) {
        HomeFragment fragment = new HomeFragment();
        HomeFragment.actionBar = actionBar;
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("Duzoo");
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Handles tabs for feeds and rooms
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.container);

        mTabHost.addTab(mTabHost.newTabSpec("feed").setIndicator("FEED"),
                FeedFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("rooms").setIndicator("ROOMS"),
                RoomsFragment.class, null);

        return mTabHost;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
