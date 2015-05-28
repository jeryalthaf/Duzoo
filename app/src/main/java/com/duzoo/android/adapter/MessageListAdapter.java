
package com.duzoo.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duzoo.android.R;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.datasource.Message;
import com.duzoo.android.util.DuzooConstants;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by RRaju on 3/18/2015.
 */
public class MessageListAdapter extends BaseAdapter {

    List<ParseObject> messages;
    Context mContext;
    LayoutInflater inflater;
    HashMap<String, Integer> colorMap = new HashMap<>();


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public MessageListAdapter(List<ParseObject> messages) {
        mContext = MyApplication.getContext();
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.messages = messages;
        initColorMap(messages);
    }

    private void initColorMap(List<ParseObject> messages) {
        int count = 0;
        int[] colors = {R.color.name_brown, R.color.name_dodger_blue, R.color.name_green,
                R.color.name_coral, R.color.name_gold, R.color.name_red};
        for (ParseObject message : messages) {
            if (!message.getBoolean(DuzooConstants.PARSE_MESSAGE_SENT_BY_ME)) {
                if (!colorMap.containsKey(message.getString(DuzooConstants.PARSE_MESSAGE_USER_NAME)))
                    colorMap.put(message.getString(DuzooConstants.PARSE_MESSAGE_USER_NAME), colors[count++]);
                if (count == 6)
                    count = 0;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getString(DuzooConstants.PARSE_MESSAGE_FACEBOOK_ID).contentEquals(DuzooPreferenceManager.getKey(DuzooConstants.KEY_FACEBOOK_ID)))
            return 1;
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ParseObject message = messages.get(position);
        int direction = getItemViewType(position);
        if (convertView == null) {
            int res = 0;
            if (direction == 1)
                res = R.layout.row_message_right;
            else
                res = R.layout.row_message_left;
            convertView = inflater.inflate(res, parent, false);

        }

        TextView mContent = (TextView) convertView.findViewById(R.id.home_message);
        setText(mContent, message, direction);
        return convertView;

    }

    private void setText(TextView mContent, ParseObject message, int direction) {
        if (direction == 0) {
//            Spannable name = new SpannableString(message.getString(DuzooConstants.PARSE_MESSAGE_USER_NAME));
            //           name.setSpan(new ForegroundColorSpan(mContent.getResources().getColor(colorMap.get(message.getString(DuzooConstants.PARSE_MESSAGE_USER_NAME)))), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(message.getString(DuzooConstants.PARSE_MESSAGE_USER_NAME));
        } else {
//            Spannable name = new SpannableString("Me");
            //          name.setSpan(new ForegroundColorSpan(R.color.light_black), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText("Me");
        }
        mContent.append("\n");
        Spannable text = new SpannableString(message.getString(DuzooConstants.PARSE_MESSAGE_CONTENT));
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mContent.append(text);
    }

    public void add(ParseObject message) {
        messages.add(messages.size(), message);
        notifyDataSetChanged();
    }
}
