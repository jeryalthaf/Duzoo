package com.duzoo.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
import com.duzoo.android.adapter.FeedListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.UIController;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Post;
import com.duzoo.android.util.DuzooConstants;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

/**
 * Created by RRaju on 3/15/2015.
 */
public class FeedFragment extends Fragment {

    ListView mListView;
    TextView empty;
    FloatingActionButton fab;

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

    public static FeedFragment newInstance() {
        FeedFragment feedFragment = new FeedFragment();
        return feedFragment;
    }

    private void setUpList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DataSource db = new DataSource(getActivity());
                db.open();
                List<Post> posts = db.getAllPosts(DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));

                final FeedListAdapter adapter = new FeedListAdapter(getActivity(), posts);
                mListView.setAdapter(adapter);
                mListView.setEmptyView(empty);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String _id = adapter.getId(position);
                        DuzooPreferenceManager.putKey(DuzooConstants.KEY_POST_ID, _id);
                        UIController.getInstance().switchToCommentsFragment();
                    }
                });
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
