package com.ola.appathon.food.adapter;

/**
 * Created by RRaju on 2/7/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;



public class HomeListAdapter extends BaseAdapter {
    String[] name,menu;
    Context mContext;
    int imageId[];
    int resId[];

    public HomeListAdapter(String[] name,String[] menu,Context mContext,int[] imageId,int resId[]) {
        this.name = name;
        this.menu = menu;
        this.mContext = mContext;
        this.imageId = imageId;
        this.resId = resId;
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
            list = inflater.inflate(R.layout.row_home_list_item, null);
            TextView mName = (TextView) list.findViewById(R.id.home_res_name);
            TextView mMenu = (TextView) list.findViewById(R.id.home_res_items);
            ImageView resIcon = (ImageView) list.findViewById(R.id.home_res_image);
            ImageView olaIcon = (ImageView) list.findViewById(R.id.home_ola_icon);

            int a = imageId[position];
            if(a==0)
                a = R.drawable.o_auto;
            else if(a==1)
                a = R.drawable.o_mini;
            else if(a==2)
                a = R.drawable.o_prime;
            else
                a = R.drawable.o_pink;
            mName.setText(name[position]);
            mMenu.setText(menu[position]);
            olaIcon.setImageResource(a);
            resIcon.setImageResource(resId[position]);

        } else {
            list = (View) convertView;
        }
        return list;    }
}

