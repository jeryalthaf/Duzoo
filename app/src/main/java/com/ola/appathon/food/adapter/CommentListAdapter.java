package com.ola.appathon.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;

/**
 * Created by viz on 3/14/2015.
 */
public class CommentListAdapter extends BaseAdapter{
    String[] name,content;
    Context mContext;

    public CommentListAdapter(String[] name,String[] content,Context mContext) {
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
            list = inflater.inflate(R.layout.row_comment_list_item, null);
            TextView mName = (TextView) list.findViewById(R.id.home_comment_name);
            TextView mMenu = (TextView) list.findViewById(R.id.home_comment_content);
            mName.setText(name[position]);
            mMenu.setText(content[position]);
        } else {
            list = (View) convertView;
        }
        return list;    }
}
