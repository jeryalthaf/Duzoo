
package com.duzoo.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duzoo.android.R;
import com.duzoo.android.Task.GetUserInfo;
import com.duzoo.android.activity.DuzooActivity;
import com.duzoo.android.application.UiChangeListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class SignUpFragment extends Fragment {

    UiChangeListener mUIChangeListener;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String accessToken, userEmail;
    EditText email;
    EditText code;
    TextView submit;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        code = (EditText) view.findViewById(R.id.invite_code);
        email = (EditText) view.findViewById(R.id.email);
        submit = (TextView) view.findViewById(R.id.submit_code);

        loginButton.setVisibility(View.GONE);

        submit.setClickable(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(email.getText().toString().contains("@") && email.getText().toString().contains(".")))
                    Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                else if (!code.getText().toString().contentEquals("1404"))
                    Toast.makeText(getActivity(), "Sorry , invalid invite code", Toast.LENGTH_SHORT).show();
                else {
                    userEmail = email.getText().toString();
                    loginButton.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                    code.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }

            }
        });

        loginButton.setFragment(SignUpFragment.this);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                mUIChangeListener.onAppStateChange(DuzooActivity.state.Interests, null);
                accessToken = loginResult.getAccessToken().toString();
                Log.i("duzoo-access_token", AccessToken.getCurrentAccessToken() + "");
                Log.i("duzoo-user-id", loginResult.getAccessToken().getUserId());
                createUserOnParse();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });
    }

    private void createUserOnParse() {

        GetUserInfo task = new GetUserInfo(getActivity(), userEmail);
        task.execute();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
