package com.jacktaft.selftherapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


public class RecordingActivity extends AppCompatActivity {
    private static final String LOG_TAG = "RecordingActivity";
    private ImageButton stopButton = null;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.recording_activity);
        stopButton = findViewById(R.id.stopButton);

        MediaHandler.startRecording(getApplicationContext());
        getSupportActionBar().hide();



        stopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(.5f);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                    MediaHandler.stopRecording();
                    MediaHandler.saveRecording(getApplicationContext());
                    Intent saveIntent = new Intent(RecordingActivity.this, ArchiveActivity.class);
                    startActivity(saveIntent);
                    finish();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        MediaHandler.stopRecording();
        MediaHandler.deleteRecording(getApplicationContext());
        finish();
    }
}
