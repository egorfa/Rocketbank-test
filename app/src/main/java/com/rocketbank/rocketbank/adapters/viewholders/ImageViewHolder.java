package com.rocketbank.rocketbank.adapters.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.rocketbank.rocketbank.R;

/**
 * Created by Egor on 04/11/15.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private ParseImageView imgMessage;
    private TextView tvTime;

    public ImageViewHolder(View itemView, Context mContext, View.OnClickListener listener) {
        super(itemView);
        this.mContext = mContext;
        this.imgMessage = (ParseImageView) itemView.findViewById(R.id.img_msg);
        this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        itemView.setOnClickListener(listener);
    }

    public void setTime(String time){
        tvTime.setText(time);
    }

    public void setImage(byte[] bytes) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgMessage.setImageBitmap(bmp);
//        imgMessage.setParseFile(bmp);
//        imgMessage.loadInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, ParseException e) {
//                imgMessage.setVisibility(View.VISIBLE);
//            }
//        });
//        Glide.with(mContext)
//            .load(src)
//            .into(imgMessage);
    }
}
