package com.rocketbank.rocketbank.MVP.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rocketbank.rocketbank.MVP.View.ImageActivityView;

/**
 * Created by Egor on 10/11/15.
 */
public class ImageActivityPresenterImpl implements ImageActivityPresenter {

    private Activity activity;

    private final ImageActivityView view;

    public ImageActivityPresenterImpl(Activity activity, ImageActivityView view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = activity.getIntent();

        byte[] bytes = intent.getByteArrayExtra("image");

        view.displayImage(bytes);
    }


}
