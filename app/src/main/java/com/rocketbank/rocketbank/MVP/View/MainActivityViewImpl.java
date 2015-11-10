package com.rocketbank.rocketbank.MVP.View;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.rocketbank.rocketbank.adapters.ChatAdapter;
import com.rocketbank.rocketbank.adapters.DividerItemDecoration;
import com.rocketbank.rocketbank.MVP.View.ViewHolder.MainActivityViewHolder;
import com.rocketbank.rocketbank.R;
import com.rocketbank.rocketbank.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Egor on 09/11/15.
 */
public class MainActivityViewImpl implements MainActivityView, TextWatcher{

    private Activity activity;
    private Context mContext;

    private static final String TRANSLATION_Y = "translationY";
    private static final String ALPHA = "alpha";

    public boolean isExpand = false, textContainerisShown = false;
    private boolean animatedTextContainer = false;

    private float offset, offset1, offset2, offset3, offSet4;


    private final MainActivityViewHolder viewHolder;

    public MainActivityViewImpl(Activity activity, MainActivityViewHolder viewHolder) {
        this.activity = activity;
        this.mContext = this.activity.getApplicationContext();
        this.viewHolder = viewHolder;
    }

    @Override
    public void onPreDraw(ViewTreeObserver.OnPreDrawListener listener) {
        viewHolder.fabContainer.getViewTreeObserver().removeOnPreDrawListener(listener);
        offset = viewHolder.fabAdd.getY();
        viewHolder.fabAdd.setAlpha(0.64f);
        offset1 = viewHolder.fabAdd.getY() - viewHolder.fabText.getY();
        viewHolder.fabText.setTranslationY(offset1);
        offset2 = viewHolder.fabAdd.getY() - viewHolder.fabCamera.getY();
        viewHolder.fabCamera.setTranslationY(offset2);
        offset3 = viewHolder.fabAdd.getY() - viewHolder.fabAlbum.getY();
        viewHolder.fabAlbum.setTranslationY(offset3) ;
        offSet4 = viewHolder.fabAdd.getY() - viewHolder.fabGeo.getY();
        viewHolder.fabGeo.setTranslationY(offSet4);
        viewHolder.tvText.setAlpha(0);
        viewHolder.tvCamera.setAlpha(0);
        viewHolder.tvAlbum.setAlpha(0);
        viewHolder.tvGeo.setAlpha(0);
        viewHolder.fabText.setAlpha(0f);
        viewHolder.fabCamera.setAlpha(0f);
        viewHolder.fabAlbum.setAlpha(0f);
        viewHolder.fabGeo.setAlpha(0f);
        viewHolder.imgMask.setClickable(false);
        Log.d("y ll-text", String.valueOf(viewHolder.textContainer.getY()));
        Log.d("height of ll-text", String.valueOf(viewHolder.textContainer.getHeight()));
        viewHolder.textContainer.setTranslationY(viewHolder.textContainer.getY());
//                textContainer.setVisibility(View.GONE);
        Log.d("translation ll-text after", String.valueOf(viewHolder.textContainer.getTranslationY()));
    }

    @Override
    public void setClickListeners(View.OnClickListener listener) {
        viewHolder.btnSend.setOnClickListener(listener);
        viewHolder.imgMask.setOnClickListener(listener);
        viewHolder.fabAdd.setOnClickListener(listener);
        viewHolder.fabText.setOnClickListener(listener);
        viewHolder.fabCamera.setOnClickListener(listener);
        viewHolder.fabAlbum.setOnClickListener(listener);
        viewHolder.fabGeo.setOnClickListener(listener);
    }

    @Override
    public void setEditTextChanged() {
        viewHolder.etMsg.addTextChangedListener(this);
    }

