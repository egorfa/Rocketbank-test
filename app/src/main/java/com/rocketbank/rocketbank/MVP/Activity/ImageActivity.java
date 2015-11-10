package com.rocketbank.rocketbank.MVP.Activity;

import android.os.Bundle;

import com.rocketbank.rocketbank.MVP.Presenter.ImageActivityPresenter;
import com.rocketbank.rocketbank.MVP.Presenter.ImageActivityPresenterImpl;
import com.rocketbank.rocketbank.MVP.View.ImageActivityView;
import com.rocketbank.rocketbank.MVP.View.ImageActivityViewImpl;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.ImageActivityViewHolder;
import com.rocketbank.rocketbank.R;
import com.rocketbank.rocketbank.Utils.TouchImageView;

/**
 * Created by Egor on 10/11/15.
 */
public class ImageActivity extends BaseActivity {

    private ImageActivityPresenter presenter;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ImageActivityViewHolder viewHolder = new ImageActivityViewHolder((TouchImageView) findViewById(R.id.imgDisplay));

        final ImageActivityView view = new ImageActivityViewImpl(viewHolder);

        presenter = new ImageActivityPresenterImpl(this, view);

        presenter.onCreate(savedInstanceState);

    }
}
