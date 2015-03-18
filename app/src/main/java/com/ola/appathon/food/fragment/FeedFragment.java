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
    String[] content,objectId,userName,userId;
    int[] upVotes,downVotes,interestType;
    ProgressDialog pd;
    TextView empty;

    public FeedFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading..");
        pd.setMessage("Please wait while we fetch posts for you");
        pd.show();
        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);

        getPostsFromParse();
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

    private void getPostsFromParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                int k = posts.size();
                content = new String[k];
                objectId = new String[k];
                upVotes = new int[k];
                interestType = new int[k];
                downVotes = new int[k];
                userName = new String[k];
                userId = new String[k];
                int pos = 0;
                for (ParseObject post : posts) {
                    content[pos] = new String(post.getString("content"));
                    objectId[pos] = new String(post.getObjectId());
                    upVotes[pos] =  post.getInt("upvotes");
                    downVotes[pos] = post.getInt("downvotes");
                    interestType[pos] = post.getInt("interestType");
                    userId[pos] = post.getString("userId");
                    userName[pos++] = new String(post.getString("userName"));
                }
                setUpList();
            }
        });
    }

    private void setUpList() {
        FeedListAdapter adapter = new FeedListAdapter(userName,content,getActivity(),upVotes,downVotes);
        mListView.setAdapter(adapter);
        pd.cancel();
        mListView.setEmptyView(empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CommentFragment.newInstance(objectId[position]))
                        .commit();
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
