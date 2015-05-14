package com.duzoo.android.fragment;

import android.app.Activity;
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
import com.duzoo.android.adapter.MessageListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Message;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;

import java.util.List;

/**
 * Created by RRaju on 3/15/2015.
 */
public class MessagesFragment extends Fragment {

    ListView mListView;
    TextView mEmpty;
    EditText messageBox;
    ImageView mMessageSubmit,mEmoji;
    DataSource db;
    MessageListAdapter adapter;
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
                ParseLink.createMessage(content);
                Util.hideKeyBoard(getActivity());
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       setUpList();
                    }
                },500);
            }
        });
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
                    adapter.notifyDataSetChanged();
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db = new DataSource(getActivity());
                db.open();
                List<Message> messageList = db.getAllMessages(DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
                adapter = new MessageListAdapter(getActivity(), messageList);
                mListView.setAdapter(adapter);
                mListView.setEmptyView(mEmpty);
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
