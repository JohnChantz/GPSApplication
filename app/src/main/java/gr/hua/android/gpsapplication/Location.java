package gr.hua.android.gpsapplication;

import java.util.ArrayList;

public class Location {

    private String userID;
    private String username;
    private int latitude;
    private int longtitude;
    private String current_location;
    public ArrayList<Location> locations = new ArrayList<>();

    public Location(String username, int latitude, int longtitude) {
        this.username = username;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Location(int latitude, int longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Location() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(int longtitude) {
        this.longtitude = longtitude;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }
}