package com.rocketbank.rocketbank.MVP.Model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Egor on 10/11/15.
 */
public interface MapActivityModel {

    void setSettingsOnMap(OnMapReadyCallback callback);

    void setClickListeners();

    void returnResult(Bitmap bitmap);

    Bitmap getResizedBitmap(Bitmap bmp, int maxSize);

    void setLocationManager();

    void cancelLocationManager();

    void requestUpdateLocation(int grantResult);

    void requestRemoveUpdateLocation(int grantResult);

}
