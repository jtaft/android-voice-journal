package com.jacktaft.selftherapy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

interface ArchiveInterface {
    void deleteView(String fileName);
    void setLongPressed(String oldFileName, RecordingMenuItemView viewItem);
    void updateLongPressedFile();
}
public class ArchiveAdapter extends BaseAdapter implements ArchiveInterface {

    Context context;
    ArrayList<String> items;
    private static LayoutInflater inflater = null;
    String TAG = "ArchiveAdapter";
    boolean isLongPress;
    boolean shouldDelete;
    private static LongPressedHandler longPressedHandler;

    public ArchiveAdapter(Context context, String[] items) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.items = new ArrayList<String>(Arrays.asList(items));
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isLongPress = false;
        shouldDelete = false;
    }

    public void deleteView(String fileName) {
        items.remove(fileName);
        ArchiveAdapter.this.notifyDataSetChanged();
    }

    @Override
    public void setLongPressed(String oldFileName, RecordingMenuItemView viewItem) {
        longPressedHandler = new LongPressedHandler(context, oldFileName, viewItem);
    }

    @Override
    public void updateLongPressedFile() {
        if (longPressedHandler != null) {
            String newFileName = longPressedHandler.viewItem.getText() != null ? longPressedHandler.viewItem.getText().toString() : "";
            longPressedHandler.updateFileName(newFileName);
            items.remove(longPressedHandler.oldFileName);
            items.add(longPressedHandler.newFileName);
            longPressedHandler = null;
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            FrameLayout vi = (FrameLayout) inflater.inflate(R.layout.archive_list_view, null);
            RecordingMenuItemView text = vi.findViewById(R.id.label);
            text.Initialize(this, vi, items.get(position));
            vi.setTag(text);
            return vi;
        } else {
            RecordingMenuItemView text = (RecordingMenuItemView) convertView.getTag();
            if (!items.get(position).equals(text.getText().toString())) {
                text.setText(items.get(position));
                text.setSelection(text.getText().toString().length());
            }
            return convertView;
        }
    }

}
