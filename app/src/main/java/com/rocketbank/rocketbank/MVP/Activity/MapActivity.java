package com.rocketbank.rocketbank.MVP.Activity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;
import com.rocketbank.rocketbank.MVP.Model.MapActivityModel;
import com.rocketbank.rocketbank.MVP.Model.MapActivityModelImpl;
import com.rocketbank.rocketbank.MVP.Presenter.MapActivityPresenter;
import com.rocketbank.rocketbank.MVP.Presenter.MapActivityPresenterImpl;
import com.rocketbank.rocketbank.MVP.View.MapActivityView;
import com.rocketbank.rocketbank.MVP.View.MapActivityViewImpl;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MapActivityViewHolder;
import com.rocketbank.rocketbank.R;

/**
 * Created by Egor on 10/11/15.
 */
public class MapActivity extends BaseActivity{

    private MapActivityPresenter presenter;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MapActivityViewHolder viewHolder = new MapActivityViewHolder(
                (MapFragment) getFragmentManager().findFragmentById(R.id.map),
                (Button) findViewById(R.id.btn));

        final MapActivityView view = new MapActivityViewImpl(viewHolder);

        final MapActivityModel model = new MapActivityModelImpl(this, view, viewHolder);

        presenter = new MapActivityPresenterImpl(this, model, view, viewHolder);

        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }
}
