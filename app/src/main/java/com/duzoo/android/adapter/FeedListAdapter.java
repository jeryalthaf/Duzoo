package com.duzoo.android.adapter;

/**
 * Created by RRaju on 2/7/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.datasource.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedListAdapter extends BaseAdapter {
    Context mContext;
    List<Post> posts;
    DataSource db;

    public FeedListAdapter(Context context) {
        mContext = context;
        db = new DataSource(mContext);
        db.open();
        posts = db.getAllPosts();
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
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            list = new View(mContext);
            list = inflater.inflate(R.layout.row_feed_list_item, null);

        } else {
            list = (View) convertView;
        }
        TextView mName = (TextView) list.findViewById(R.id.home_post_name);
        TextView mMenu = (TextView) list.findViewById(R.id.home_post_content);
        ImageView uVote = (ImageView) list.findViewById(R.id.home_upvote);
        ImageView dVote = (ImageView) list.findViewById(R.id.home_downvote);
        TextView voteStats = (TextView) list.findViewById(R.id.home_post_count);
        CircleImageView mPic = (CircleImageView) list.findViewById(R.id.home_post_image);
        uVote.setClickable(true);
        uVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(1, position);
            }
        });
        dVote.setClickable(true);
        dVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndVote(-1, position);
            }
        });
        mName.setText(posts.get(position).getName());
        mMenu.setText(posts.get(position).getContent());
        StringBuilder displayedText = new StringBuilder();
        displayedText.append(posts.get(position).getUpvotes()).append(" upvotes and ").append(posts.get(position).getDownvotes()).append(" downovtes");
        voteStats.setText(displayedText.toString());
        Picasso.with(mContext).load(posts.get(position).getUserImage()).error(R.drawable.user).into(mPic);
        return list;
    }

    private void checkAndVote(int vote, int position) {
        Post post = db.getPost(posts.get(position).getId());
        int status = post.getVote();
        if (status == vote && vote == 1)
            Toast.makeText(mContext, "You have already upvoted this post", Toast.LENGTH_SHORT).show();
        else if (status == vote && vote == -1)
            Toast.makeText(mContext, "You have already downvoted this post", Toast.LENGTH_SHORT).show();
        else {
            db.updatePostStatus(post.getId(), vote);
            if(vote==1)
                db.updatePost(post.getId(),post.getUpvotes()+1,post.getDownvotes());
            else
                db.updatePost(post.getId(),post.getUpvotes(),post.getDownvotes()+1);
            ParseLink.updatePostVote(post.getId(),vote);
            notifyDataSetChanged();
        }


    }

    @Override
    public void notifyDataSetChanged() {
        posts = db.getAllPosts();
        super.notifyDataSetChanged();
    }

    public String getId(int position) {
        return posts.get(position).getId();
    }
}

