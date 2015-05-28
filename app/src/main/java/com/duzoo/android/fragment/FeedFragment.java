package com.duzoo.android.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.duzoo.android.adapter.FeedListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.UIController;
import com.duzoo.android.util.DuzooConstants;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by RRaju on 3/15/2015.
 */
public class FeedFragment extends Fragment {

    ListView mListView;
    TextView empty;
    FloatingActionButton fab;
    FeedListAdapter mAdapter;
    public static boolean isFavorite = false;

    public FeedFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            fab.setVisibility(View.GONE);
            mListView = null;
            empty = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);
        if (isFavorite)
            empty.setText("No starred posts");
        else
            empty.setText("No recent posts");
        setUpList();
        fab = (FloatingActionButton) view.findViewById(R.id.new_post);
        if (fab.getVisibility() != View.VISIBLE)
            fab.setVisibility(View.VISIBLE);
        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIController.getInstance().switchToNewPostFragment();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    public static FeedFragment newInstance(Boolean isFav) {
        FeedFragment feedFragment = new FeedFragment();
        isFavorite = isFav;
        return feedFragment;
    }

    private void setUpList() {

        ParseQuery query = ParseQuery.getQuery("Post").orderByDescending(DuzooConstants.PARSE_POST_TIMESTAMP).setLimit(40);
        if (isFavorite)
            query.whereEqualTo(DuzooConstants.PARSE_POST_FAVORITE, true);
        query.whereEqualTo(DuzooConstants.PARSE_POST_DELETED,false);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list.size() != 0) {
                    mAdapter = new FeedListAdapter(list,getActivity());
                    mListView.setAdapter(mAdapter);
                    mListView.setEmptyView(empty);
                }
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
