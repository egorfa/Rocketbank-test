package com.rocketbank.rocketbank.model;

/**
 * Created by Egor on 04/11/15.
 */
public class ImgMessage extends ChatMessage {

    private String src;

    public ImgMessage(TypeMessage type, String date, String time, String src) {
        super(type, date, time);
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
