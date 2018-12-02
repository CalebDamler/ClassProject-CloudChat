package com.example.caleb.chat;

/**
 * Created by Caleb on 4/19/2018.
 */
/********************************************************
 *Message class
 *
 * holds
 *
 * string the message
 * string the type of message
 * bool send or not
 * long time
 *
 *******************************************************/
public class Messages {
    private String message, type;
    private boolean seen;
    private long time;



    //constructor
    public Messages(String message, String type, boolean seen, long time, String from) {
        this.message = message;
        this.type = type;
        this.seen = seen;
        this.time = time;
        this.from = from;
    }
    //constructor
    public Messages(){

    }

    //getters and setters
    public void setFrom(String from) {

        this.from = from;
    }

    private String from;



    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
