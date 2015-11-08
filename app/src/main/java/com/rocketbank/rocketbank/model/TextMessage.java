package com.rocketbank.rocketbank.model;

import com.parse.ParseClassName;

/**
 * Created by Egor on 04/11/15.
 */
@ParseClassName("TextMessage")
public class TextMessage extends ChatMessage {

    public TextMessage() {
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }
}
