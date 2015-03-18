package com.ola.appathon.food.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.ola.appathon.food.util.ImageUtil;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

/**
 * Created by RRaju on 2/4/2015.
 */
public class InterestListAdapter extends BaseAdapter{
    String[] name;
    Context mContext;
    ParseFile[] image;
    public InterestListAdapter(String[] name,ParseFile image[], Context mContext) {
        this.name = name;
        this.image = image;
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
            list = inflater.inflate(R.layout.row_rest_item, null);
            TextView mName = (TextView) list.findViewById(R.id.interest_name);
            ImageView mImage = (ImageView) list.findViewById(R.id.interest_image);
            mImage.setImageBitmap(ImageUtil.convertParseFileToBitmap(image[position]));
            mName.setText(name[position]);

        } else {
            list = (View) convertView;
        }
        return list;
    }
}
