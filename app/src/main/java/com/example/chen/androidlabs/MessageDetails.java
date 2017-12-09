package com.example.chen.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        //extra info that passed from previous activity
        Bundle bundle = getIntent().getBundleExtra("bundle");

        MessageFragment mf = new MessageFragment();
        mf.setArguments(bundle);

        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        ft.replace(R.id.emptyFrameLayout, mf );
        ft.commit();
    }
}
