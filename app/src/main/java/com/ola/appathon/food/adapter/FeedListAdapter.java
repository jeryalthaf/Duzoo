package com.ola.appathon.food.adapter;

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

import com.ola.appathon.food.R;



public class FeedListAdapter extends BaseAdapter {
    String[] name,content;
    Context mContext;
    int upvotes[],downvotes[];

    public FeedListAdapter(String[] name, String[] content, Context mContext, int[] upvotes, int[] downvotes) {
        this.name = name;
        this.content = content;
        this.mContext = mContext;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }



    @Override

    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            list = new View(mContext);
            list = inflater.inflate(R.layout.row_feed_list_item, null);
            TextView mName = (TextView) list.findViewById(R.id.home_post_name);
            TextView mMenu = (TextView) list.findViewById(R.id.home_post_content);
            ImageView uVote = (ImageView) list.findViewById(R.id.home_upvote);
            ImageView dVote = (ImageView) list.findViewById(R.id.home_downvote);
            TextView voteStats = (TextView) list.findViewById(R.id.home_post_count);

            uVote.setClickable(true);
            uVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            dVote.setClickable(true);

            mName.setText(name[position]);
            mMenu.setText(content[position]);
            StringBuilder displayedText = new StringBuilder();
            displayedText.append(upvotes[position]).append(" upvotes and ").append(downvotes[position]).append(" downovtes");
            voteStats.setText(displayedText.toString());
        } else {
            list = (View) convertView;
        }
        return list;    }
}

