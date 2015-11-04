package com.rocketbank.rocketbank.model;

/**
 * Created by Egor on 04/11/15.
 */
public class ChatMessage {

    private TypeMessage Type;
    private String Date;
    private String Time;

    public ChatMessage(TypeMessage type, String date, String time) {
        Type = type;
        Date = date;
        Time = time;
    }

    public TypeMessage getType() {
        return Type;
    }

    public void setType(TypeMessage type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
