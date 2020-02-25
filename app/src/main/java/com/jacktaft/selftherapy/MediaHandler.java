package com.jacktaft.selftherapy;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Integer.parseInt;
import static java.util.Locale.*;

public class MediaHandler {
    private static String fileName = new SimpleDateFormat("MM-dd-yyyy", getDefault()).format(new Date()) + ".3gp";
    private static final String LOG_TAG = "MediaHandler";
    private static MediaPlayer player = null;
    private static MediaRecorder recorder = null;
    public static void startPlaying(Context context, String playFileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(context.getFilesDir().getAbsolutePath() + "/" + playFileName + ".3gp");
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public static void pausePlaying() {
        if (player != null) {
            player.pause();
        }
    }

    public static void resumePlaying() {
        if (player != null) {
            player.start();
        }
    }

    public static void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public static void startRecording(Context context) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(context.getCacheDir().getAbsolutePath() + "/" + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    public static void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    private static String getNewFileName(File[] files, String testFileName) {
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName().replace(".3gp", "");
            if (fileName.equals(testFileName)) {
                String[] splitString = testFileName.split("[(]");
                String[] fileNameNoNumberArray = Arrays.copyOf(splitString, splitString.length > 1 ? splitString.length - 1 : 1);
                String fileNameNoNumber = TextUtils.join("(", fileNameNoNumberArray);
                int newNum = Integer.parseInt(splitString.length > 1 ? splitString[splitString.length - 1].replace(")", "") : "0") + 1;
                return getNewFileName(files, fileNameNoNumber + "(" + String.valueOf(newNum) + ")");
            }
        }
        return testFileName;
    }

    public static String saveRecording(Context context) {
        String newFileName = getNewFileName((new File(context.getFilesDir().getAbsolutePath() + "/").listFiles()), fileName.replace(".3gp",""));
        File fileDest = new File(context.getFilesDir().getAbsolutePath() + "/" + newFileName + ".3gp");
        File file = new File(context.getCacheDir().getAbsolutePath() + "/" + fileName);
        file.renameTo(fileDest);
        file.delete();
        return fileDest.getName().replace(".3gp","");
    }

    public static String renameRecording(Context context, String oldName, String newName) {
        if (oldName == null || newName == null) {
            return null;
        } else if (!oldName.equals(newName) && !newName.equals("")) {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/" + oldName + ".3gp");
            File fileDest = new File(context.getFilesDir().getAbsolutePath() + "/" + newName + ".3gp");
            file.renameTo(fileDest);
            file.delete();
            return fileDest.getName();
        }
        return oldName + ".3gp";
    }

    public static void deleteRecording(Context context) {
        new File(context.getCacheDir().getAbsolutePath() + "/" + fileName).delete();
    }

    public static void deleteSavedRecording(Context context, String deleteFile) {
        new File(context.getFilesDir().getAbsolutePath() + "/" + deleteFile + ".3gp").delete();
    }
}
