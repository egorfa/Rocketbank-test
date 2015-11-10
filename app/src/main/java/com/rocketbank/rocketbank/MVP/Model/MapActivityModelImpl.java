package com.rocketbank.rocketbank.MVP.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rocketbank.rocketbank.MVP.View.MapActivityView;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MapActivityViewHolder;
import com.rocketbank.rocketbank.MapActivity;
import com.rocketbank.rocketbank.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Egor on 10/11/15.
 */
public class MapActivityModelImpl implements MapActivityModel, View.OnClickListener, LocationListener{

    private Activity activity;
    private Context mContext;
    private MapActivityView view;
    private MapActivityViewHolder viewHolder;

    private Location myLocation;
    private LocationManager locationManager;

    private boolean isSettingLocationUpdates;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 51;
    final private int REQUEST_CODE_ASK_CANCEL_PERMISSIONS = 52;

    public MapActivityModelImpl(Activity activity, MapActivityView view, MapActivityViewHolder viewHolder) {
        this.activity = activity;
        this.view = view;
        mContext = activity.getApplicationContext();
        this.viewHolder = viewHolder;
    }


    @Override
    public void setSettingsOnMap(OnMapReadyCallback callback) {
        viewHolder.mapFragment.getMapAsync(callback);
    }

    @Override
    public void setClickListeners() {
        viewHolder.btnSave.setOnClickListener(this);
    }

    @Override
    public void returnResult(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Intent intent = new Intent();
            intent.putExtra("bitmap", getResizedBitmap(bitmap, 200));
            intent.putExtra("location", myLocation);
            activity.setResult(Activity.RESULT_OK, intent);
            cancelLocationManager();
            activity.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getResizedBitmap(Bitmap bmp, int maxSize) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bmp, width, height, true);
    }

    @Override
    public void setLocationManager() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isSettingLocationUpdates = true;
        int hasFineLocationPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasFineLocationPermission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*15, 0, this);
    }

    @Override
    public void cancelLocationManager() {
        int hasFineLocationPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasFineLocationPermission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_CANCEL_PERMISSIONS);
                }
                return;
            }
            return;
        }
        if(isSettingLocationUpdates) locationManager.removeUpdates(this);
    }

    @Override
    public void requestUpdateLocation(int grantResult) {
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 30, 0, this);
        }else
            Toast.makeText(mContext, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestRemoveUpdateLocation(int grantResult) {
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }else
            Toast.makeText(mContext, "ACCESS_FINE_CANCEL_LOCATION Denied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                if(myLocation == null){
                    Toast.makeText(mContext, "Пожалуйста, подождите, пока приложение найдёт Вас", Toast.LENGTH_SHORT).show();
                }else {
                    viewHolder.btnSave.setEnabled(false);
                    GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            returnResult(bitmap);
                        }
                    };
                    viewHolder.googleMap.snapshot(callback);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        myLocation = location;

        view.displayGeoLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
