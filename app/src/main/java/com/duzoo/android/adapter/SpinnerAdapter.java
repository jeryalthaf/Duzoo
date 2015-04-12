package com.duzoo.android.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzoo.android.R;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Interest;
import com.duzoo.android.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RRaju on 4/12/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;
    List<Interest> interests = new ArrayList<Interest>();
    Context context;
    /*************  CustomAdapter Constructor *****************/
    public SpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
        super(ctx, txtViewResourceId, objects);
        context = ctx;
        DataSource db = new DataSource(context);
        db.open();
        interests = db.getAllInterests();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup prnt) {
        return getCustomView(position, convertView, prnt);
    }
    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }
    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.spinner_rows, parent,
                false);
        TextView name = (TextView) mySpinner
                .findViewById(R.id.new_post_text);
        name.setText(interests.get(position).getName());

        ImageView left_icon = (ImageView) mySpinner
                .findViewById(R.id.new_post_image);
        left_icon.setImageBitmap(ImageUtil.convertStringToBitmap(interests.get(position).getImage()));

        return mySpinner;
    }
}
