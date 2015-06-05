
package com.duzoo.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duzoo.android.R;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.datasource.Comment;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by viz on 3/14/2015.
 */
public class CommentListAdapter extends BaseAdapter {
    Context  mContext;
    List<ParseObject> comments;
    LayoutInflater mInflater;
    public CommentListAdapter(List<ParseObject> comments,Activity parentActivity) {

        mContext = parentActivity;
        this.comments = comments;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Comment comment = new Comment(comments.get(position));
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.row_comment,parent, false);

        TextView mName = (TextView) convertView.findViewById(R.id.comment_name);
        TextView mView = (TextView) convertView.findViewById(R.id.comment_content);
        TextView mDate = (TextView) convertView.findViewById(R.id.comment_time);
        CircleImageView mPic = (CircleImageView) convertView.findViewById(R.id.comment_pic);

        Date date = new Date((long) comment.getTimestamp());
        mDate.setText(date.toString().substring(0,date.toString().indexOf("GMT")-4));
        mName.setText(comment.getName());
        mView.setText(comment.getContent());
        Glide.with(mContext).load(comment.getUser_image_url()).placeholder(R.drawable.user).error(R.drawable.user).into(mPic);
        return convertView;
    }

    public void addComment(ParseObject comment) {
        comments.add(comments.size(),comment);
        notifyDataSetChanged();
    }

}
