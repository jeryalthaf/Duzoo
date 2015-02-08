
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.ola.appathon.food.adapter.HomeListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class HomeFragment extends Fragment {

    ListView mListView;
    String[] hotel,ngo,order;
    int[] via,resIcon;
    ProgressDialog pd;
    TextView empty;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRecentActivityFromParse();
    }

    private void getRecentActivityFromParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("RecentActivity");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> acts, ParseException e) {
                int k = acts.size();
                hotel = new String[k];
                ngo = new String[k];
                via = new int[k];
                order = new String[k];
                resIcon = new int[k];
                int pos = 0;
                for (ParseObject act : acts) {
                    hotel[pos] = new String(act.getString("hotelName"));
                    ngo[pos] = new String(act.getString("ngoName"));
                    if(hotel[pos].startsWith("P"))
                        resIcon[pos] = R.drawable.pizza;
                    else if(hotel[pos].startsWith("K"))
                        resIcon[pos] = R.drawable.kfc;
                    else if(hotel[pos].startsWith("A"))
                        resIcon[pos] = R.drawable.a2b;
                    else if(hotel[pos].startsWith("M"))
                        resIcon[pos] = R.drawable.macd;
                    else if(hotel[pos].startsWith("D"))
                        resIcon[pos] = R.drawable.dominoz;
                    order[pos] = new String(act.getString("food").replaceAll("\\(.+?\\)","").replaceAll(",",", "));
                    via[pos++] = act.getInt("via");
                }
                setUpList();
            }
        });
    }

    private void setUpList() {
        HomeListAdapter adapter = new HomeListAdapter(ngo,order,getActivity(),via,resIcon);
        mListView.setAdapter(adapter);
        pd.cancel();
        mListView.setEmptyView(empty);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading..");
        pd.setMessage("Please wait");
        pd.show();
        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
