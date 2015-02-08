
package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ola.appathon.food.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

public class SignUpFragment extends Fragment implements
        LocationListener
{
    static Context mContext;
    EditText                mName, mPass, mPhone, mEmail;
    TextView                mSignUp;
    String                  name, pass, phone, mail, lat="0", lon="0";
    LocationManager locationManager;
    String provider;

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(Context context) {
        SignUpFragment fragment = new SignUpFragment();
        mContext = context;
        return fragment;
    }

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = (EditText) view.findViewById(R.id.signup_name);
        mEmail = (EditText) view.findViewById(R.id.signup_mail);
        mPass = (EditText) view.findViewById(R.id.signup_pass);
        mPhone = (EditText) view.findViewById(R.id.signup_phone);
        mSignUp = (TextView) view.findViewById(R.id.signup);
        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    mEmail.requestFocus();
                    return true;
                }
                else
                    return false;
            }
        });
        mEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    mPass.requestFocus();
                    return true;
                }
                else
                    return false;
            }
        });
        mPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    mPhone.requestFocus();
                    return true;
                }
                else
                    return false;
            }
        });
        mPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mSignUp.performClick();
                    return true;
                }
                else
                    return false;
            }
        });

        initLocation();
        mSignUp.setClickable(true);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString();
                mail = mEmail.getText().toString();
                pass = mPass.getText().toString();
                phone = mPhone.getText().toString();
                signUpUser();
            }
        });
    }

    private void initLocation() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        provider = LocationManager.NETWORK_PROVIDER;

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
          //  else
          //      Toast.makeText(getActivity(), "Location can't be retrieved", Toast.LENGTH_SHORT)
          //              .show();

        } else {
         //   Toast.makeText(getActivity(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUpUser() {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Signing Up ...");
        pd.show();
        ParseUser user = new ParseUser();
        user.setUsername(phone);
        user.setPassword(pass);
        user.put("email", mail);
        user.put("type", "N");
        user.put("lat",lat);
        user.put("lon", lon);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
        new SignUpAtOla().execute();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        editor.putBoolean("isSignedUp",true);
        editor.putString("user", phone);
        editor.putString("pass", pass);
        editor.putString("phone", phone);
        editor.putString("lat",lat);
        editor.putString("lon",lon);
        editor.putString("mail",mail);
        editor.putString("name",name);
        editor.commit();
        Fragment fragment = HomeFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
      //  getActivity().setTitle("Recent Giveaways");
        pd.cancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = new String(location.getLatitude() + "");
        lon = new String(location.getLongitude() + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class SignUpAtOla extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String url = "http://mobile.api.foo.com/v1/user/signup";
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("email", mail);
            httpPost.addHeader("password", pass);
            httpPost.addHeader("name", name);
            httpPost.addHeader("mobile", phone);
            httpPost.addHeader("source", "and-app");
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

}
