package com.rocketbank.rocketbank.MVP.View;

import android.location.Location;

/**
 * Created by Egor on 10/11/15.
 */
public interface MapActivityView {

    void btnSaveSetTouchSettings();

    void displayGeoLocation(double latitude, double longitude);

    void addMyLocationMarker(Location location);

}
