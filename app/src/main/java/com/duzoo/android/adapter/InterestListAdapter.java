
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
    List<String> names  = Arrays.asList("Chennai Superkings", "Mumbai Indians", "Rajasthan Royals",
                                "Kolkata Knight Riders",
                                "Delhi Daredevils", "Sunrisers Hyderabad", "Kings XI Punjab",
                                "Royal Challengers Bangalore");

    int[]        images = { R.drawable.chen, R.drawable.mi, R.drawable.raj, R.drawable.kkr,
            R.drawable.dare, R.drawable.sun, R.drawable.kin, R.drawable.rcb };

    public InterestListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
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
        mImage.setImageResource(images[position]);
        mName.setText(names.get(position));
        return convertView;
    }
}
