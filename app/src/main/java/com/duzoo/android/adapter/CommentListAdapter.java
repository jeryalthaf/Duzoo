
package com.duzoo.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duzoo.android.R;

/**
 * Created by viz on 3/14/2015.
 */
public class CommentListAdapter extends BaseAdapter {
    String[] name, content;
    Context  mContext;

    public CommentListAdapter(String[] name, String[] content, Context mContext) {
        this.name = name;
        this.content = content;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return content[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            list = new View(mContext);
            list = inflater.inflate(R.layout.row_comment_left, null);

        } else {
            list = (View) convertView;
        }

        TextView mName = (TextView) list.findViewById(R.id.home_comment_name);
        TextView mView = (TextView) list.findViewById(R.id.home_comment_content);
        mName.setText(name[position]);
        mView.setText(content[position]);
        return list;
    }
}
