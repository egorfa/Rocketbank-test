package com.rocketbank.rocketbank;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd, fabText, fabCamera, fabAlbum, fabGeo;
    private TextView tvText, tvCamera, tvAlbum, tvGeo;
    private EditText etMsg;
    private ImageButton btnSend;

    private boolean isExpand = false;

    private float offset1, offset2, offset3, offSet4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup fabContainer =(ViewGroup) findViewById(R.id.fab_container);
        final LinearLayout textContainer = (LinearLayout) findViewById(R.id.ll_text);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabText = (FloatingActionButton) findViewById(R.id.fab_text);
        fabCamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fabAlbum = (FloatingActionButton) findViewById(R.id.fab_album);
        fabGeo = (FloatingActionButton) findViewById(R.id.fab_geo);

        tvGeo = (TextView) findViewById(R.id.tv_geo);
        tvText = (TextView) findViewById(R.id.tv_text);
        tvCamera = (TextView) findViewById(R.id.tv_camera);
        tvAlbum = (TextView) findViewById(R.id.tv_album);

        etMsg = (EditText) findViewById(R.id.et_msg);

        btnSend = (ImageButton) findViewById(R.id.btn_send);

        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().equals(""))
                    btnSend.setImageResource(R.drawable.send_enabled);
                else
                        btnSend.setImageResource(R.drawable.send_disabled);

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAdd.setEnabled(false);
                fabAdd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabAdd.setEnabled(true);
                    }
                }, 600);
                isExpand = !isExpand;
                if(isExpand){
                    expandFab();
                }else
                    collapseFab();
            }
        });

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fabAdd.getY() - fabText.getY();
                fabText.setTranslationY(offset1);
                offset2 = fabAdd.getY() - fabCamera.getY();
                fabCamera.setTranslationY(offset2);
                offset3 = fabAdd.getY() - fabAlbum.getY();
                fabAlbum.setTranslationY(offset3);
                offSet4 = fabAdd.getY() - fabGeo.getY();
                fabGeo.setTranslationY(offSet4);
                tvText.setAlpha(0);
                tvCamera.setAlpha(0);
                tvAlbum.setAlpha(0);
                tvGeo.setAlpha(0);
                Log.d("translation ll-text", String.valueOf(textContainer.getTranslationY()));
                Log.d("height of ll-text", String.valueOf(textContainer.getHeight()));
                textContainer.setTranslationY(textContainer.getHeight());
                Log.d("translation ll-text after", String.valueOf(textContainer.getTranslationY()));
                return true;
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    private void collapseFab(){
        fabAdd.setImageResource(R.drawable.add);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabText, offset1),
                createCollapseAnimator(fabCamera, offset2),
                createCollapseAnimator(fabAlbum, offset3),
                createCollapseAnimator(fabGeo, offSet4),
                createCollapseAnimator(tvText),
                createCollapseAnimator(tvCamera),
                createCollapseAnimator(tvAlbum),
                createCollapseAnimator(tvGeo));
        animatorSet.start();
        animateFabAdd();
    }

    private void expandFab() {
        fabAdd.setImageResource(R.drawable.close_48);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabText, offset1),
                createExpandAnimator(fabCamera, offset2),
                createExpandAnimator(fabAlbum, offset3),
                createExpandAnimator(fabGeo, offSet4));
        animatorSet.start();
        final AnimatorSet[] animatorSetTv = new AnimatorSet[1];
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetTv[0] = new AnimatorSet();
                animatorSetTv[0].playTogether(createExpandAnimator(tvText),
                        createExpandAnimator(tvCamera),
                        createExpandAnimator(tvAlbum),
                        createExpandAnimator(tvGeo));
                animatorSetTv[0].start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animatorSetTv[0].cancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animateFabAdd();
    }


    private static final String TRANSLATION_Y = "translationY";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createCollapseAnimator(View view){
        return ObjectAnimator.ofFloat(view, "alpha", 1, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator createExpandAnimator(View view){
        return ObjectAnimator.ofFloat(view, "alpha", 0, 1)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }


    private void animateFabAdd() {
        Drawable drawable = fabAdd.getDrawable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
