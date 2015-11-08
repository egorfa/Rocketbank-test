package com.rocketbank.rocketbank.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;

/**
 * Created by Egor on 04/11/15.
 */
@ParseClassName("ImgMessage")
public class ImgMessage extends ChatMessage {

    public ImgMessage() {
    }

    public ParseFile getSrc() {
        return getParseFile("image");
    }

    public void setSrc(ParseFile src) {
        put("image", src);
    }
}
