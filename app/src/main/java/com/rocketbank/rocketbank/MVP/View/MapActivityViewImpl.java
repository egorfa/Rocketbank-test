package com.rocketbank.rocketbank.MVP.View;

import android.location.Location;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MapActivityViewHolder;
import com.rocketbank.rocketbank.R;

/**
 * Created by Egor on 10/11/15.
 */
public class MapActivityViewImpl implements MapActivityView, View.OnTouchListener{

    private final MapActivityViewHolder viewHolder;

    public MapActivityViewImpl(MapActivityViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    public void btnSaveSetTouchSettings() {
        viewHolder.btnSave.setOnTouchListener(this);
    }

    @Override
    public void displayGeoLocation(double latitude, double longitude) {
        LatLng loc = new LatLng(latitude, longitude);
        Marker mMarker = viewHolder.googleMap.addMarker(new MarkerOptions().position(loc));
        if (viewHolder.googleMap != null) {
            viewHolder.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }
    }

    @Override
    public void addMyLocationMarker(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        viewHolder.googleMap.clear();
        viewHolder.googleMap.addMarker(new MarkerOptions().position(loc));
        if (viewHolder.googleMap != null) {
            viewHolder.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.btn:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewHolder.btnSave.setAlpha(1f);
                        break;
                    case MotionEvent.ACTION_UP:
                        viewHolder.btnSave.setAlpha(0.64f);
                        break;
                }
                break;
        }
        return false;
    }
}
