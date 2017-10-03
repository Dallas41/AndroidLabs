package com.example.chen.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected static final String FILENAME = "myFile";
    protected static final String EMAILKEY = "myEmailKey";

    protected Button loginButton;
    protected SharedPreferences sharedPref;
    protected String userEmail;
    protected EditText textEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        loginButton=(Button)findViewById(R.id.loginButton);
        textEditor=(EditText)findViewById(R.id.loginText);
        sharedPref = getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        userEmail=sharedPref.getString(EMAILKEY, "email@domain.com");
        textEditor.setText(userEmail);

        loginButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  String x = textEditor.getText().toString();
                                                  SharedPreferences.Editor editor = sharedPref.edit();
                                                  editor.putString(EMAILKEY, x);
                                                  editor.commit();

                                                  Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                                                  startActivity(intent);
                                              }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
