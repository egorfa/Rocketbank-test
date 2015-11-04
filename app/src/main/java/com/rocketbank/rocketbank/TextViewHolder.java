package com.rocketbank.rocketbank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Egor on 04/11/15.
 */
public class TextViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView tvText, tvTime;


    public TextViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        this.tvText = (TextView) itemView.findViewById(R.id.tv_msg);
        this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);
    }

    void setTime(String time){
        tvTime.setText(time);
    }

    public void setTitle(String text) {tvText.setText(text);}
}
