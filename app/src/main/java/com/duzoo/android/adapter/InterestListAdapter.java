
package com.duzoo.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzoo.android.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by RRaju on 2/4/2015.
 */
public class InterestListAdapter extends BaseAdapter {
    Context      mContext;

    public InterestListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.row_rest_item, null);
        }
        TextView mName = (TextView) convertView.findViewById(R.id.interest_name);
        ImageView mImage = (ImageView) convertView.findViewById(R.id.interest_image);
        return convertView;
    }
}
