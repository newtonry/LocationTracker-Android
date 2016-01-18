package com.fadetoproductions.rvkn.locationtracker;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;


/**
 * Created by rnewton on 1/16/16.
 */
@ParseClassName("LocationCoordinates")
public class LocationCoordinates extends ParseObject {

    public void setLocation(String location) {
        this.put("location", location);
    }

    public void setTimeVisited(Date timeVisited) {
        this.put("timeVisited", timeVisited);
    }

    public void setUsername(String username) {
        if (username != null) {
            this.put("username", username);
        }
    }

    public void uploadToParse() {
        if (this.getString("location") == null) {
            Log.d("DEBUG", "Not saving to Parse. Missing location!");
        } else if (this.getString("username") == null) {
            Log.d("DEBUG", "Not saving to Parse. Missing username!");
        } else {
            Log.d("DEBUG", "Uploading location to Parse!");
            this.saveInBackground();
        }
    }
}
