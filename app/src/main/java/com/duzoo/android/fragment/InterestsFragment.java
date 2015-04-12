
package com.duzoo.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.duzoo.android.R;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.adapter.InterestListAdapter;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.UiChangeListener;
import com.parse.ParseException;
import com.parse.ParseUser;

public class InterestsFragment extends Fragment {

    private ListView         mListView;
    TextView                 empty, interestAdd;
    private UiChangeListener mUIChangeListener;

    public static InterestsFragment newInstance() {
        InterestsFragment fragment = new InterestsFragment();
        return fragment;
    }

    public InterestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void saveInterestsOnParse() {

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            String username = DuzooPreferenceManager.getKey("username");
            String password = DuzooPreferenceManager.getKey("password");
            try {
                user = ParseUser.logIn(username, password);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable
    final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);
        // interestAdd = (TextView) view.findViewById(R.id.interest_add);
        // interestAdd.setClickable(true);
        // interestAdd.setOnClickListener(new View.OnClickListener() {
        /*
         * @Override public void onClick(View v) { saveInterestsOnParse(); } });
         */
        setUpList();
    }

    private void setUpList() {

        InterestListAdapter adapter = new InterestListAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*CheckBox c = (CheckBox) view.findViewById(R.id.interest_checked);
                c.toggle();
                if (c.isChecked()) {
                    c.setVisibility(View.VISIBLE);
                    ticked[position] = true;
                }
                else {
                    c.setVisibility(View.GONE);
                    ticked[position] = false;
                }*/
                DuzooPreferenceManager.putKey("interest_type",position);
                mUIChangeListener.onAppStateChange(DuzooActivity.state.Home,null);

            }
        });
        mListView.setEmptyView(empty);
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
