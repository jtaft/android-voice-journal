package com.jacktaft.selftherapy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.security.Key;

import static android.content.ContentValues.TAG;


public class RecordingMenuItemView extends android.support.v7.widget.AppCompatEditText {
    Context context;
    private static LayoutInflater inflater = null;
    String TAG = "RecordingMenuItemView";
    static boolean isLongPress;
    boolean shouldDelete;
    Runnable longPressRunnable;
    Runnable deleteRunnable;
    ConstraintLayout deleteView;
    AnimatorListenerAdapter deleteAnimationEndListener;

    ValueAnimator deleteViewAnimation;
    String oldFileName;
    ArchiveInterface archiveInterface;
    RecordingMenuItemView self;
    boolean isHideKeyboardClick;


    public RecordingMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.shouldDelete = false;
        this.self = this;
    }
    public void Initialize(ArchiveInterface archiveInterface, final FrameLayout vi, String itemText) {
        this.archiveInterface = archiveInterface;
        this.longPressRunnable = longPressRunnable();
        this.deleteView = vi.findViewById(R.id.delete_view);
        this.deleteAnimationEndListener = deleteAnimationEndListener(deleteView);
        this.deleteRunnable = deleteRunnable(this.deleteView, vi, deleteAnimationEndListener);
        this.setText(itemText);
        isHideKeyboardClick = false;
    }

    private Runnable longPressRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                isLongPress = true;
                self.setBackgroundColor(Color.parseColor("#5c5c5c"));
            }
        };
    }
    private AnimatorListenerAdapter deleteAnimationEndListener(final ConstraintLayout deleteView){
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animator){
                shouldDelete = false;
                removeDeleteAnimation();
            }
            @Override
            public void onAnimationPause(Animator animator){
                shouldDelete = false;
                removeDeleteAnimation();
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if (deleteViewAnimation.getDuration() > 1000) {
                    shouldDelete = true;
                    deleteView.setBackgroundColor(Color.parseColor("#8B0000"));
                }
            }
        };
    }
    private Runnable deleteRunnable(final ConstraintLayout deleteView, final FrameLayout vi, final AnimatorListenerAdapter deleteAnimationEndListener){
        return new Runnable() {
            @Override
            public void run() {
                deleteView.setVisibility(View.VISIBLE);
                deleteView.bringToFront();

                if (deleteViewAnimation == null) {
                    deleteViewAnimation = ValueAnimator.ofInt(deleteView.getMeasuredWidth(), vi.getWidth());
                }
                deleteViewAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = deleteView.getLayoutParams();
                        layoutParams.width = val;
                        deleteView.setLayoutParams(layoutParams);
                    }
                });
                deleteViewAnimation.addListener(deleteAnimationEndListener);
                deleteViewAnimation.setDuration(3000);
                deleteViewAnimation.start();
                self.setBackgroundColor(Color.parseColor("#00000000"));
            }
        };
    }
    public void removeDeleteAnimation() {
        this.removeCallbacks(longPressRunnable);
        this.removeCallbacks(deleteRunnable);
        if (deleteViewAnimation != null){
            deleteViewAnimation.removeAllUpdateListeners();
            deleteViewAnimation.removeListener(deleteAnimationEndListener);
            deleteViewAnimation.cancel();
            deleteView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            shouldDelete = false;
            if (isLongPress) {
                isLongPress = false;
                archiveInterface.updateLongPressedFile();
            }
            this.getRootView().clearFocus();
            if (KeyboardHandler.isShown()){
                KeyboardHandler.hideKeyboard();
                isHideKeyboardClick = true;
            }
            this.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
            this.postDelayed(deleteRunnable, 2000);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            removeDeleteAnimation();
            if (shouldDelete){
                if (this.getText() != null) {
                    MediaHandler.deleteSavedRecording(context, this.getText().toString());
                    archiveInterface.deleteView(this.getText().toString());
                }
                return true;
            } else if (isLongPress) {
                this.setBackgroundColor(Color.parseColor("#00000000"));
                oldFileName = String.valueOf(this.getText() != null ? this.getText().toString(): "");
                this.setSelection(oldFileName.length());
                this.requestFocus();
                archiveInterface.setLongPressed(oldFileName, this);
                KeyboardHandler.showKeyboard(context, this.getWindowToken());
                return true;
            } else {
                if (isHideKeyboardClick) {
                    isHideKeyboardClick = false;
                    return true;
                }
                Intent playIntent = new Intent(context, PlayRecordingActivity.class);
                playIntent.putExtra(PlayRecordingActivity.INTENT_EXTRA_FILE_NAME, this.getText().toString());
                context.startActivity(playIntent);
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL){
            removeDeleteAnimation();
        }
        return true;
    }
}
