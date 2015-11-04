package com.rocketbank.rocketbank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Egor on 04/11/15.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private ImageView imgMessage;
    private TextView tvTime;

    public ImageViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        this.imgMessage = (ImageView) itemView.findViewById(R.id.img_msg);
        this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);
    }

    void setTime(String time){
        tvTime.setText(time);
    }

    void setImage(String src) {
        Glide.with(mContext)
            .load(src)
            .into(imgMessage);
    }
}
