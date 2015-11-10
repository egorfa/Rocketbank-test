package com.rocketbank.rocketbank.MVP.View;

import android.animation.Animator;
import android.view.View;
import android.view.ViewTreeObserver;

import com.rocketbank.rocketbank.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Egor on 09/11/15.
 */
public interface MainActivityView {

    void onPreDraw(ViewTreeObserver.OnPreDrawListener listener);

    void setClickListeners(View.OnClickListener listener);

    void setEditTextChanged();

    void setSettingsOnRecyclerView();

    void setAdapter(ArrayList<ChatMessage> array, View.OnClickListener listener);

    void clearEditText();

    void sendTextMessage();

    void sendImgMessage();

    void sendGeoMessage();

    void collapseFab();

    void expandFab();

    void hideFabs();

    void showFabs();

    void changeIsExpand();

    boolean getIsExpand();

    boolean getTextContainerIsShown();

    Animator createCollapseAnimator(View view, float offset);

    Animator createExpandAnimator(View view, float offset);

    Animator downAlphaAnimator(View view);

    Animator upAlphaAnimator(View view);

    Animator showTextContainer();

    Animator hideTextContainer();

}
