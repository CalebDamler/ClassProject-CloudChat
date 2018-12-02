package com.example.caleb.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;
/*************************************************************
 *settingsActivity
 *
 *
 *
 *
 *************************************************************/
public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    //layout
    private ImageView imageView;
    private TextView Name, Status;

    private Button statusBtn, imgBtn;
    private ProgressDialog progressDialog;
    private static final int GALLARYPIC = 1;
    //storage ref
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connect views buttons and other stuff
        setContentView(R.layout.activity_settings);
        Name = findViewById(R.id.settingName);
        Status = findViewById(R.id.settingStatus);
        imageView = findViewById(R.id.settingsImg);
        statusBtn = findViewById(R.id.settingStatusBtn);
        imgBtn = findViewById(R.id.settingPicChangeBtn);

        //get the current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //storage reference
        storageReference = FirebaseStorage.getInstance().getReference();
        //get the current users ID
        String currentUid = firebaseUser.getUid();
        //point the the UID under the child Users
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        //event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //add data or retrieve data
               String name = dataSnapshot.child("name").getValue().toString();
               String image = dataSnapshot.child("image").getValue().toString();
               String status = dataSnapshot.child("status").getValue().toString();
               String thumb_img = dataSnapshot.child("thumb_nail").getValue().toString();

               //set name and status
               Name.setText(name);
               Status.setText(status);
               //picasso for github use to load image from database to user screen
               Picasso.get().load(image).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //errors
            }
        });

        //status button click takes you to update the status
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the current status
                String statusVal = Status.getText().toString();
                Intent intent = new Intent(SettingsActivity.this, UpdateStatus.class);
                //send over the current status too
                intent.putExtra("StatusVal", statusVal);
                startActivity(intent);
            }
        });

        //change image
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");

                //let the user choose from the device images
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);
                //choose image getter
                startActivityForResult(Intent.createChooser(imgIntent, "SELECT IMAGE"),GALLARYPIC );
            }
        });
    }
    /*************************************************************
     *onActivityResult
     *FireBase method copied from the assistent editor
     *
     *
     *
     *************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLARYPIC && resultCode == RESULT_OK){

            //waiting indicator
            progressDialog = new ProgressDialog(SettingsActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //get URI data
            Uri imageURI = data.getData();

            //get the current users UID
            String current_id = firebaseUser.getUid();

            //storage reference/path
            StorageReference filePath = storageReference.child("profile_images").child(current_id + ".jpg");

            //check for task complete
            filePath.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //get tje url
                        String download = task.getResult().getDownloadUrl().toString();
                        //set the new image from the user and verify completion
                        databaseReference.child("image").setValue(download).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingsActivity.this, "we all gucci", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else{
                        Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
           // imageView = imageURI;

        }
    }

}
