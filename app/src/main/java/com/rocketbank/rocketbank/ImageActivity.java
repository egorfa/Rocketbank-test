package com.rocketbank.rocketbank;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Egor on 08/11/15.
 */
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);

        Intent intent = getIntent();

        byte[] bytes = intent.getByteArrayExtra("image");

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        TouchImageView img = (TouchImageView) findViewById(R.id.imgDisplay);
        img.setImageBitmap(bmp);
    }
}
