
package com.duzoo.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duzoo.android.R;
import com.duzoo.android.datasource.Comment;

import java.util.List;

/**
 * Created by viz on 3/14/2015.
 */
public class CommentListAdapter extends BaseAdapter {
    Context  mContext;
    List<Comment> comments;
    LayoutInflater mInflater;
    public CommentListAdapter(List<Comment> comments, Context mContext) {

        this.mContext = mContext;
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

        if (convertView == null) {
            convertView = new View(mContext);
            convertView = mInflater.inflate(R.layout.row_comment, null);

        }

        TextView mName = (TextView) convertView.findViewById(R.id.home_comment_name);
        TextView mView = (TextView) convertView.findViewById(R.id.home_comment_content);
        mName.setText(comments.get(position).getName());
        mView.setText(comments.get(position).getContent());
        return convertView;
    }
}
