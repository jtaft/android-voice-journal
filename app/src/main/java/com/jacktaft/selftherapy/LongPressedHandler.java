package com.jacktaft.selftherapy;

import android.content.Context;

public class LongPressedHandler {
    String oldFileName;
    Context context;
    RecordingMenuItemView viewItem;
    String newFileName;

    public LongPressedHandler(Context context, String oldFileName, RecordingMenuItemView viewItem) {
        this.context = context;
        this.oldFileName = oldFileName;
        this.viewItem = viewItem;
    }

    public void updateFileName(String newFileName) {
        newFileName = newFileName.replace(" ","_");
        newFileName = newFileName.replaceAll("[^A-Za-z0-9_-]","");
        MediaHandler.renameRecording(context, oldFileName, newFileName);
        this.newFileName = newFileName;
    }
}
