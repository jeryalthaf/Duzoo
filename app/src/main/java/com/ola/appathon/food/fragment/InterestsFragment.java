
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.ola.appathon.food.adapter.InterestListAdapter;
import com.ola.appathon.food.datasource.DataSource;
import com.ola.appathon.food.util.ImageUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class InterestsFragment extends Fragment {

    private ListView mListView;
    String[] name, objectId;
    ParseFile[] image;
    int[] type,followers;
    boolean[] ticked;
    ProgressDialog pd;
    TextView empty,interestAdd;
    DataSource db;
    static ActionBar actionBar;
    public static InterestsFragment newInstance(ActionBar actionBar) {
        InterestsFragment fragment = new InterestsFragment();
        InterestsFragment.actionBar = actionBar;
        return fragment;
    }

    public InterestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("Interests");
    }

    private void saveInterestsOnParse() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please wait");
        pd.setMessage("Updating interests, We'll fetch posts for you in a jiffy");
        pd.show();

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String username = prefs.getString("user", "000");
            String password = prefs.getString("pass", "000");
            try {
                user = ParseUser.logIn(username, password);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        StringBuilder followingInterestsTypeArray = new StringBuilder();
        int l = 0;
        db = new DataSource(getActivity());
        db.open();
        for (boolean b : ticked) {
            if (b) {
                followingInterestsTypeArray.append(l + ":");
                db.createInterest(type[l],name[l], ImageUtil.convertParseFileToString(image[l]),followers[l]);
            }
            l++;
        }

        followingInterestsTypeArray.append("EOF");
        if (user != null) {
            user.put("followingInterests", followingInterestsTypeArray.toString());
            user.saveInBackground();
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,HomeFragment.newInstance(actionBar)).commit();
        pd.cancel();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);
        interestAdd = (TextView) view.findViewById(R.id.interest_add);
        interestAdd.setClickable(true);
        interestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInterestsOnParse();
            }
        });
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.setMessage("Kindly wait while we fetch you various interests to follow...");
        pd.show();
        getFromParse();
    }

    private void getFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interest");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> interests, ParseException e) {
                int k = interests.size();
                name = new String[k];
                objectId = new String[k];
                type = new int[k];
                image = new ParseFile[k];
                followers = new int[k];
                ticked = new boolean[k];
                int pos = 0;
                for (ParseObject interest : interests) {
                    name[pos] = new String(interest.getString("interestName"));
                    objectId[pos] = new String(interest.getObjectId());
                    type[pos] = interest.getInt("interestType");
                    followers[pos] = interest.getInt("followersCount");
                    image[pos] = interest.getParseFile("interestImage");
                    ticked[pos++] = false;
                }
                setUpList();
            }
        });
    }

    private void setUpList() {

        InterestListAdapter adapter = new InterestListAdapter(name,image, getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox c = (CheckBox) view.findViewById(R.id.interest_checked);
                c.toggle();
                if(c.isChecked()) {
                    c.setVisibility(View.VISIBLE);
                    ticked[position] = true;
                }
                else {
                    c.setVisibility(View.GONE);
                    ticked[position] = false;
                }
            }
        });

        pd.cancel();
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
