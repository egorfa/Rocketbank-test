package com.rocketbank.rocketbank.MVP.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rocketbank.rocketbank.model.ChatMessage;
import com.rocketbank.rocketbank.model.TypeFrom;
import com.rocketbank.rocketbank.model.TypeMessage;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Egor on 09/11/15.
 */
public class MainActivityModelImpl implements MainActivityModel {

    private ArrayList<ChatMessage> Array;
    private Activity activity;
    private Context mContext;

    public MainActivityModelImpl(Activity activity, ArrayList<ChatMessage> array) {
        Array = array;
        this.activity = activity;
        mContext = activity.getApplicationContext();
    }

    @Override
    public void getChatMessages(final FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessage");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> markers, ParseException e) {

                if (e == null) {
                    // your logic here
                    ArrayList<ChatMessage> array = null;
                    for (int i = markers.size()-1; i>=0; i-- ) {
                        ChatMessage message = (ChatMessage) markers.get(i);
                        message.setFrom(TypeFrom.Server);
                        Array.add(message);
                        callback.done(markers, e);


//                        rvChat.getAdapter().notifyDataSetChanged();
//                        rvChat.scrollToPosition(0);
                    }


                } else {
                    Toast.makeText(mContext, "Ошибка при загрузке сообщений, попробуйте позднее", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void addGeoMessage(Bitmap bmp, Location loc) {
        Calendar c = setTimeToNewMessage();
        final ChatMessage message = new ChatMessage();
        message.setType(TypeMessage.GeoLocationMessage);
        message.setLatitude(loc.getLatitude());
        message.setLongitude(loc.getLongitude());
        message.setBmp(bitmapToByteArray(bmp));
        message.saveInBackground();

        message.setDate(new Date());
//        message.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        message.setTime(String.valueOf(fmt.format(new Date())));
        message.setFrom(TypeFrom.Local);

        Array.add(0, message);


    }

    @Override
    public void addImgMessage(Bitmap bmp) {
        Calendar c = setTimeToNewMessage();
        final ChatMessage imgMessage = new ChatMessage();
        imgMessage.setType(TypeMessage.ImageMesage);
        imgMessage.setBmp(bitmapToByteArray(bmp));
        imgMessage.saveInBackground();

        imgMessage.setDate(new Date());
//        imgMessage.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        imgMessage.setTime(String.valueOf(fmt.format(new Date())));
        imgMessage.setFrom(TypeFrom.Local);

        Array.add(0, imgMessage);
    }

    @Override
    public void addTextMessage(String text) {
        Calendar c = setTimeToNewMessage();
        final ChatMessage message = new ChatMessage(c.getTime().toString());
        message.setType(TypeMessage.TextMessage);
        message.setText(text);
        message.saveInBackground();
        message.setDate(new Date());//c.get(Calendar.DAY_OF_MONTH) + " " + String.format(Locale.US,"%tB",c)));
//        message.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        message.setTime(String.valueOf(fmt.format(new Date())));
        message.setFrom(TypeFrom.Local);

        Array.add(0, message);

    }

    @Override
    public Calendar setTimeToNewMessage() {
        Date date = new Date();
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Override
    public byte[] bitmapToByteArray(Bitmap bmp) {
        bmp = getResizedBitmap(bmp, 200);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public Bitmap getResizedBitmap(Bitmap bmp, int maxSize) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bmp, width, height, true);
    }
}
