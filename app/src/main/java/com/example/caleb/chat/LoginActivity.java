package com.example.caleb.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**************************************************************
 * handles sending the user to either the create account screen
 * or the login activity screen
 *
 ***************************************************************/
public class LoginActivity extends AppCompatActivity {

    private Button logRegBtn;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button3);
        logRegBtn = findViewById(R.id.logRegBtn);
        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if no account go to create account
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if have account go to login page
                Intent intent = new Intent(LoginActivity.this, RealLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
