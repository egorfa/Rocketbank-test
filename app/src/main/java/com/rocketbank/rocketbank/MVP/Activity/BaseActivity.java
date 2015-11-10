package com.rocketbank.rocketbank.MVP.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Egor on 09/11/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceIdentifier());
        loadComponents();
    }

    private void loadComponents(){
        mContext = getApplicationContext();
    }

    protected abstract int getLayoutResourceIdentifier();
}
