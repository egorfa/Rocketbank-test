package com.rocketbank.rocketbank.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Egor on 04/11/15.
 */
@ParseClassName("ChatMessage")
public class ChatMessage extends ParseObject{

    private String time;
    private Date date;
    public ChatMessage(){

    }
    private TypeFrom from;

    public ChatMessage(String time) {
        this.time = "сегодня, " + time;
    }

    //    public ChatMessage(TypeMessage type, String date, String time) {
//        Type = type;
//        Date = date;
//        Time = time;
//    }

    public TypeMessage getType() {
        return TypeMessage.valueOf(getString("type"));
    }

    public void setType(TypeMessage type) {
        put("type", type.toString());
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
        return getBytes("image");
    }

    public void setBmp(byte[] bmp) {
        put("image", bmp);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TypeFrom getFrom() {
        return from;
    }

    public void setFrom(TypeFrom from) {
        this.from = from;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
