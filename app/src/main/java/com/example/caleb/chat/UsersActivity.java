package com.example.caleb.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*************************************************************
 *UserActivity
 *
 * handles the allUsers page
 * sets up a recyclerView to hold each user that the app has
 * each users is a userCell that displays in a List
 *
 * the RecyclerView should allow use to scroll through all the users
 * but for some reason we cant.
 *
 * it should also allow us to click on any user cell, to see that users
 * profile. but we also cant click on anything
 *
 * its like the toolbar view or view that its inside of is overlapped
 * on top of the recyclerView, so when we press the touch doesn't register
 * on the correct view because of this I was could not get another users UID
 * to start a chat with. I tried to work around this by hardcoding
 * one of the user IDs
 *
 * I was not able to find a solution to this problem anywhere
 *
 * I tried to work around this problem by hardcoding one userIds
 * but that requires to versions of the app to work
 * one for the current user
 * and one for the user that we hardcoded
 *************************************************************/
public class UsersActivity extends AppCompatActivity {

    //toolbar
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    /********************************************************
     * onCreate()
     *
     * sets up the toolbar
     * gets the child we want
     * sets up the recycler view so we can have a list of users
     *
     *********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_users);
       //toolbar and setup
       toolbar = findViewById(R.id.User_app_bar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("All Users");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //get the users child
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //recycler setup
        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }
    /*************************************************************
     *onStart()
     *
     *builds the view for each userCell that gets loaded in
     *
     *************************************************************/
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<UserClass, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserClass, UserViewHolder>(UserClass.class, R.layout.user_cell, UserViewHolder.class, databaseReference ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, UserClass model, int position) {
                //set the value tp recycle items
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());

                //where the user clicks or it should we cant drag or click on users
                final String uid = getRef(position).getKey();

                //setting up a lister for if someone clicks on one of the user boxes
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //send them to the profile
                        Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);

                        //send the uid of the user over so we know which profile to grab from the database
                        intent.putExtra("uid", uid);
                    }
                });



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    /*************************************************************
     *UserViewHolder class
     *
     *this sets up the profile for us to see when we look at the list
     *
     *************************************************************/
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View view;

        public UserViewHolder(View itemView) {
            super(itemView);

            //one user cell thing
            view = itemView;
        }
        public void setName(String name){

            //show the name of the user in each cell
            TextView userNameView = view.findViewById(R.id.cell_name);
            userNameView.setText(name);
        }

        public void setStatus(String status){
            //show the status of the user in each cell
            TextView userStatus = view.findViewById(R.id.cell_status);
            userStatus.setText(status);
        }

    }



}
