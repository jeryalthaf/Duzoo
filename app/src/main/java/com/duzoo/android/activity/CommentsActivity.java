
package com.duzoo.android.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
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
import com.duzoo.android.datasource.Comment;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.datasource.Post;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends ActionBarActivity {

    ListView mListView;
    ProgressDialog pd;
    DataSource db;
    Post post;
    TextView empty;
    EditText mNewComment;
    ImageView mNewCommentSubmit;
    View mPostView;
    String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataSource(this);
        db.open();
        setContentView(R.layout.activity_comment);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_comments));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Comments");
        mPostView = findViewById(R.id.comment_post);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(empty);
        setUpPostInComment();

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
                ParseLink.createComment(content);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpList();
                    }
                }, 1000);
            }
        });
    }

    private void setUpPostInComment() {

        post = db.getPost(DuzooPreferenceManager.getKey(DuzooConstants.KEY_POST_ID));
        TextView mName = (TextView) mPostView.findViewById(R.id.home_post_name);
        TextView mContent = (TextView) mPostView.findViewById(R.id.home_post_content);
        ImageView uVote = (ImageView) mPostView.findViewById(R.id.home_upvote);
        ImageView dVote = (ImageView) mPostView.findViewById(R.id.home_downvote);
        TextView voteStats = (TextView) mPostView.findViewById(R.id.home_post_count);
        CircleImageView mPic = (CircleImageView) mPostView.findViewById(R.id.home_post_image);
        ImageView mWhatsApp = (ImageView) mPostView.findViewById(R.id.home_share_whatsapp);
        TextView mCommentCount = (TextView) mPostView.findViewById(R.id.home_comment_count);

        int status = post.getVote();
        if (status == 1) {
            uVote.setImageResource(R.drawable.up_inactive);
            dVote.setImageResource(R.drawable.down_active);
        } else if (status == -1) {
            dVote.setImageResource(R.drawable.down_inactive);
            uVote.setImageResource(R.drawable.up_active);
        }

        uVote.setClickable(true);
        uVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(1, post, db);
            }
        });
        dVote.setClickable(true);
        dVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(-1, post, db);
            }
        });

        mWhatsApp.setClickable(true);
        mWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String text = new String(DuzooPreferenceManager
                            .getKey(DuzooConstants.KEY_USER_NAME)
                            + " shared the following post with you : " +
                            post.getContent() + " .Get the CricZoo app to get more involved ");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    MyApplication.getContext().startActivity(sendIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(MyApplication.getContext(), "WhatsApp not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mName.setText(post.getName());
        if (post.getCommentCount() > 1)
            mCommentCount.setText(post.getCommentCount() + " comments");
        else
            mCommentCount.setText(post.getCommentCount() + " comment");
        analyseContentAndSetUrl(mContent, post.getContent());
        int score = post.getUpvotes() - post.getDownvotes();
        voteStats.setText(score + "");
        Picasso.with(UIController.commentsActivity).load(post.getUserImage()).error(R.drawable.user)
                .into(mPic);
        setUpList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

    private void setUpList() {

        List<Comment> comments = db.getCommentByPost(post.getId());
        CommentListAdapter adapter = new CommentListAdapter(comments, this);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(empty);
    }

    private void analyseContentAndSetUrl(TextView mContent, String message) {

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(message);
        if (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            link = new String(urlStr);
        }
        if (link != null) {
            SpannableString ss = new SpannableString(message);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW);
                    openBrowser.setData(Uri.parse(link));
                    MyApplication.getContext().startActivity(openBrowser);
                }
            };
            int start = message.indexOf(link);
            if (start == -1) {
                mContent.setText(message);
                return;
            }
            int end = start + link.length();
            ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(ss);
            mContent.setMovementMethod(LinkMovementMethod.getInstance());
        } else
            mContent.setText(message);
    }

    private void checkAndVote(int vote, Post post, DataSource db) {
        if (DuzooActivity.isNetworkAvailable()) {

            int status = db.getPost(post.getId()).getVote();
            if (vote == 1 && status == 1)
                Toast.makeText(MyApplication.getContext(), "You have already upvoted this post",
                        Toast.LENGTH_SHORT).show();
            else if (vote == -1 & status == -1)
                Toast.makeText(MyApplication.getContext(), "You have already downvoted this post",
                        Toast.LENGTH_SHORT).show();
            else {
                db.updatePostStatus(post.getId(), vote);
                if (vote == 1)
                    db.updatePost(post.getId(), post.getUpvotes() + 1, post.getDownvotes(),
                            post.getCommentCount());
                else
                    db.updatePost(post.getId(), post.getUpvotes(), post.getDownvotes() + 1,
                            post.getCommentCount());
                ParseLink.updatePostVote(post.getId(), vote, ParseLink.currentActivity.comments);
                db.close();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpPostInComment();
                    }
                }, 500);
            }
        } else
            Toast.makeText(MyApplication.getContext(), "Sorry, no internet connection available",
                    Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            pd = null;
            mListView = null;
            db.close();
            mListView = null;
            pd = null;
            post = null;
            empty = null;
            mNewComment = null;
            mNewCommentSubmit = null;
            mPostView = null;
            link = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
