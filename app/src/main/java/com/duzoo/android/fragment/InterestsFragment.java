
package com.duzoo.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.adapter.InterestListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Interest;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class InterestsFragment extends Fragment {

    private ListView mListView;
    private UiChangeListener mUIChangeListener;

    public static InterestsFragment newInstance() {
        InterestsFragment fragment = new InterestsFragment();
        return fragment;
    }

    public InterestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mListView = null;
            mUIChangeListener = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable
    final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseLink.getMessages();
        mListView = (ListView) view.findViewById(R.id.list);
        setUpList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void setUpList() {

        InterestListAdapter adapter = new InterestListAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DuzooPreferenceManager.putKey(DuzooConstants.KEY_INTEREST_TYPE, position);
                mUIChangeListener.onAppStateChange(DuzooActivity.state.Home, null);

            }
        });
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
