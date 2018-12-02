package com.example.caleb.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*************************************************************
 *RealLoginActivity()
 * activity and class names got a little messy here
 *
 * handles the login process verifies the user credentials
 * and logs them in
 *

 *************************************************************/
public class RealLoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout email, password;
    private FirebaseAuth mAuth;
    private Button button;
    private ProgressDialog progressDialog;
    /*************************************************************
     *onCreate
     *
     * handles getting data and setting up screen items
     *
     * sends user data to Login() to log into the app
     *************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //connect the buttons and stuff
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.logToolBar);
        email = findViewById(R.id.textInputLayout);
        password = findViewById(R.id.textInputLayout2);
        button = findViewById(R.id.Login);

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        //waiting indicator
        progressDialog = new ProgressDialog(this);

        //onClick for login Button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the email and password from the textboxes
                String email2 = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();

                //make sure all fields are filled out
                if(!TextUtils.isEmpty(email2)|| !TextUtils.isEmpty(pass)){

                    //show waiting box
                    progressDialog.setTitle("Logging in...");
                    progressDialog.setMessage("Please Wait!");
                    //cant close the box
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    //call to login to log into the app
                    Login(email2, pass);
                }
            }
        });

    }
    /*************************************************************
     *Login()
     *
     * called by onClick in onCreate
     *
     * verifies the user and logs them in
     *
     * from firebase assistant editor
     *************************************************************/
    private void Login(String email2, String pass) {

        mAuth.signInWithEmailAndPassword(email2, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(RealLoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //prevent user from going back to start activity
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //hide dialog
                            progressDialog.hide();
                            Toast.makeText(RealLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });



    }
}
