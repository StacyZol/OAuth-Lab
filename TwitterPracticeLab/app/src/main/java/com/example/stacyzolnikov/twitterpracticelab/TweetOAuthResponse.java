package com.example.stacyzolnikov.twitterpracticelab;

import java.util.ArrayList;

/**
 * Created by stacyzolnikov on 8/10/16.
 */
public class TweetOAuthResponse {
    ArrayList<String> description;
    String hashtags;
    String created_at;
    String location;

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
