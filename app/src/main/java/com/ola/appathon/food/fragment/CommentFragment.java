
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ola.appathon.food.R;
import com.ola.appathon.food.adapter.CommentListAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CommentFragment extends Fragment {

    ListView       mListView;
    static String  postId;
    ProgressDialog pd;
    TextView       empty;
    String[]       content, userId, objectId, userName;
    EditText       new_comment_content;
    ImageView       new_comment_add;

    public static CommentFragment newInstance(String _postId) {
        CommentFragment fragment = new CommentFragment();
        postId = _postId;
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCommentsFromParse();
    }

    private void getCommentsFromParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment").whereEqualTo("postId",
                postId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> comments, ParseException e) {
                int k = comments.size();
                content = new String[k];
                objectId = new String[k];
                userId = new String[k];
                userName = new String[k];
                int pos = 0;
                for (ParseObject post : comments) {
                    content[pos] = new String(post.getString("content"));
                    objectId[pos] = new String(post.getObjectId());
                    userName[pos] = new String(post.getString("userName"));
                    userId[pos++] = new String(post.getString("userId"));
                }
                try {
                    setUpList();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setUpList() throws ParseException {
        CommentListAdapter adapter = new CommentListAdapter(userName, content, getActivity());
        mListView.setAdapter(adapter);
        pd.cancel();
        mListView.setEmptyView(empty);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading..");
        pd.setMessage("Please wait");
        pd.show();
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setEmptyView(empty);
        empty = (TextView) view.findViewById(R.id.empty);
        new_comment_content = (EditText) view.findViewById(R.id.add_comment_box);
        new_comment_add = (ImageView) view.findViewById(R.id.add_comment_submit);
        new_comment_add.setClickable(true);
        new_comment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject comment = new ParseObject("Comment");
                comment.put("content", new_comment_content.getText().toString());
                comment.put("postId", postId);
                comment.put("userId", ParseUser.getCurrentUser().getObjectId());
                comment.put("userName", ParseUser.getCurrentUser().get("name"));
                new_comment_content.setText("");
                try {
                    comment.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Comment not saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
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
