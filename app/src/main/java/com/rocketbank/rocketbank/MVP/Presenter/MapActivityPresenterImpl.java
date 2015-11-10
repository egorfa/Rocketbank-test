package com.rocketbank.rocketbank.MVP.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rocketbank.rocketbank.MVP.Model.MapActivityModel;
import com.rocketbank.rocketbank.MVP.View.MapActivityView;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MapActivityViewHolder;

/**
 * Created by Egor on 10/11/15.
 */
public class MapActivityPresenterImpl implements MapActivityPresenter, OnMapReadyCallback{

    private Activity activity;
    private Context mContext;

    private final MapActivityModel model;
    private final MapActivityView view;
    private final MapActivityViewHolder viewHolder;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 51;
    final private int REQUEST_CODE_ASK_CANCEL_PERMISSIONS = 52;

    public MapActivityPresenterImpl(Activity activity, MapActivityModel model, MapActivityView view, MapActivityViewHolder viewHolder) {
        this.activity = activity;
        mContext = activity.getApplicationContext();
        this.model = model;
        this.view = view;
        this.viewHolder = viewHolder;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        model.setSettingsOnMap(this);
        model.setClickListeners();

        view.btnSaveSetTouchSettings();
    }

    @Override
    public void onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        model.cancelLocationManager();
        activity.finish();
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                model.requestUpdateLocation(grantResults[0]);
                break;
            case REQUEST_CODE_ASK_CANCEL_PERMISSIONS:
                model.requestRemoveUpdateLocation(grantResults[0]);

                break;
            default:
                return true;
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        viewHolder.googleMap = googleMap;

        Intent intent = activity.getIntent();
        Boolean needResult = intent.getBooleanExtra("needResult", false);

        if(needResult){
            viewHolder.btnSave.setVisibility(View.VISIBLE);
            model.setLocationManager();
        }else{
            viewHolder.btnSave.setVisibility(View.INVISIBLE);
            view.displayGeoLocation(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0));
        }
    }
}
