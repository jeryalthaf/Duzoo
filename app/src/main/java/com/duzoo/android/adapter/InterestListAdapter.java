
package com.duzoo.android.adapter;

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

import com.duzoo.android.R;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Interest;
import com.duzoo.android.util.ImageUtil;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RRaju on 2/4/2015.
 */
public class InterestListAdapter extends BaseAdapter {
    Context        mContext;
    DataSource     db;
    List<Interest> interests;

    public InterestListAdapter(Context mContext) {
        this.mContext = mContext;
        db = new DataSource(mContext);
        db.open();
        interests = db.getAllInterests();
    }

    @Override
    public int getCount() {
        return interests.size();
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
        } else {
            list = (View) convertView;
        }
        TextView mName = (TextView) list.findViewById(R.id.interest_name);
        ImageView mImage = (ImageView) list.findViewById(R.id.interest_image);
        mImage.setImageBitmap(ImageUtil.convertStringToBitmap(interests.get(position).getImage()));
        mName.setText(interests.get(position).getName());
        return list;
    }
}
