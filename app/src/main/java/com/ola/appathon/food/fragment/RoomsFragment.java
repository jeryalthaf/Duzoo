package com.ola.appathon.food.fragment;

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

import com.ola.appathon.food.R;
import com.ola.appathon.food.adapter.FeedListAdapter;
import com.ola.appathon.food.adapter.RoomListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by RRaju on 3/15/2015.
 */
public class RoomsFragment extends Fragment {

    ListView mListView;
    String[] content,objectId,userName,userId;
    int[] upVotes,downVotes,interestType;
    ProgressDialog pd;
    TextView empty;

    public RoomsFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);
        setUpList();
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

    public static RoomsFragment newInstance() {
        RoomsFragment roomsFragment = new RoomsFragment();
        return roomsFragment;
    }



    private void setUpList() {
        RoomListAdapter adapter = new RoomListAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setEmptyView(empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
