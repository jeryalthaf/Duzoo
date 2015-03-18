package com.ola.appathon.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.ola.appathon.food.datasource.DataSource;
import com.ola.appathon.food.datasource.Interest;
import com.ola.appathon.food.util.ImageUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RRaju on 3/18/2015.
 */
public class RoomListAdapter extends BaseAdapter {

    List<Interest> interests;
    Context mContext;
    DataSource db;

    public RoomListAdapter(Context context) {
        mContext = context;
        db = new DataSource(mContext);
        db.open();

        interests = new ArrayList<Interest>();
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
            list = inflater.inflate(R.layout.row_room_list_item, null);
            TextView mName = (TextView) list.findViewById(R.id.room_name);
            ImageView mImage = (ImageView) list.findViewById(R.id.room_item_image);
            TextView mNumber = (TextView) list.findViewById(R.id.room_number);

            mName.setText(interests.get(position).getName());
            mNumber.setText(interests.get(position).getFollowers_count()+" participants");
            mImage.setImageBitmap(ImageUtil.convertStringToBitmap(interests.get(position).getImage()));

        } else {
            list = (View) convertView;
        }
        return list;    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
