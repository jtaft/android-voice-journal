package com.jacktaft.selftherapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;

public class PlayRecordingActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_FILE_NAME = "fileName";
    private Button playbackButton = null;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recording);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playbackButton = findViewById(R.id.pause_button);
        getSupportActionBar().hide();

        MediaHandler.startPlaying(getApplicationContext(), getIntent().getStringExtra(INTENT_EXTRA_FILE_NAME));

        playbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PlayActivity", "onclick");
                if (!isPaused) {
                    MediaHandler.pausePlaying();
                    playbackButton.setText(R.string.play);
                    isPaused = true;
                } else {
                    MediaHandler.resumePlaying();
                    playbackButton.setText(R.string.pause);
                    isPaused = false;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        MediaHandler.stopPlaying();
        finish();
    }
}
