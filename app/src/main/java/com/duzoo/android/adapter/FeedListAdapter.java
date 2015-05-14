
package com.duzoo.android.adapter;

/**
 * Created by RRaju on 2/7/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Message;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.datasource.Post;
import com.duzoo.android.util.DuzooConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedListAdapter extends BaseAdapter {
    Context mContext;
    List<Post> posts;
    LayoutInflater inflater;
    DataSource db;
    String link;

    public FeedListAdapter(Context context, List<Post> posts) {
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Post post = posts.get(position);
        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.row_feed_list_item, null);
        }
        TextView mName = (TextView) convertView.findViewById(R.id.home_post_name);
        TextView mContent = (TextView) convertView.findViewById(R.id.home_post_content);
        ImageView uVote = (ImageView) convertView.findViewById(R.id.home_upvote);
        ImageView dVote = (ImageView) convertView.findViewById(R.id.home_downvote);
        TextView voteStats = (TextView) convertView.findViewById(R.id.home_post_count);
        CircleImageView mPic = (CircleImageView) convertView.findViewById(R.id.home_post_image);
        ImageView mWhatsApp = (ImageView) convertView.findViewById(R.id.home_share_whatsapp);
        TextView mCommentCount = (TextView) convertView.findViewById(R.id.home_comment_count);

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
                checkAndVote(1, post);
            }
        });
        dVote.setClickable(true);
        dVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(-1, post);
            }
        });

        mWhatsApp.setClickable(true);
        mWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String text = new String(post.getContent() + " .Get the CricZoo app to get more involved ");
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                mContext.startActivity(sendIntent);
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
        Picasso.with(mContext).load(post.getUserImage()).error(R.drawable.user)
                .into(mPic);
        return convertView;
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
                    mContext.startActivity(openBrowser);
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
//            mContent.setMovementMethod(LinkMovementMethod.getInstance());
        } else
            mContent.setText(message);
    }

    private void checkAndVote(int vote, Post post) {
        if (DuzooActivity.isNetworkAvailable()) {
            if (db == null) {
                db = new DataSource(mContext);
                db.open();
            }
            int status = post.getVote();
            if (status == 1 && vote == 1)
                Toast.makeText(mContext, "You have already upvoted this post", Toast.LENGTH_SHORT)
                        .show();
            else if (status == -1 && vote == -1)
                Toast.makeText(mContext, "You have already downvoted this post", Toast.LENGTH_SHORT)
                        .show();
            else {
                db.updatePostStatus(post.getId(), vote);
                if (vote == 1)
                    db.updatePost(post.getId(), post.getUpvotes() + 1, post.getDownvotes(),
                            post.getCommentCount());
                else
                    db.updatePost(post.getId(), post.getUpvotes(), post.getDownvotes() + 1,
                            post.getCommentCount());
                ParseLink.updatePostVote(post.getId(), vote, ParseLink.currentActivity.home);
                notifyDataSetChanged();
            }
        } else
            Toast.makeText(mContext, "Sorry, no internet connection available", Toast.LENGTH_SHORT)
                    .show();
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        posts = db.getAllPosts(DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
        super.notifyDataSetChanged();
    }

    public String getId(int position) {
        return posts.get(position).getId();
    }
}
