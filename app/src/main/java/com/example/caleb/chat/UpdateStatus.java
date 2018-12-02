package com.example.caleb.chat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*************************************************************
 *updateStatus
 *
 * handles the status update when the user wants to change there
 * status
 *
 *************************************************************/
public class UpdateStatus extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutstatus;
    private Button button;
    //user
    private FirebaseUser firebaseUser;
    //progress indicator
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        //get the current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = firebaseUser.getUid();

        //point to user in database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //toolbar and setup
        toolbar = findViewById(R.id.status_appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get the new status
        String statusVal = getIntent().getStringExtra("StatusVal");
        textInputLayoutstatus = findViewById(R.id.statusInput);
        button = findViewById(R.id.ChangeBtn);

        //set the new status to the current status
        textInputLayoutstatus.getEditText().setText(statusVal);
        //on click for the change status button
        //update the status to a new value
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //waiting indicator
                progressDialog = new ProgressDialog(UpdateStatus.this);
                progressDialog.setTitle("Saving...");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                //get the status
                String status = textInputLayoutstatus.getEditText().getText().toString();
                //store to firebase
                databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //dismiss the waiting box
                            progressDialog.dismiss();

                        } else{
                            //error
                            Toast.makeText(UpdateStatus.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


    }
}
