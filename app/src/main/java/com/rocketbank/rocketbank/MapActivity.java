package com.rocketbank.rocketbank;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;

/**
 * Created by Egor on 05/11/15.
 */
public class MapActivity extends Activity implements OnMapReadyCallback, android.location.LocationListener {

    String API_KEY = "AIzaSyAbBo3DHWDyu88vmPVtabXlWqEEeP1JaFg";
    private Button btn;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location myLocation;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 51;
    final private int REQUEST_CODE_ASK_CANCEL_PERMISSIONS = 52;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocation == null){
                    Toast.makeText(MapActivity.this, "Пожалуйста, подождите, пока приложение найдёт Вас", Toast.LENGTH_SHORT).show();
                }else {
                    btn.setEnabled(false);
                    GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                        Bitmap bitmap;

                        @Override
                        public void onSnapshotReady(Bitmap snapshot) {
                            // TODO Auto-generated method stub
                            bitmap = snapshot;
                            try {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                Intent intent = new Intent();
                                intent.putExtra("bitmap", getResizedBitmap(bitmap, 200));
                                intent.putExtra("location", myLocation);
                                setResult(RESULT_OK, intent);
                                cancelLocationManager();
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    googleMap.snapshot(callback);
                }
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn.setAlpha(1f);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn.setAlpha(0.64f);
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

//        LatLng sydney = new LatLng(-33.867, 151.206);
        Intent intent = getIntent();
        Boolean needResult = intent.getBooleanExtra("needResult", false);
        if (needResult) {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

            setLocationManager();


        }else{

        }



        Log.d("loc", "");

//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        cancelLocationManager();
        finish();
    }




    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


//    @Override
//    public void onMyLocationChange(Location location) {
//        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//        Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
//        if (googleMap != null) {
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//        }
//    }


    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        this.myLocation = location;
        LatLng centerOfMap = googleMap.getCameraPosition().target;

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.clear();
            Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

            }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("str", String.valueOf(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("str", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("str", provider);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*30, 0, this);
                    }else
                    Toast.makeText(this, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT).show();
                    break;
            case REQUEST_CODE_ASK_CANCEL_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(MapActivity.this);
                }else
                    Toast.makeText(this, "ACCESS_FINE_CANCEL_LOCATION Denied", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        @Override
        public boolean shouldShowRequestPermissionRationale (@NonNull String permission){
            return super.shouldShowRequestPermissionRationale(permission);
        }

    private void setLocationManager() {
        int hasFineLocationPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOkCancel("You need to allow access to your location",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*30, 0, this);
    }

    private void cancelLocationManager(){
        int hasFineLocationPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOkCancel("You need to allow access to your location",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE_ASK_CANCEL_PERMISSIONS);
                                }
                            });
                    return;
                }
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        locationManager.removeUpdates(MapActivity.this);

    }

    private void showMessageOkCancel(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(MapActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

}