
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
import com.duzoo.android.datasource.Message;
import java.util.HashMap;
import java.util.List;

/**
 * Created by RRaju on 3/18/2015.
 */
public class MessageListAdapter extends BaseAdapter {

    List<Message> messages;
    Context mContext;
    LayoutInflater inflater;
    HashMap<String, Integer> colorMap = new HashMap<>();


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public MessageListAdapter(Context context, List<Message> messages) {
        mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.messages = messages;
        initColorMap(messages);
    }

    private void initColorMap(List<Message> messages) {
        int count = 0;
        int[] colors = {R.color.name_brown, R.color.name_green, R.color.name_dodger_blue, R.color.name_gold,
                R.color.name_coral, R.color.name_orange, R.color.name_red, R.color.name_lawn_green, R.color.name_cyan};
        for (Message message : messages) {
            if (!message.isSentByMe()) {
                if (!colorMap.containsKey(message.getName()))
                    colorMap.put(message.getName(), colors[count++]);
                if (count == 9)
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
        if (messages.get(position).isSentByMe())
            return 1;
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = messages.get(position);
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
        setText(mContent, message);
        return convertView;
    }

    private void setText(TextView mContent, Message message) {
        if (!message.isSentByMe()) {
            Spannable name = new SpannableString(message.getName());
            name.setSpan(new ForegroundColorSpan(mContent.getResources().getColor(colorMap.get(message.getName()))), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(name);
        } else {
            Spannable name = new SpannableString("Me");
            name.setSpan(new ForegroundColorSpan(R.color.light_black), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(name);
        }
        mContent.append("\n");
        Spannable text = new SpannableString(message.getContent());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mContent.append(text);
    }

}
