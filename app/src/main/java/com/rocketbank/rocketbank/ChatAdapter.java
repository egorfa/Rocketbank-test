package com.rocketbank.rocketbank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocketbank.rocketbank.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Egor on 04/11/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatMessage> mMessages;

    public ChatAdapter(Context mContext, ArrayList<ChatMessage> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
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
                return new TextViewHolder(v, mContext);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_message, parent, false);
                return new ImageViewHolder(v, mContext);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = mMessages.get(position);


        switch(mMessages.get(position).getType()){
            case TextMessage:
//                ((TextViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));
                ((TextViewHolder) holder).setTitle(message.getText());
                break;
            case ImageMesage:
//                ((ImageViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));
                break;
            case GeoLocationMessage:
                if(message.getTime()!=null) ((ImageViewHolder) holder).setTime(message.getTime());
                else ((ImageViewHolder) holder).setTime(String.valueOf(message.getCreatedAt().getHours() + ":" + message.getCreatedAt().getMinutes()));

                ((ImageViewHolder) holder).setImage(message.getBytes("image"));


                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


}
