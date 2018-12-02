package com.example.caleb.chat;

/**
 * Created by Caleb on 4/12/2018.
 */
/*************************************************************
 *UserClass
 *
 * class for each user
 * holds
 *
 * name
 * image/profile picture
 * status
 *
 *
 *************************************************************/
public class UserClass {

    public String name, image, status;
    //constructor
    public UserClass(){

    }
    //constructor
    public UserClass(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
