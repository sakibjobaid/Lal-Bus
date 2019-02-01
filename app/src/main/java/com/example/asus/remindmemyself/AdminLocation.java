package com.example.asus.remindmemyself;

public  class AdminLocation {
    double latitude,longitude;

    public AdminLocation(double  latitude, double  longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double  getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
