package com.rocketbank.rocketbank.MVP.View.ViewHolder;

import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by Egor on 10/11/15.
 */
public class MapActivityViewHolder {

    public MapFragment mapFragment;
    public GoogleMap googleMap;
    public Button btnSave;


    public MapActivityViewHolder(MapFragment mapFragment, Button btnSave) {
        this.mapFragment = mapFragment;
        this.btnSave = btnSave;
    }
}
