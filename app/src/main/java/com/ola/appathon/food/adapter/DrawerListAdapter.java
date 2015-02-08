
package com.ola.appathon.food.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;

/**
 * Created by RRaju on 2/3/2015.
 */
public class DrawerListAdapter extends BaseAdapter {
    String[] name = { "Home", "Takeaways", "Profile", "Settings" };
    Context  mContext;
    int[] icons ={R.drawable.home,R.drawable.food,R.drawable.profile,R.drawable.settings};

    public DrawerListAdapter(Context mContext) {
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
            list = inflater.inflate(R.layout.row_drawer_layout, null);
            TextView title = (TextView) list.findViewById(R.id.drawer_item);
            ImageView icon = (ImageView) list.findViewById(R.id.drawer_icon);

            icon.setImageResource(icons[position]);
            title.setText(name[position]);

        } else {
            list = (View) convertView;
        }
        return list;
    }
}
