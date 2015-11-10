package com.rocketbank.rocketbank.MVP.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.rocketbank.rocketbank.MVP.Activity.MapActivity;
import com.rocketbank.rocketbank.MVP.Model.MainActivityModel;
import com.rocketbank.rocketbank.MVP.View.MainActivityView;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MainActivityViewHolder;
import com.rocketbank.rocketbank.R;
import com.rocketbank.rocketbank.model.ChatMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egor on 09/11/15.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter, View.OnClickListener {

    private Context mContext;
    private Activity activity;
    private MainActivityViewHolder viewHolder;


    private ArrayList<ChatMessage> array;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GEO_LOCATION = 2;
    static final int PICK_IMAGE_REQUEST = 3;

    private final MainActivityModel model;
    private final MainActivityView view;

    public MainActivityPresenterImpl(Activity activity ,MainActivityModel model, MainActivityView view, MainActivityViewHolder viewHolder, ArrayList<ChatMessage> array) {
        this.activity = activity;
        mContext = activity.getApplicationContext();
        this.model = model;
        this.view = view;
        view.setClickListeners(this);
        this.viewHolder = viewHolder;
        this.array = array;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        view.setEditTextChanged();
        view.setClickListeners(this);

        viewHolder.fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.onPreDraw(this);
                return true;
            }
        });

        view.setAdapter(array, this);

        model.getChatMessages(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                viewHolder.rvChat.getAdapter().notifyDataSetChanged();
                viewHolder.rvChat.scrollToPosition(0);
            }
        });
        view.setSettingsOnRecyclerView();


    }

    @Override
    public boolean onBackPressed() {
        if(view.getTextContainerIsShown()) {
            view.showFabs();
        }
        if(view.getIsExpand()) viewHolder.fabAdd.callOnClick();
        return !view.getTextContainerIsShown() && !view.getIsExpand();
//            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GEO_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Bitmap bitmap = data.getParcelableExtra("bitmap");
                        Location loc = data.getParcelableExtra("location");

                        model.addGeoMessage(bitmap, loc);
                        view.sendGeoMessage();
                        break;
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        model.addImgMessage(imageBitmap);
                        view.sendImgMessage();
                        break;
                }
                break;
            case PICK_IMAGE_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri uri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);

                            model.addImgMessage(bitmap);
                            view.sendImgMessage();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img_mask:
                viewHolder.fabAdd.callOnClick();
                break;
            case R.id.btn_send:
                if (viewHolder.etMsg.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, "Пустые сообщения никому не интересны", Toast.LENGTH_SHORT).show();
                } else {
                    model.addTextMessage(viewHolder.etMsg.getText().toString().trim());
                    view.sendTextMessage();

                }
                break;
            case R.id.fab_add:
                viewHolder.fabAdd.setEnabled(false);
                viewHolder.fabAdd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.fabAdd.setEnabled(true);
                    }
                }, 600);
                view.changeIsExpand();
                if (view.getIsExpand()) {
                    viewHolder.imgMask.setClickable(true);
                    view.expandFab();
                } else {
                    viewHolder.imgMask.setClickable(false);
                    view.collapseFab();
                }
                break;
            case R.id.fab_text:
                view.hideFabs();
                viewHolder.imgMask.setClickable(false);
                viewHolder.etMsg.setFocusableInTouchMode(true);
                viewHolder.etMsg.requestFocus();
                break;
            case R.id.fab_camera:
                view.changeIsExpand();
                viewHolder.imgMask.setClickable(false);
                view.collapseFab();

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intentCamera.resolveActivity(mContext.getPackageManager())!= null){
                    activity.startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.fab_album:
                view.changeIsExpand();
                viewHolder.imgMask.setClickable(false);
                view.collapseFab();

                Intent intentAlbum = new Intent();
// Show only images, no videos or anything else
                intentAlbum.setType("image/*");
                intentAlbum.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                activity.startActivityForResult(Intent.createChooser(intentAlbum, "Выберите фото"), PICK_IMAGE_REQUEST);
                break;
            case R.id.fab_geo:
                Intent intentGeo = new Intent(mContext, MapActivity.class);
                intentGeo.putExtra("needResult", true);
                activity.startActivityForResult(intentGeo, REQUEST_GEO_LOCATION);

                view.changeIsExpand();
                viewHolder.imgMask.setClickable(false);
                view.collapseFab();
                break;
            case R.id.cell_message:
                int position = viewHolder.rvChat.getChildLayoutPosition(v);
                ChatMessage message = array.get(position);
                switch(array.get(position).getType()){
                    case ImageMesage:
                        Intent intent = new Intent(mContext, com.rocketbank.rocketbank.MVP.Activity.ImageActivity.class);
                        intent.putExtra("image", message.getBmp());
                        activity.startActivity(intent);
                        break;
                    case GeoLocationMessage:
                        Intent intent2 = new Intent(mContext, com.rocketbank.rocketbank.MVP.Activity.MapActivity.class);
                        intent2.putExtra("needResult", false);
                        intent2.putExtra("latitude", message.getLatitude());
                        intent2.putExtra("longitude", message.getLongitude());
                        activity.startActivity(intent2);
                        break;
                }
                break;

        }
    }
}
