package com.example.plannr;

import android.media.Image;
import android.widget.ImageView;

/**
 * This class is used as the object for peoples friends and the data associated with them.
 * @author Tasman
 */
public class BasicFriend {
    protected String name;
    protected ImageView avatar;

    /**
     * contructor for the basic friend object/class
     * @param name the name of the friend
     * @param avatar the picture of the friend
     */
    public BasicFriend(String name, ImageView avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    /**
     * this methods gets the friends name
     * @return the name of the friend
     */
    public String getName() {
        return name;
    }

    /**
     * this methods sets the friends name
     * @param name name to set teh friend
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this methods gets the friends avatar/picture
     * @return the picture of the friend
     */
    public ImageView getAvatar() {
        return avatar;
    }

    /**
     * this method sets the friends picture
     * @param avatar the picture of the friend you want to use
     */
    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }
}
