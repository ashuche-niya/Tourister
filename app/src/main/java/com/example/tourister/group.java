package com.example.tourister;

import java.util.ArrayList;

public class group {
    ArrayList<String> usernames;
    ArrayList<String> locations;
    ArrayList<String> paids;


    public group(ArrayList<String> usernames, ArrayList<String> locations, ArrayList<String> paids) {
        this.usernames = usernames;
        this.locations = locations;
        this.paids = paids;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public ArrayList<String> getPaids() {
        return paids;
    }
}
