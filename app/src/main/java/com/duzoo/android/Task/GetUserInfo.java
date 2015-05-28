package com.duzoo.android.Task;

import android.content.Context;
import android.os.AsyncTask;

import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.datasource.User;
import com.duzoo.android.util.DuzooConstants;
import com.facebook.AccessToken;
import com.parse.ParseObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by RRaju on 4/7/2015.
 */
public class GetUserInfo extends AsyncTask<String, Void, String> {

    Context mContext;
    String email;

    public GetUserInfo(Context context,String email) {
        mContext = context;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = "https://graph.facebook.com/v2.3/me?fields=name,picture&access_token=" + AccessToken.getCurrentAccessToken().getToken();
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
            return inputStreamToString(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return full string
        return total.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject user = new JSONObject(s);
            String name = new String(user.getString("name"));
            String id = new String(user.getString("id"));
            String url = new String(user.getJSONObject("picture").getJSONObject("data").getString("url"));
            ParseObject _user = new ParseObject("User");
            _user.put(DuzooConstants.PARSE_USER_NAME,name);
            _user.put(DuzooConstants.PARSE_USER_FACEBOOK_ID,id);
            _user.put(DuzooConstants.PARSE_USER_IMAGE,url);
            _user.put(DuzooConstants.PARSE_USER_EMAIL,email);
            _user.pinInBackground();
            if (_user != null) {
                ParseLink.createUserOnParse(name, id, url,email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
