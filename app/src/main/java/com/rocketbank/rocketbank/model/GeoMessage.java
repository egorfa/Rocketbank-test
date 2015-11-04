package com.rocketbank.rocketbank.model;

/**
 * Created by Egor on 04/11/15.
 */
public class GeoMessage extends ChatMessage {

    private Integer longitude, latitude;
    private String src;

    public GeoMessage(TypeMessage type, String date, String time, Integer longitude, Integer latitude, String src) {
        super(type, date, time);
        this.longitude = longitude;
        this.latitude = latitude;
        this.src = src;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
