package com.rocketbank.rocketbank.MVP.Presenter;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Egor on 09/11/15.
 */
public interface MainActivityPresenter {

    void onCreate(Bundle savedInstanceState);

    boolean onBackPressed();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
