package com.rocketbank.rocketbank.MVP.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rocketbank.rocketbank.MVP.View.ViewHolder.ImageActivityViewHolder;

/**
 * Created by Egor on 10/11/15.
 */
public class ImageActivityViewImpl implements ImageActivityView {

    private final ImageActivityViewHolder viewHolder;

    public ImageActivityViewImpl(ImageActivityViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    public void displayImage(byte[] bytes) {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        viewHolder.imgDisplay.setImageBitmap(bmp);
    }
}
