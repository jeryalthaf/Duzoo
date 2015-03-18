
package com.ola.appathon.food.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ola.appathon.food.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;



public class SignUpFragment extends Fragment {
    static Context mContext;
    EditText                mName, mPass, mPhone, mEmail;
    TextView                mSignUp;
    String                  name, pass, phone, mail;
    static android.support.v7.app.ActionBar actionBar;
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(Context context, android.support.v7.app.ActionBar actionBar) {
        SignUpFragment fragment = new SignUpFragment();
        mContext = context;
        SignUpFragment.actionBar = actionBar;
        return fragment;
    }

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("Signup");
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


    private void signUpUser() {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Signing Up ...");
        pd.show();
        ParseUser user = new ParseUser();
        user.setUsername(phone);
        user.setPassword(pass);
        user.put("email", mail);
        user.put("name",name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        editor.putBoolean("isSignedUp",true);
        editor.putString("user", phone);
        editor.putString("pass", pass);
        editor.putString("phone", phone);
        editor.putString("mail",mail);
        editor.putString("name",name);
        editor.commit();
        Fragment fragment = InterestsFragment.newInstance(actionBar);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
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




}
