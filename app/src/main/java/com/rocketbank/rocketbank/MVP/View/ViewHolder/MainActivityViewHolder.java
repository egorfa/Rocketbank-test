package com.rocketbank.rocketbank.MVP.View.ViewHolder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Egor on 09/11/15.
 */
public class MainActivityViewHolder {

    public ViewGroup fabContainer;
    public LinearLayout textContainer;
    public FloatingActionButton fabAdd, fabText, fabCamera, fabAlbum, fabGeo;
    public RecyclerView rvChat;
    public ImageView imgMask;
    public TextView tvText, tvCamera, tvAlbum, tvGeo;
    public EditText etMsg;
    public ImageButton btnSend;

    public MainActivityViewHolder(ViewGroup fabContainer, LinearLayout textContainer, FloatingActionButton fabAdd, FloatingActionButton fabText, FloatingActionButton fabCamera, FloatingActionButton fabAlbum, FloatingActionButton fabGeo, RecyclerView rvChat, ImageView imgMask, TextView tvText, TextView tvCamera, TextView tvAlbum, TextView tvGeo, EditText etMsg, ImageButton btnSend) {
        this.fabContainer = fabContainer;
        this.textContainer = textContainer;
        this.fabAdd = fabAdd;
        this.fabText = fabText;
        this.fabCamera = fabCamera;
        this.fabAlbum = fabAlbum;
        this.fabGeo = fabGeo;
        this.rvChat = rvChat;
        this.imgMask = imgMask;
        this.tvText = tvText;
        this.tvCamera = tvCamera;
        this.tvAlbum = tvAlbum;
        this.tvGeo = tvGeo;
        this.etMsg = etMsg;
        this.btnSend = btnSend;
    }
}
