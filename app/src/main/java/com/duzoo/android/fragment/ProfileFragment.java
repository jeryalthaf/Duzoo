package com.duzoo.android.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duzoo.android.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    EditText mName,mMail,mPhone;
    String name,mail,phone;
    Button mUpdate;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mName = (EditText) view.findViewById(R.id.set_name_edit);
        mMail = (EditText) view.findViewById(R.id.set_mail_edit);
        mPhone = (EditText) view.findViewById(R.id.set_phone_edit);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mName.setHint(sp.getString("name","Enter name"));
        mMail.setHint(sp.getString("mail","Email"));
        mPhone.setHint(sp.getString("phone","Phone"));

        mUpdate = (Button) view.findViewById(R.id.set_update);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString();
                mail = mMail.getText().toString();
                phone = mPhone.getText().toString();
                if(name.contentEquals(""))
                    name = mName.getHint().toString();
                if(mail.contentEquals(""))
                    mail = mMail.getHint().toString();
                if(phone.contentEquals(""))
                    phone = mPhone.getHint().toString();
                loginAndChangeUserInfo();
            }
        });




    }

    private void loginAndChangeUserInfo() {
        String user = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("user","xx");
        String pass = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("pass","xx");
        ParseUser.logInInBackground(user,pass,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
       //         parseUser.put("email",mail);
         //       parseUser.saveInBackground();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                editor.putString("mail",mail);
                editor.putString("phone",phone);
                editor.putString("name",name);
                editor.commit();
            }
        });
        Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_SHORT).show();
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