    @Override
    public void setSettingsOnRecyclerView() {
        viewHolder.rvChat.setHasFixedSize(true);
        viewHolder.rvChat.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        viewHolder.rvChat.setLayoutManager(mLayoutManager);
        viewHolder.rvChat.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void setAdapter(ArrayList<ChatMessage> array, View.OnClickListener listener) {
        viewHolder.rvChat.setAdapter(new ChatAdapter(mContext, array, listener));
    }

    @Override
    public void clearEditText() {
        viewHolder.etMsg.setText("");
        viewHolder.etMsg.clearFocus();
    }

    @Override
    public void sendTextMessage() {
        viewHolder.etMsg.setText("");
        viewHolder.etMsg.clearFocus();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        showFabs();

        viewHolder.rvChat.getAdapter().notifyDataSetChanged();
        viewHolder.rvChat.scrollToPosition(0);

    }

    @Override
    public void sendImgMessage() {
        viewHolder.rvChat.getAdapter().notifyDataSetChanged();
        viewHolder.rvChat.scrollToPosition(0);
    }

    @Override
    public void sendGeoMessage() {
        viewHolder.rvChat.getAdapter().notifyDataSetChanged();
        viewHolder.rvChat.scrollToPosition(0);
    }

    @Override
    public void collapseFab() {
        viewHolder.fabAdd.setImageResource(R.drawable.add);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(viewHolder.fabText, offset1),
                    createCollapseAnimator(viewHolder.fabCamera, offset2),
                    createCollapseAnimator(viewHolder.fabAlbum, offset3),
                    createCollapseAnimator(viewHolder.fabGeo, offSet4),
                ObjectAnimator.ofFloat(viewHolder.fabAdd, ALPHA, 1f, 0.64f)
                            .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime)),
                    downAlphaAnimator(viewHolder.fabText),
                    downAlphaAnimator(viewHolder.fabAlbum),
                    downAlphaAnimator(viewHolder.fabCamera),
                    downAlphaAnimator(viewHolder.fabGeo),
                    downAlphaAnimator(viewHolder.tvText),
                    downAlphaAnimator(viewHolder.tvCamera),
                    downAlphaAnimator(viewHolder.tvAlbum),
                    downAlphaAnimator(viewHolder.tvGeo),
                    downAlphaAnimator(viewHolder.imgMask));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewHolder.fabAdd.setAlpha(0.64f);
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

    @Override
    public void expandFab() {
        viewHolder.fabAdd.setAlpha(1f);
        viewHolder.fabAdd.setImageResource(R.drawable.close_48);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(viewHolder.fabText, offset1),
                createExpandAnimator(viewHolder.fabCamera, offset2),
                createExpandAnimator(viewHolder.fabAlbum, offset3),
                createExpandAnimator(viewHolder.fabGeo, offSet4),
                ObjectAnimator.ofFloat(viewHolder.fabAdd, ALPHA, 0.64f, 1f)
                        .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime)),
                upAlphaAnimator(viewHolder.fabText),
                upAlphaAnimator(viewHolder.fabAlbum),
                upAlphaAnimator(viewHolder.fabCamera),
                upAlphaAnimator(viewHolder.fabGeo),
                upAlphaAnimator(viewHolder.imgMask));
        final AnimatorSet[] animatorSetTv = new AnimatorSet[1];
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetTv[0] = new AnimatorSet();
                animatorSetTv[0].playTogether(upAlphaAnimator(viewHolder.tvText),
                        upAlphaAnimator(viewHolder.tvCamera),
                        upAlphaAnimator(viewHolder.tvAlbum),
                        upAlphaAnimator(viewHolder.tvGeo));
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

    @Override
    public void hideFabs() {
        isExpand = !isExpand;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(viewHolder.fabText, offset1 + offset),
                createCollapseAnimator(viewHolder.fabCamera, offset2 + offset),
                createCollapseAnimator(viewHolder.fabAlbum, offset3 + offset),
                createCollapseAnimator(viewHolder.fabGeo, offSet4 + offset),
                createCollapseAnimator(viewHolder.fabAdd, offset),
                downAlphaAnimator(viewHolder.tvText),
                downAlphaAnimator(viewHolder.tvCamera),
                downAlphaAnimator(viewHolder.tvAlbum),
                downAlphaAnimator(viewHolder.tvGeo),
                downAlphaAnimator(viewHolder.imgMask));
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
                        viewHolder.textContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textContainerisShown = true;
                        animatedTextContainer = false;
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewHolder.rvChat.getLayoutParams();
                        Log.d("height = ", String.valueOf(((ViewGroup.LayoutParams) viewHolder.textContainer.getLayoutParams()).height));
                        lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, 120);

                        viewHolder.rvChat.requestLayout();
                        viewHolder.rvChat.scrollToPosition(0);

                        viewHolder.etMsg.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(viewHolder.etMsg, InputMethodManager.SHOW_IMPLICIT);//TODO
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

    @Override
    public void showFabs() {
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

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewHolder.rvChat.getLayoutParams();
                Log.d("height = ", String.valueOf(((ViewGroup.LayoutParams)viewHolder.textContainer.getLayoutParams()).height));
                lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, 0);

                viewHolder.rvChat.requestLayout();
                viewHolder.rvChat.scrollToPosition(0);

                viewHolder.fabAdd.setImageResource(R.drawable.add);
                AnimatorSet newAnimatorSet = new AnimatorSet();
                viewHolder.fabAdd.setAlpha(0.64f);
                newAnimatorSet.playTogether(createExpandAnimator(viewHolder.fabAdd, offset));
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

    @Override
    public void changeIsExpand() {
        isExpand = !isExpand;
    }

    @Override
    public boolean getIsExpand() {
        return isExpand;
    }

    @Override
    public boolean getTextContainerIsShown() {
        return textContainerisShown;
    }

    @Override
    public Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    @Override
    public Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    @Override
    public Animator downAlphaAnimator(View view) {
        return ObjectAnimator.ofFloat(view, ALPHA, 1, 0)
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public Animator upAlphaAnimator(View view) {
        return ObjectAnimator.ofFloat(view, ALPHA, 0, 1)
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public Animator showTextContainer() {
        return ObjectAnimator.ofFloat(viewHolder.textContainer, TRANSLATION_Y, viewHolder.textContainer.getY(), 0)
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public Animator hideTextContainer() {
        return ObjectAnimator.ofFloat(viewHolder.textContainer, TRANSLATION_Y, 0, viewHolder.textContainer.getY())
                .setDuration(mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().trim().equals(""))
            viewHolder.btnSend.setImageResource(R.drawable.send_enabled);
        else
            viewHolder.btnSend.setImageResource(R.drawable.send_disabled);
    }
}
