package com.rocketbank.rocketbank.model;

/**
 * Created by Egor on 04/11/15.
 */
public class TextMessage extends ChatMessage {

    private String text;

    public TextMessage(TypeMessage type, String date, String time, String text) {
        super(type, date, time);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
