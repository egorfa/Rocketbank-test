package com.rocketbank.rocketbank.MVP.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocketbank.rocketbank.MVP.Model.MainActivityModel;
import com.rocketbank.rocketbank.MVP.Model.MainActivityModelImpl;
import com.rocketbank.rocketbank.MVP.Presenter.MainActivityPresenter;
import com.rocketbank.rocketbank.MVP.Presenter.MainActivityPresenterImpl;
import com.rocketbank.rocketbank.MVP.View.MainActivityView;
import com.rocketbank.rocketbank.MVP.View.MainActivityViewImpl;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MainActivityViewHolder;
import com.rocketbank.rocketbank.R;
import com.rocketbank.rocketbank.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Egor on 09/11/15.
 */
public class MainActivity extends BaseActivity {

    private MainActivityPresenter presenter;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainActivityViewHolder mainActivityViewHolder = new MainActivityViewHolder(
                (ViewGroup) findViewById(R.id.fab_container),
                (LinearLayout) findViewById(R.id.ll_text),
                (FloatingActionButton) findViewById(R.id.fab_add),
                (FloatingActionButton) findViewById(R.id.fab_text),
                (FloatingActionButton) findViewById(R.id.fab_camera),
                (FloatingActionButton) findViewById(R.id.fab_album),
                (FloatingActionButton) findViewById(R.id.fab_geo),
                (RecyclerView) findViewById(R.id.rvChat),
                (ImageView) findViewById(R.id.img_mask),
                (TextView) findViewById(R.id.tv_text),
                (TextView) findViewById(R.id.tv_camera),
                (TextView) findViewById(R.id.tv_album),
                (TextView) findViewById(R.id.tv_geo),
                (EditText) findViewById(R.id.et_msg),
                (ImageButton) findViewById(R.id.btn_send)
        );

        ArrayList<ChatMessage> Array = new ArrayList<>();

        final MainActivityView view = new MainActivityViewImpl(this, mainActivityViewHolder);

        final MainActivityModel model = new MainActivityModelImpl(this, Array);

        presenter = new MainActivityPresenterImpl(this, model, view, mainActivityViewHolder, Array);

        presenter.onCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        if (presenter.onBackPressed()) super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
