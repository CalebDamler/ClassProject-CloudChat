package com.example.caleb.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    private TextInputLayout displayName;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button createBtn;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private DatabaseReference database;
    private ProgressDialog progressDialog;


    /*****************************************************
     * onCreate()
     *
     * initialize objects variables and views
     * loading indicator
     *
     * onClick to create a new account with user input
     * data
     *
     * call registerUser to create the account
     *
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connect view
        setContentView(R.layout.activity_create_account);

        //get auth instance
        mAuth = FirebaseAuth.getInstance();

        //connect TextViews and buttons
        displayName = findViewById(R.id.textInputLayout2);
        email = findViewById(R.id.textInputLayout);
        password = findViewById(R.id.password);
        createBtn = findViewById(R.id.createBtn);

        //connect toolbar set up tool bar
        toolbar = findViewById(R.id.logToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading indecator
        progressDialog = new ProgressDialog(this);

        //onClick for the createbutton sends the user input data
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the text from each box
                String name = displayName.getEditText().getText().toString();
                String email2 = email.getEditText().getText().toString();
                String  pass = password.getEditText().getText().toString();

                //make sure none of the boxes are empty
                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email2)|| !TextUtils.isEmpty(pass)){

                    //show loading indicator
                    progressDialog.setTitle("Registering...");
                    progressDialog.setMessage("Please Wait!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    //call register passing the text from the boxes
                    registerUser(name, email2, pass);
                }


            }
        });

    }
/*******************************************************************************
 *registerUser()
 *
 * register User handles setting up the new account with the data
 * it received from the inputTextViews
 *
 * called by onClickListener in onCreate
 *
 *********************************************************************************/
    private void registerUser(final String name, String email2, String pass) {
        //use email and password login authentication
        mAuth.createUserWithEmailAndPassword(email2, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //close loading indicator
                            progressDialog.dismiss();

                            //get user instance
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            //get the current users ID
                            String uid = currentUser.getUid();

                            //create the child Users in the database by ID
                            database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            //hash map for user database structure
                            HashMap<String, String> map = new HashMap<>();
                            map.put("name", name );
                            map.put("status", "default status!!");
                            map.put("image", "default");
                            map.put("thumb_nail", "default");

                            //when the map completes do this
                            database.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //if the task completes
                                    if (task.isSuccessful()) {
                                        //drop waiting box
                                        progressDialog.dismiss();

                                        //now send the new user to the main page
                                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });




                           // updateUI(user);
                        } else {
                            progressDialog.hide();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                         //   updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
