package com.fadetoproductions.rvkn.locationtracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.SaveCallback;

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
            this.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
//                        Toast.makeText(mainContext, "Your location has been successfully saved!", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "Successfully saved to Parse!");
                    } else {
//                        Toast.makeText(mainContext, "There was an issue saving your location!", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "There was an issue saving to Parse.");
                    }
                }
            });
        }
    }
}
