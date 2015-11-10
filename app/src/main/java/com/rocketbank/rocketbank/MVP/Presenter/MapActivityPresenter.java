package com.rocketbank.rocketbank.MVP.Presenter;

import android.os.Bundle;

/**
 * Created by Egor on 10/11/15.
 */
public interface MapActivityPresenter {

    void onCreate(Bundle savedInstanceState);

    void onBackPressed();

    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
