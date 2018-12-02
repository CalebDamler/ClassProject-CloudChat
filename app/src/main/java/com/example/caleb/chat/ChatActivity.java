package com.example.caleb.chat;

import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    //should be the other user but not able to get them
    private String ChatUser;
    Toolbar toolbar;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String currentUserId;

    //hard coded userID not currently working
    //only works for other users
    //hard coded ID create a chat with itself when it runs the app
    //cannot send msgs
    private String otherUID = "d0XQUteM4QO74Vz9Y5Y28rT2Vxt2";

    private ImageButton sendButton;
    private EditText editText;

    private RecyclerView messageList;
    private final List<Messages> mList = new ArrayList<>();

    //layout for the messeges
    private LinearLayoutManager layoutManager;
    private MessageAdapter adapter;


    /******************************************************************************
     *onCreate()
     * does all necessary view, message, and database set up
     * create the char in the database for each user
     * lets us take the message that the user typed into the editText
     * set it to a list then send it to the other user
     *
     *******************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_avtivity);

        //tool bar and set up
        toolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chatting...");

        //text and buttons
        sendButton = findViewById(R.id.sendButton);
        editText = findViewById(R.id.editText);

        //our adapter from the messageAdapter class
        adapter = new MessageAdapter(mList);
        //list of messages for the recyclerView
        messageList = findViewById(R.id.chatRecycle);
        layoutManager = new LinearLayoutManager(this);

        //set the adapters
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(layoutManager);
        messageList.setAdapter(adapter);

        //Firebase instances
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        //current user ID
        currentUserId = auth.getCurrentUser().getUid();

        //call setMsg to deal with the data
        setMsg();

        //pointing to Current user in chats section of database
        databaseReference.child("Chats").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //if the chat does not exist create it
                if (!dataSnapshot.hasChild(otherUID)) {

                    //hash map for the database extra message info
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    //map of the database Chat section
                    Map chatUserMap = new HashMap();

                    //create a new child in each users directory
                    chatUserMap.put("Chats/" + currentUserId + "/" + otherUID, chatAddMap);
                    chatUserMap.put("Chats/" + otherUID + "/" + currentUserId, chatAddMap);


                    //error checking
                   databaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null) {
                                Log.d("CHAT_LOG", databaseError.getMessage());
                            }
                        }
                   });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }
    /******************************************************************************
     *setMsg()
     *
     *get the message the user wants to send from the editText
     *add it to the list
     *
     *******************************************************************************/
    private void setMsg() {
        //point to the user under the child messages
        databaseReference.child("messages").child(currentUserId).child(otherUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //get message
                Messages messages = dataSnapshot.getValue(Messages.class);
                //add to list
                mList.add(messages);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /******************************************************************************
     *sendMessage()
     *send to the other users database reference
     *gets the path of each user in the database
     *send the message map from one to the other
     *
     *******************************************************************************/
    private void sendMessage(){
        //get the message from the textBox
        String msg = editText.getText().toString();

        //make sure text is not empty
        if(!TextUtils.isEmpty(msg)){


            /*************************************************************************
             * this is where things get messed up the current user is able to create a
             * chat with the hardcoded user this works fine for anyone that is not the harcoded user
             *
             *
             * when the hardcoded user creates the chat to receive messages this code runs
             * and basically creates a chat from the the hardcoded user to the currentuser
             * because when the hardcoded user runs it
             *
             * currentUserID == otherID
             *
             * this results in the hardcoded user creating a chat with himself
             *
             * a potential fix is having a separate section of code run for that user
             * but that is a pretty extreme work around and got to far away from the
             * original goal of the app, and im not sure it would work anyways
             *
             * the goal of hardcoding one of the users was so i could at least get a chat going
             * with in the app so i could demonstrate that is sends messages at least
             *
             *
             ************************************************************************/
            //path of each user
            String currentUserRef = "messages/" + currentUserId + "/" + otherUID;
            String otherUserRef = "messages/" + otherUID + "/" + currentUserId;

            //point to the chat under the child message
            DatabaseReference msgReference = databaseReference.child("messages").child(currentUserId).child(otherUID).push();

            //not sure what push keys are  but I know we need it figure out where the message is going
            //used a guide on youTube for this
            String pushId = msgReference.getKey();

            //holds the necessary info and details for the message we are sending
            Map msgMap = new HashMap();
            msgMap.put("message", msg );
            msgMap.put("seen", false);
            msgMap.put("type", "text");
            msgMap.put("time", ServerValue.TIMESTAMP);
            msgMap.put("from", currentUserId);

            //send the messages between the two users
            Map msgUserMap = new HashMap();
            msgUserMap.put(currentUserRef + "/" + pushId, msgMap );
            msgUserMap.put(otherUserRef + "/" + pushId, msgMap );

            //clear the text after send it pressed
            editText.setText("");

            //error checking
            databaseReference.updateChildren(msgUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null) {
                        Log.d("CHAT_LOG", databaseError.getMessage());
                    }

                }
            });
        }
    }
}
