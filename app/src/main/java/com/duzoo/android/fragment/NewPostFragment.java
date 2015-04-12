package com.duzoo.android.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.duzoo.android.R;
import com.duzoo.android.adapter.SpinnerAdapter;
import com.duzoo.android.application.UiChangeListener;
import com.duzoo.android.datasource.DataSource;
import com.duzoo.android.datasource.Interest;

import java.util.List;

/**
 * Created by RRaju on 4/12/2015.
 */
public class NewPostFragment extends Fragment {


    EditText new_post_content;
    Spinner new_post_spinner;
    ImageView new_post_add;
    UiChangeListener mUIChangeListener;

    public NewPostFragment() {
    }

    public static NewPostFragment newInstance() {
        final NewPostFragment newPostFragment = new NewPostFragment();
        return newPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_post,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        new_post_content = (EditText) view.findViewById(R.id.new_post_text);
        new_post_add = (ImageView) view.findViewById(R.id.new_post_image);
        new_post_spinner = (Spinner) view.findViewById(R.id.new_post_spinner);
        DataSource db = new DataSource(getActivity());
        db.open();
        List<Interest> interests = db.getAllInterests();
        String[] name = new String[8];
        for(int i=0;i<8;i++)
            name[i] = new String(interests.get(i).getName());
        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),R.layout.spinner_rows,name);
        new_post_spinner.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        if (activity instanceof UiChangeListener) {
            mUIChangeListener = (UiChangeListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Calling activity should implement ChatUIChangeListener");
        }
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
