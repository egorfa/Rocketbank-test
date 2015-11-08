package com.rocketbank.rocketbank.model;

import com.parse.ParseClassName;

/**
 * Created by Egor on 04/11/15.
 */
@ParseClassName("GeoMessage")
public class GeoMessage extends ChatMessage {

    public GeoMessage() {

    }

    public double getLongitude() {
        return getDouble("longitude");
    }

    public void setLongitude(double longitude) {
        put("longitude", longitude);
    }

    public double getLatitude() {
        return getDouble("latitude");
    }

    public void setLatitude(double latitude) {
        put("latitude", latitude);
    }

    public byte[] getBmp() {
        return getBytes("map");
    }

    public void setBmp(byte[] bmp) {
        put("map", bmp);
    }
}
