package com.rocketbank.rocketbank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocketbank.rocketbank.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Egor on 04/11/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatMessage> mMessages;
    private View.OnClickListener listener;

    public ChatAdapter(Context mContext, ArrayList<ChatMessage> mMessages, View.OnClickListener listener) {
        this.mContext = mContext;
        this.mMessages = mMessages;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mMessages.get(position).getType()){
            case TextMessage: return 0;
            case ImageMesage:
            case GeoLocationMessage: return 1;
        }
        return 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_message, parent, false);
                return new TextViewHolder(v, mContext, listener);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_message, parent, false);
                return new ImageViewHolder(v, mContext, listener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ChatMessage message = mMessages.get(position);
        Date date;
        String time;

        if(message.getCreatedAt()==null){
            date = message.getDate();
            time = message.getTime();

        }else{
            date = message.getCreatedAt();
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
            time = String.valueOf(fmt.format(date));
        }
        date = getZeroTimeDate(date);

        String dateStr = "";
        Date today = getZeroTimeDate(new Date());

        Date yesterday = getZeroTimeDate(new Date(today.getTime() - 24 * 60 * 60 * 1000));
        if(date.equals(today))   dateStr = "сегодня";
        else if(date.equals(yesterday)) dateStr = "вчера";
        else {
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(date);
            Locale locale = new Locale("ru");
            dateStr = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + " " + String.format(locale,"%tB",c));
        }


        switch(mMessages.get(position).getType()){
            case TextMessage:
                ((TextViewHolder)holder).setTime(dateStr + ", " + time);
//                ((TextViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));
                ((TextViewHolder) holder).setTitle(message.getText());
                break;
            case ImageMesage:
//                ((ImageViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));
            case GeoLocationMessage:
//                if(message.getTime()!=null) ((ImageViewHolder) holder).setTime(message.getTime());
//                else ((ImageViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));
                ((ImageViewHolder)holder).setTime(dateStr + ", " + time);
                ((ImageViewHolder) holder).setImage(message.getBytes("image"));


                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public static Date getZeroTimeDate(Date date) {
        Date res = date;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

}
