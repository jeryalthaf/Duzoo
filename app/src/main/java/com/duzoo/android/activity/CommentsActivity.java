
package com.duzoo.android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.adapter.CommentListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class CommentsActivity extends ActionBarActivity {

    ListView mListView;
    TextView empty;
    EditText mNewComment;
    ImageView mNewCommentSubmit;
    CommentListAdapter commentListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_comments));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Comments");

        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(empty);
        setUpList();

        UIController.getInstance().commentsActivity = this;
        empty = (TextView) findViewById(R.id.empty);
        mNewComment = (EditText) findViewById(R.id.add_comment_box);
        mNewCommentSubmit = (ImageView) findViewById(R.id.add_comment_submit);
        mNewCommentSubmit.setClickable(true);
        mNewCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewComment.getText().toString().replaceAll("\\s+", "")
                        .contentEquals("")) {
                    Toast.makeText(MyApplication.getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Util.hideKeyBoard(UIController.commentsActivity);
                String content = new String(mNewComment.getText().toString());
                mNewComment.setText("");
                createComment(content);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpList();
                    }
                }, 1000);
            }
        });
    }

    public void createComment(String content) {


        if (DuzooActivity.isNetworkAvailable()) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Adding comment ...");
            dialog.show();
            final ParseObject comment = new ParseObject("Comment");
            comment.put(DuzooConstants.PARSE_COMMENT_CONTENT, content);
            comment.put(DuzooConstants.PARSE_COMMENT_POST_ID,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID));
            comment.put(DuzooConstants.PARSE_COMMENT_USER_NAME,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
            comment.put(DuzooConstants.PARSE_COMMENT_USER_IMAGE,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_IMAGE));
            comment.put(DuzooConstants.PARSE_COMMENT_TIMESTAMP,
                    System.currentTimeMillis());
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        ParseLink.updatePostComments(DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID));
                }
            });
            comment.pinInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.dismiss();
                    if (e == null) {
                        commentListAdapter.addComment(comment);
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    private void setUpList() {

        ParseQuery query = ParseQuery.getQuery("Comment").whereEqualTo(DuzooConstants.PARSE_COMMENT_POST_ID, DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID)).addAscendingOrder("createdAt");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List comments, ParseException e) {
                commentListAdapter = new CommentListAdapter(comments);
                mListView.setAdapter(commentListAdapter);
                if (comments.size() != 0) {
                    if (fits(mListView))
                        mListView.setStackFromBottom(false);
                    else
                        mListView.setStackFromBottom(true);
                }
            }
        });
    }

    public boolean fits(ListView listView) {
        int last = listView.getLastVisiblePosition();
        if (last == listView.getCount() - 1 && listView.getChildAt(last).getBottom() <= listView.getHeight())
            return true;
        else
            return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //       super.onSaveInstanceState(outState);
    }
}
