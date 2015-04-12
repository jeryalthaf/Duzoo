
package com.duzoo.android.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.duzoo.android.R;
import com.duzoo.android.adapter.CommentListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.datasource.Post;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentFragment extends Fragment {

    ListView       mListView;
    ProgressDialog pd;
    DataSource db;
    Post _post;
    TextView       empty;
    String[]       content, objectId, userName;
    EditText       new_comment_content;
    ImageView       new_comment_add;
    View  post;

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
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

        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading..");
        pd.setMessage("Please wait");
        pd.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment").whereEqualTo("postId",
                DuzooPreferenceManager.getKey("post_id"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> comments, ParseException e) {
                int k = comments.size();
                content = new String[k];
                objectId = new String[k];
                userName = new String[k];
                int pos = 0;
                for (ParseObject post : comments) {
                    content[pos] = new String(post.getString("content"));
                    objectId[pos] = new String(post.getObjectId());
                    userName[pos++] = new String(post.getString("userName"));
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
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        post = view.findViewById(R.id.comment_post);

        db = new DataSource(getActivity());
        setUpPostInComment();
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
                comment.put("postId", DuzooPreferenceManager.getKey("post_id"));
                comment.put("userName", DuzooPreferenceManager.getKey("name"));
                comment.put("userImage", DuzooPreferenceManager.getKey("image"));
                new_comment_content.setText("");
                try {
                    comment.save();
                    getCommentsFromParse();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Sorry , an error occurred in saving your comment",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpPostInComment() {

        db.open();
        _post = db.getPost(DuzooPreferenceManager.getKey("post_id"));
        TextView mName = (TextView) post.findViewById(R.id.home_post_name);
        TextView mMenu = (TextView) post.findViewById(R.id.home_post_content);
        ImageView uVote = (ImageView) post.findViewById(R.id.home_upvote);
        ImageView dVote = (ImageView) post.findViewById(R.id.home_downvote);
        TextView voteStats = (TextView) post.findViewById(R.id.home_post_count);
        CircleImageView mPic = (CircleImageView) post.findViewById(R.id.home_post_image);
        Picasso.with(getActivity()).load(_post.getUserImage()).error(R.drawable.user).into(mPic);
        uVote.setClickable(true);
        uVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(1, _post,db);
            }
        });
        dVote.setClickable(true);
        dVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(-1, _post,db);
            }
        });
        mName.setText(_post.getName());
        mMenu.setText(_post.getContent());
        StringBuilder displayedText = new StringBuilder();
        displayedText.append(_post.getUpvotes()).append(" upvotes and ").append(_post.getDownvotes()).append(" downovtes");
        voteStats.setText(displayedText.toString());
    }

    private void checkAndVote(int vote, Post post,DataSource db) {
        int status = db.getPost(post.getId()).getVote();
        if (status == 1)
            Toast.makeText(getActivity(), "You have already upvoted this post", Toast.LENGTH_SHORT).show();
        else if (status == -1)
            Toast.makeText(getActivity(), "You have already downvoted this post", Toast.LENGTH_SHORT).show();
        else {
            db.updatePostStatus(post.getId(), vote);
            if(vote==1)
                db.updatePost(post.getId(),post.getUpvotes()+1,post.getDownvotes());
            else
                db.updatePost(post.getId(),post.getUpvotes(),post.getDownvotes()+1);
            ParseLink.updatePostVote(post.getId(), vote);
            db.close();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpPostInComment();
                }
            }, 500);
        }


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

    @Override
    public void onDestroyView() {
        pd = null;
        content = null;
        userName = null;
        objectId = null;
        mListView = null;
        super.onDestroyView();
    }
}
