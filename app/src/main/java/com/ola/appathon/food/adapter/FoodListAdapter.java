package com.ola.appathon.food.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ola.appathon.food.R;

/**
 * Created by RRaju on 2/4/2015.
 */
public class FoodListAdapter extends BaseAdapter{
    String[] name,dist,menu;
    int[] imageId;
    Context mContext;

    public FoodListAdapter(String[] name, String[] dist, String[] menu, Context mContext) {
        this.name = name;
        this.dist = dist;
        this.menu = menu;
        this.mContext = mContext;
        setUpImages(name);
    }

    private void setUpImages(String[] name) {
        int l = name.length;
        imageId = new int[l];
        for (int i=0;i<l;i++)
        {
            if(name[i].startsWith("K"))
                this.imageId[i] = R.drawable.kfc;
            else if (name[i].startsWith("D"))
                this.imageId[i] = R.drawable.dominoz;
            else if(name[i].startsWith("Mc"))
                this.imageId[i] = R.drawable.macd;
            else if(name[i].startsWith("A"))
                this.imageId[i] = R.drawable.a2b;
            else if(name[i].startsWith("Ma"))
                this.imageId[i] = R.drawable.mk;
            else if(name[i].startsWith("B"))
                this.imageId[i] = R.drawable.bn;
            else
                this.imageId[i] = R.drawable.pizza;

        }
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
            TextView mName = (TextView) list.findViewById(R.id.rest_name);
            TextView mMenu = (TextView) list.findViewById(R.id.rest_menu);
            ImageView mLogo = (ImageView) list.findViewById(R.id.rest_logo);
            TextView mDist = (TextView) list.findViewById(R.id.rest_dist);
            mName.setText(name[position]);
            mDist.setText(dist[position]+"km");
            mMenu.setText(menu[position].substring(0,menu[position].indexOf("("))+" and more ");
            mLogo.setImageResource(imageId[position]);

        } else {
            list = (View) convertView;
        }
        return list;    }
}
