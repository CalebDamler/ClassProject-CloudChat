package com.example.caleb.chat;

/*****************************************************************************************
 * Caleb Damler
 *
 * CSCI 428
 * Final Project: Firebase Cloud Chat
 * Brown
 * 4/30/2018
 *
 * This app should allow a user to chat with any other user that is currently using the app
 * all data is stored in a google firebase database.
 *
 * I used a lot of online recourse for this.
 * I used this YouTube account to guide me through the firebase setup and how to manipulate
 * the data in the database
 * https://www.youtube.com/channel/UCl6DxakCjDR5AfRwWhWNbMg
 *
 * I also used lynda.com
 * https://www.lynda.com/Android-tutorials/Welcome/604238/703086-4.html?srchtrk=index%3a1%0alinktypeid%3a2%0aq%3aFCM%0apage%3a1%0as%3arelevance%0asa%3atrue%0aproducttypeid%3a2
 *
 * this gitHub repository for downloading images from the database
 * https://github.com/square/picasso
 *
 * the bee.png from the demo is my default image for everything
 *
 *
 * Currently this app does not totally work I can get messages to show up on the screen like you
 * would see in a basic texting app, they also store in the database fine, but I had trouble getting
 * each user to be able to retrieve each message on their individual screen. More detail on this
 * in ChatActivity and UserActivity
 *
 * to send a message you need to click the three dots in the top right and then click send message in the
 * drop down menu
 *
 * this opens a chat with a hardcoded user so you don't have the option to chat with anyone else yet.
 * this was a work around since i could not click on users in the UseActivity screen to get their UID
 *
 *
 *******************************************************************************************/


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //firebase authentication
    private FirebaseAuth mAuth;
    //toolbar
    private android.support.v7.widget.Toolbar toolbar;
    //viewpager
    private ViewPager viewPager;
    //adapter
    private SectionsPagerAdapter sectionsPagerAdapter;
    //tabs
    private TabLayout tabLayout;

    /*************************************************************
     * onCreate()
     *
     * connect the view and all items on screen

     *
     ********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //main layout
        setContentView(R.layout.activity_main);

        //auth var initialize
        mAuth = FirebaseAuth.getInstance();

        //toolbar and setup
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cloud Chat");

        //view pager and set up
        viewPager = findViewById(R.id.viewPager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        //tabs
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /********************************************************
     * onStart()
     *
     *check if user has account
     *authentication check
     *if no user send them to login
     ***********************************************************/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); // store current user
        //updateUI(currentUser);

        //if the user is not singed in
        if(currentUser == null){
           Send();
        }
    }
    /*************************************************************
     *Send()
     *
     * sends the user back to the login screen
     *
     * reducing redundant code
     ************************************************************/
    private void Send() {
        //bring to login
        Intent intent = new Intent(MainActivity.this, LoginActivity.class );
        startActivity(intent);
        finish(); // do not return to this page if the back button is clicked
    }
    /*************************************************************
     *onCreateOptionsMenu()
     *
     * populates the menu with the buttons we put in the layout
     *
     *inflate the menu with the buttons we chose
     ************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //connect the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

/*************************************************************
 * onOptionsItemSelected()
 *
 * menu for the top right drop down menu
 *
 * handles which button is pressed from the button in the top right
 * corner
 *
 * settings
 * all users
 * logout
 * send message
 *
 ************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //if btn is log out
        if(item.getItemId() == R.id.mainLogOut) {
            //sign the user out
            FirebaseAuth.getInstance().signOut();
            //back to login
            Send();
        }

        //if btn is settings
        if(item.getItemId() == R.id.mainSettings){
            //send them to the settings activity
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        //if button is all users
        if (item.getItemId() == R.id.mainAll){
            //send them to the all users page
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(intent);
        }

        //if btn is send message
        if (item.getItemId() == R.id.SendMessage){
            //send them to a chat this chat is hardcoded in only allows 2 users that cant be changed
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        }


        return true;
    }
}
