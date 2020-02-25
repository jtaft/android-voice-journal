package com.jacktaft.selftherapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SaveRecordingActivity extends AppCompatActivity {
    private Button deleteButton = null;
    private Button playButton = null;
    private Button saveButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_recording);
        deleteButton = findViewById(R.id.deleteButton);
        playButton = findViewById(R.id.playButton);
        saveButton = findViewById(R.id.saveButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaHandler.deleteRecording(getApplicationContext());
                //Intent mainIntent = new Intent(SaveRecordingActivity.this, MainActivity.class);
                //startActivity(mainIntent);
                finish();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SaveRecordingActivity.this, PlayRecordingActivity.class);
                playIntent.putExtra(PlayRecordingActivity.INTENT_EXTRA_FILE_NAME, MediaHandler.saveRecording(getApplicationContext()));
                startActivity(playIntent);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaHandler.saveRecording(getApplicationContext());
                //Intent mainIntent = new Intent(SaveRecordingActivity.this, MainActivity.class);
                //startActivity(mainIntent);
                finish();
            }
        });
    }
}
