
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.ola.appathon.food.adapter.FoodListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FoodFragment extends Fragment {

    private ListView mListView;
    String[] name, lat, lon, menu, objectId, time;
    ProgressDialog pd;
    String items[];
    TextView empty;

    public static FoodFragment newInstance() {
        FoodFragment fragment = new FoodFragment();
        return fragment;
    }

    public FoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDialog(String hotelName, String menu, int position) {

        final int pos = position;
        process(menu);
        final boolean[] checked = new boolean[items.length];
        for (boolean b : checked)
            b = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(hotelName);
        builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = true;
            }
        });
        builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                placeOrderAndUpdateDb(checked, pos);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });
        builder.create().show();

    }

    private void placeOrderAndUpdateDb(boolean[] checked, int pos) {
        int l = checked.length;
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please wait");
        pd.setMessage("Placing order and booking OLA, We'll reach you in a jiffy");
        pd.show();
        ParseObject hotel = ParseObject.createWithoutData("hotel", objectId[pos]);
        String originalEntry = menu[pos];
        StringBuilder order= new StringBuilder();
        String[] parts = originalEntry.split(";");
        StringBuilder newEntry=new StringBuilder();
        for(int i=0;i<l;i++)
        {
            if(!checked[i])
                newEntry.append(parts[i]+";");
            else
                order.append(parts[i]+";");
        }
        hotel.put("items",newEntry+"");
        hotel.saveInBackground();

        String[] params = {lat[pos],lon[pos],time[pos]};
        new BookOla().execute(params);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.cancel();
                getFromParse();
            }
        },2500);

        ParseObject transaction = ParseObject.create("Transaction");
        transaction.put("hotelName",name[pos]);
        transaction.put("order",order.toString());
        transaction.put("ngoName",PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("name","default"));
        transaction.saveInBackground();
    }

    public class BookOla extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String url = "http://mobile.api.foo.com/v1/booking/create";
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("user_id", "randomUserId");
            httpPost.addHeader("lat", params[0]);
            httpPost.addHeader("lng", params[1]);
            httpPost.addHeader("pickup_time", params[2]);
            try {
                HttpResponse response = client.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String page)
        {

        }
    }

    private void process(String menu) {
        items = menu.split(";");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        empty = (TextView) view.findViewById(R.id.empty);

        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.setMessage("Kindly wait while we fetch you various options...");
        pd.show();
        getFromParse();
    }

    private void getFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("hotel");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> hotels, ParseException e) {
                int k = hotels.size();
                name = new String[k];
                lat = new String[k];
                lon = new String[k];
                menu = new String[k];
                objectId = new String[k];
                time = new String[k];
                int pos = 0;
                for (ParseObject hotel : hotels) {
                    name[pos] = new String(hotel.getString("name"));
                    lat[pos] = new String(hotel.getString("lat"));
                    lon[pos] = new String(hotel.getString("long"));
                    menu[pos] = new String(hotel.getString("items"));
                    objectId[pos] = new String(hotel.getObjectId());
                    time[pos++] = new String(hotel.getString("time"));
                }
                setUpList();
            }
        });
    }

    private void setUpList() {

        FoodListAdapter adapter = new FoodListAdapter(name, generateDistanceArray(lat, lon), menu, getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(name[position], menu[position],position);
            }
        });

        pd.cancel();
        mListView.setEmptyView(empty);
    }

    private String[] generateDistanceArray(String[] lat, String[] lon) {
        String myLat = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("lat", "12.9499049");
        String myLon = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("lon", "77.6430996");
        String dist[] = new String[lat.length];
        Double lat1, lon1;
        lat1 = Double.valueOf(myLat);
        lon1 = Double.valueOf(myLon);
        int l = lat.length;
        for (int i = 0; i < l; i++) {
            Double lat2 = Double.valueOf(lat[i]);
            Double lon2 = Double.valueOf(lon[i]);
            int R = 6371; // km
            double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
            double y = (lat2 - lat1);
            double distance = Math.sqrt(x * x + y * y) * R;
            if (distance > 1)
                dist[i] = new String(String.valueOf(distance).substring(0, 2));
            else
                dist[i] = new String("1");
        }

        return dist;
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
