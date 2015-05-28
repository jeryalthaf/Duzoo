package com.duzoo.android.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.adapter.MessageListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by RRaju on 3/15/2015.
 */
public class MessagesFragment extends Fragment {

    ListView mListView;
    TextView mEmpty;
    EditText messageBox;
    ImageView mMessageSubmit, mEmoji;
    MessageListAdapter mAdapter;
    Handler messageCheckHandler;

    public MessagesFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.message_list);
        mEmpty = (TextView) view.findViewById(R.id.empty);
        setUpList();
        messageBox = (EditText) view.findViewById(R.id.add_message_box);
        messageBox.setCursorVisible(true);
        mEmoji = (ImageView) view.findViewById(R.id.emoji_add);
        mEmoji.setClickable(true);
        mEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mMessageSubmit = (ImageView) view.findViewById(R.id.add_message_submit);
        mMessageSubmit.setClickable(true);
        mMessageSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBox.getText().toString().replaceAll("\\s+", "").contentEquals("")) {
                    Toast.makeText(getActivity(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = new String(messageBox.getText().toString());
                messageBox.setText("");
                createMessage(content);
                Util.hideKeyBoard(getActivity());
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpList();
                    }
                }, 500);
            }
        });
    }

    public void createMessage(String content) {
        if (DuzooActivity.isNetworkAvailable()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Sending your message");
            dialog.show();
            final ParseObject message = new ParseObject("Message");
            message.put(DuzooConstants.PARSE_MESSAGE_CONTENT, content);
            message.put(DuzooConstants.PARSE_MESSAGE_INTEREST_TYPE,
                    DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
            message.put(DuzooConstants.PARSE_MESSAGE_USER_NAME,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
            long timestamp = System.currentTimeMillis();
            message.put(DuzooConstants.PARSE_MESSAGE_TIMESTAMP, timestamp);
            message.put(DuzooConstants.PARSE_MESSAGE_FACEBOOK_ID,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_FACEBOOK_ID));
            message.put(DuzooConstants.PARSE_MESSAGE_SENT_BY_ME, true);
            message.saveInBackground();
            message.pinInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                        mAdapter.add(message);
                    dialog.dismiss();
                }
            });
        }
        else
            Toast.makeText(getActivity(),"Sorry , no internet connection available",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //        runMessageCheckEveryTenSeconds();
    }

    @Override
    public void onStop() {
        super.onStop();
        messageCheckHandler = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            messageBox = null;
            mListView = null;
            mMessageSubmit = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void runMessageCheckEveryTenSeconds() {
        messageCheckHandler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    ParseLink.getMessages();
                    messageCheckHandler.postDelayed(this, 1000 * 10);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                } finally {
                    //also call the same runnable
                }
            }
        };
        messageCheckHandler.postDelayed(runnable, 1000 * 10);
    }

    public static MessagesFragment newInstance() {
        MessagesFragment messagesFragment = new MessagesFragment();
        return messagesFragment;
    }

    private void setUpList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message").orderByAscending(DuzooConstants.PARSE_MESSAGE_TIMESTAMP);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mAdapter = new MessageListAdapter(list);
                mListView.setAdapter(mAdapter);
                int last = mListView.getLastVisiblePosition();
                if (list.size() != 0) {
                    if (last == mListView.getCount() - 1 && mListView.getChildAt(last).getBottom() <= mListView.getHeight())
                        mListView.setStackFromBottom(false);
                    else
                        mListView.setStackFromBottom(true);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
