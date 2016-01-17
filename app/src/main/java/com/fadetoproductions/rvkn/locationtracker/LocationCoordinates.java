package com.fadetoproductions.rvkn.locationtracker;

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


}
