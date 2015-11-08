package com.rocketbank.rocketbank;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rocketbank.rocketbank.model.ChatMessage;
import com.rocketbank.rocketbank.model.TypeFrom;
import com.rocketbank.rocketbank.model.TypeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewGroup fabContainer;
    private LinearLayout textContainer;
    private FloatingActionButton fabAdd, fabText, fabCamera, fabAlbum, fabGeo;
    private RecyclerView rvChat;
    private ImageView imgMask;
    private TextView tvText, tvCamera, tvAlbum, tvGeo;
    private EditText etMsg;
    private ImageButton btnSend;

    private ArrayList<ChatMessage> array;

    private boolean isExpand = false, textContainerisShown = false;
    private boolean animatedTextContainer = false;

    private float offset, offset1, offset2, offset3, offSet4;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GEO_LOCATION = 2;
    static final int PICK_IMAGE_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabContainer =(ViewGroup) findViewById(R.id.fab_container);
        textContainer = (LinearLayout) findViewById(R.id.ll_text);

        rvChat = (RecyclerView) findViewById(R.id.rvChat);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabText = (FloatingActionButton) findViewById(R.id.fab_text);
        fabCamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fabAlbum = (FloatingActionButton) findViewById(R.id.fab_album);
        fabGeo = (FloatingActionButton) findViewById(R.id.fab_geo);

        tvGeo = (TextView) findViewById(R.id.tv_geo);
        tvText = (TextView) findViewById(R.id.tv_text);
        tvCamera = (TextView) findViewById(R.id.tv_camera);
        tvAlbum = (TextView) findViewById(R.id.tv_album);

        imgMask = (ImageView) findViewById(R.id.img_mask);


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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMsg.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Пустые сообщения никому не интересны", Toast.LENGTH_SHORT).show();
                } else {

                    Date date = new Date();
                    Calendar c = GregorianCalendar.getInstance();
                    c.setTime(date);
                    final ChatMessage message = new ChatMessage(c.getTime().toString());
                    message.setType(TypeMessage.TextMessage);
                    message.setText(etMsg.getText().toString().trim());
                    message.saveInBackground();
                    message.setDate(new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000));//c.get(Calendar.DAY_OF_MONTH) + " " + String.format(Locale.US,"%tB",c)));
                    message.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                    message.setFrom(TypeFrom.Local);


                    etMsg.setText("");
                    etMsg.clearFocus();
                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    showFabs();
                    array.add(0, message);

                    rvChat.getAdapter().notifyDataSetChanged();
                    rvChat.scrollToPosition(0);
                }
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
                if (isExpand) {
                    imgMask.setClickable(true);
                    expandFab();
                    imgMask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fabAdd.callOnClick();
                            imgMask.setClickable(false);
                        }
                    });
                } else {
                    imgMask.setClickable(false);
                    collapseFab();
                }
            }
        });

        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllFabs();
                imgMask.setClickable(false);
                etMsg.setFocusableInTouchMode(true);
                etMsg.requestFocus();

            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                imgMask.setClickable(false);
                collapseFab();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        fabAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                imgMask.setClickable(false);
                collapseFab();

                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
            }
        });

        fabGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("needResult", true);
                startActivityForResult(intent, REQUEST_GEO_LOCATION);

                isExpand = !isExpand;
                imgMask.setClickable(false);
                collapseFab();
            }
        });




        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset = fabAdd.getY();
                fabAdd.setAlpha(0.64f);
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
                fabText.setAlpha(0f);
                fabCamera.setAlpha(0f);
                fabAlbum.setAlpha(0f);
                fabGeo.setAlpha(0f);
                Log.d("y ll-text", String.valueOf(textContainer.getY()));
                Log.d("height of ll-text", String.valueOf(textContainer.getHeight()));
                textContainer.setTranslationY(textContainer.getY());
//                textContainer.setVisibility(View.GONE);
                Log.d("translation ll-text after", String.valueOf(textContainer.getTranslationY()));
                return true;
            }
        });

        rvChat.setHasFixedSize(true);
        rvChat.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(mLayoutManager);
        rvChat.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        array = new ArrayList<>();
        rvChat.setAdapter(new ChatAdapter(MainActivity.this, array, MainActivity.this));


        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessage");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> markers, ParseException e) {

                if (e == null) {
                    // your logic here

                    for (int i = markers.size()-1; i>=0; i-- ) {
                        ChatMessage message = (ChatMessage) markers.get(i);
                        message.setFrom(TypeFrom.Server);
                        array.add(message);
                        rvChat.getAdapter().notifyDataSetChanged();
                        rvChat.scrollToPosition(0);
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Ошибка при загрузке сообщений, попробуйте позднее", Toast.LENGTH_LONG).show();
                }
            }
        });

