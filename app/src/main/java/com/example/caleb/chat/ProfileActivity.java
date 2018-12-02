package com.example.caleb.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/*************************************************************
 *profileActivity
 *
 * populates the screen with items and gets the current user
 * ID to display
 *************************************************************/
public class ProfileActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acrivity);

        //get intent extra the used UID that clicked
        String user = getIntent().getStringExtra("uid");

        //name textView
        textView = findViewById(R.id.name);
        //set name
        textView.setText(user);


    }
}
