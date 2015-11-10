package com.rocketbank.rocketbank.MVP.Model;

import android.graphics.Bitmap;
import android.location.Location;

import com.parse.FindCallback;
import com.parse.ParseObject;

import java.util.Calendar;

/**
 * Created by Egor on 09/11/15.
 */
public interface MainActivityModel {

    void getChatMessages(FindCallback<ParseObject> callback);

    void addGeoMessage(Bitmap bmp, Location loc);

    void addImgMessage(Bitmap bmp);

    void addTextMessage(String text);

    Calendar setTimeToNewMessage();

    byte[] bitmapToByteArray(Bitmap bmp);

    Bitmap getResizedBitmap(Bitmap bmp, int maxSize);

}