//        query.getInBackground("oxSVjhdj6F", new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    // object will be your game score
//                    array.add((GeoMessage) object);
//                    rvChat.setAdapter(new ChatAdapter(MainActivity.this, array));
//                    rvChat.scrollToPosition(0);
//                } else {
//                    // something went wrong
//                }
//            }
//        });




    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null) {return;}
//        Bitmap bitmap = data.getParcelableExtra("bitmap");
        switch(requestCode){
                case REQUEST_GEO_LOCATION:
                    switch (resultCode){
                        case RESULT_OK:
                        Bitmap bitmap = data.getParcelableExtra("bitmap");
                        Location loc = data.getParcelableExtra("location");

                            Date date = new Date();
                            Calendar c = GregorianCalendar.getInstance();
                            c.setTime(date);
                            final ChatMessage message = new ChatMessage();
                            message.setType(TypeMessage.GeoLocationMessage);
                            message.setLatitude(loc.getLatitude());
                            message.setLongitude(loc.getLongitude());
                            message.setBmp(bitmapToByteArray(bitmap));
                            message.saveInBackground();

                            message.setDate(date);
                            message.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                            message.setFrom(TypeFrom.Local);

                        array.add(0, message);

                        rvChat.getAdapter().notifyDataSetChanged();
                        rvChat.scrollToPosition(0);
                        break;
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    switch(resultCode) {
                        case RESULT_OK:
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");

                            Date date = new Date();
                            Calendar c = GregorianCalendar.getInstance();
                            c.setTime(date);

                            final ChatMessage imgMessage = new ChatMessage();
                            imgMessage.setType(TypeMessage.ImageMesage);
                            imgMessage.setBmp(bitmapToByteArray(imageBitmap));
                            imgMessage.saveInBackground();

                            imgMessage.setDate(date);
                            imgMessage.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                            imgMessage.setFrom(TypeFrom.Local);

                            array.add(0, imgMessage);

                            rvChat.getAdapter().notifyDataSetChanged();
                            rvChat.scrollToPosition(0);
                            break;
                    }
                    break;
                case PICK_IMAGE_REQUEST:
                    switch(resultCode){
                        case RESULT_OK:
                            Uri uri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                // Log.d(TAG, String.valueOf(bitmap));
                                Date date = new Date();
                                Calendar c = GregorianCalendar.getInstance();
                                c.setTime(date);

                                final ChatMessage imgMessage = new ChatMessage();
                                imgMessage.setType(TypeMessage.ImageMesage);
                                imgMessage.setBmp(bitmapToByteArray(bitmap));
                                imgMessage.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.d("success", " save");
                                    }
                                });

                                imgMessage.setDate(date);
                                imgMessage.setTime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                                imgMessage.setFrom(TypeFrom.Local);

                                array.add(0, imgMessage);

                                rvChat.getAdapter().notifyDataSetChanged();
                                rvChat.scrollToPosition(0);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
        }
    }

    private void collapseFab(){
        fabAdd.setImageResource(R.drawable.add);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabText, offset1),
                createCollapseAnimator(fabCamera, offset2),
                createCollapseAnimator(fabAlbum, offset3),
                createCollapseAnimator(fabGeo, offSet4),
                ObjectAnimator.ofFloat(fabAdd, ALPHA, 1f, 0.64f)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)),
                downAlphaAnimator(fabText),
                downAlphaAnimator(fabAlbum),
                downAlphaAnimator(fabCamera),
                downAlphaAnimator(fabGeo),
                downAlphaAnimator(tvText),
                downAlphaAnimator(tvCamera),
                downAlphaAnimator(tvAlbum),
                downAlphaAnimator(tvGeo),
                downAlphaAnimator(imgMask));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fabAdd.setAlpha(0.64f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void expandFab() {
        fabAdd.setAlpha(1f);
        fabAdd.setImageResource(R.drawable.close_48);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabText, offset1),
                createExpandAnimator(fabCamera, offset2),
                createExpandAnimator(fabAlbum, offset3),
                createExpandAnimator(fabGeo, offSet4),
                ObjectAnimator.ofFloat(fabAdd, ALPHA, 0.64f, 1f)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)),
                upAlphaAnimator(fabText),
                upAlphaAnimator(fabAlbum),
                upAlphaAnimator(fabCamera),
                upAlphaAnimator(fabGeo),
                upAlphaAnimator(imgMask));
        final AnimatorSet[] animatorSetTv = new AnimatorSet[1];
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetTv[0] = new AnimatorSet();
                animatorSetTv[0].playTogether(upAlphaAnimator(tvText),
                        upAlphaAnimator(tvCamera),
                        upAlphaAnimator(tvAlbum),
                        upAlphaAnimator(tvGeo));
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
        animatorSet.start();
    }

    private void hideAllFabs(){

//        textContainer.setVisibility(View.VISIBLE);
        isExpand = !isExpand;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabText, offset1 + offset),
                createCollapseAnimator(fabCamera, offset2 + offset),
                createCollapseAnimator(fabAlbum, offset3 + offset),
                createCollapseAnimator(fabGeo, offSet4 + offset),
                createCollapseAnimator(fabAdd, offset),
                downAlphaAnimator(tvText),
                downAlphaAnimator(tvCamera),
                downAlphaAnimator(tvAlbum),
                downAlphaAnimator(tvGeo),
                downAlphaAnimator(imgMask));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatedTextContainer = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet animatorTextContainer = new AnimatorSet();
                animatorTextContainer.play(showTextContainer());
                animatorTextContainer.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        textContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textContainerisShown = true;
                        animatedTextContainer = false;
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) rvChat.getLayoutParams();
                        Log.d("height = ", String.valueOf(((ViewGroup.LayoutParams) textContainer.getLayoutParams()).height));
                        lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, 120);

                        rvChat.requestLayout();
                        rvChat.scrollToPosition(0);

                        etMsg.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(etMsg, InputMethodManager.SHOW_IMPLICIT);//TODO
                            }
                        }, 200);


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorTextContainer.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void showFabs(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(hideTextContainer());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatedTextContainer = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textContainerisShown = false;

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) rvChat.getLayoutParams();
                Log.d("height = ", String.valueOf(((ViewGroup.LayoutParams)textContainer.getLayoutParams()).height));
                lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, 0);

                rvChat.requestLayout();
                rvChat.scrollToPosition(0);

                fabAdd.setImageResource(R.drawable.add);
                AnimatorSet newAnimatorSet = new AnimatorSet();
                newAnimatorSet.playTogether(createExpandAnimator(fabAdd, offset));
                newAnimatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animatedTextContainer = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                newAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }

    private static final String TRANSLATION_Y = "translationY";
    private static final String ALPHA = "alpha";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator downAlphaAnimator(View view){
        return ObjectAnimator.ofFloat(view, ALPHA, 1, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator upAlphaAnimator(View view){
        return ObjectAnimator.ofFloat(view, ALPHA, 0, 1)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }


    private Animator showTextContainer(){
        Log.d("translation = ", String.valueOf(textContainer.getY()));
        return ObjectAnimator.ofFloat(textContainer, TRANSLATION_Y, textContainer.getY(), 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator hideTextContainer(){
        return ObjectAnimator.ofFloat(textContainer, TRANSLATION_Y, 0, textContainer.getY())
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public void onBackPressed() {
        if(textContainerisShown) {
            showFabs();
        }
        if(isExpand) fabAdd.callOnClick();
        if(textContainerisShown ==false && isExpand==false)
            super.onBackPressed();

    }

    public byte[] bitmapToByteArray(Bitmap bmp)
    {
        bmp = getResizedBitmap(bmp, 200);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cell_message:
                int position = rvChat.getChildLayoutPosition(v);
                ChatMessage message = array.get(position);
                switch(array.get(position).getType()) {
                    case ImageMesage:
                        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                        intent.putExtra("image", message.getBmp());
                        startActivity(intent);
                        break;
                    case GeoLocationMessage:
                        Intent intent2 = new Intent(MainActivity.this, MapActivity.class);
                        intent2.putExtra("needResult", false);
                        intent2.putExtra("latitude", message.getLatitude());
                        intent2.putExtra("longitude", message.getLongitude());
                        startActivity(intent2);
                        break;
                }

                break;
        }
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
}
