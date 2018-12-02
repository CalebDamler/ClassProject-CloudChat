package com.example.caleb.chat;

import android.app.Notification;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by Caleb on 4/19/2018.
 * getting data from firebase and populating message fields
 *
 * I used this video for this code
 *
 * https://www.youtube.com/watch?v=1tGivzYHMoc&t=651s&list=PLGCjwl1RrtcQ3o2jmZtwu2wXEA4OIIq53&index=29
 */
/*************************************************************
 *MessageAdapter class
 *
 *setting up a list to hold all of the messages
 *adding views. this allows us to put the messages on
 *the screen
 *
 *
 *************************************************************/
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    //list to hold messages
    private List<Messages> MessageList;
    private FirebaseAuth auth;

    public MessageAdapter(List<Messages> MessageList){
        this.MessageList = MessageList;
    }

    /*************************************************************
     *MessageAdapter onCreateViewHolder
     *
     *create the viewHolder for the message
     *
     *
     *************************************************************/
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        //return the new view
        return new MessageViewHolder(v);
    }

    /*************************************************************
     *MessageViewHolderClass
     *
     *sets the message text and message image
     *
     *
     *************************************************************/
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        private ImageView imageView;

        public MessageViewHolder(View view){
            super(view);
            messageText = view.findViewById(R.id.msgText);
            imageView = view.findViewById(R.id.msgImg);
        }

    }
    /*************************************************************
     *onBindViewHolder
     *
     *setting background colors for each message sent based on
     * who the user is
     *
     *
     *************************************************************/
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        //auth instance
        auth = FirebaseAuth.getInstance();
        //get the current user
        String currentUSer = auth.getCurrentUser().getUid();
        Messages messages = MessageList.get(position);
        String fromUser = messages.getFrom();

        //text color and background color
        if (fromUser == currentUSer){
            holder.messageText.setBackgroundResource(R.color.colorAccent);
            holder.messageText.setTextColor(Color.BLACK);
        }else {
            holder.messageText.setBackgroundResource(R.color.colorPrimary);
            holder.messageText.setTextColor(Color.WHITE);
        }

        holder.messageText.setText(messages.getMessage());


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
}
