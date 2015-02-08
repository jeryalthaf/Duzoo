package com.ola.appathon.food.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ola.appathon.food.R;
import com.parse.ParseUser;


public class SettingsFragment extends Fragment {

    CheckBox mNoteEnabled;
    TextView mLogout;
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteEnabled = (CheckBox) view.findViewById(R.id.set_not_check);
        boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("nEnabled",true);
        mNoteEnabled.setChecked(isEnabled);

        mLogout = (TextView) view.findViewById(R.id.set_logout);
        mLogout.setClickable(true);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                if(user!=null)
                    ParseUser.logOut();
                Toast.makeText(getActivity(),"User logged out",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                editor.putBoolean("loggedOut",true);
                editor.commit();
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
