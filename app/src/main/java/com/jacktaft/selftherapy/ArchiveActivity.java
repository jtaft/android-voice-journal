package com.jacktaft.selftherapy;

import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ArchiveActivity extends NavBarActivity {
    private final String TAG = "Archive Activity";
    private String[] filterNonAudio(String[] fileList){
        ArrayList<String> filteredList = new ArrayList<String>();
        for (String file: fileList) {
            if (file.endsWith(".3gp")){
                filteredList.add(file.replace(".3gp", ""));
            }
        }
        return filteredList.toArray(new String[0]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        getSupportActionBar().hide();
        final ConstraintLayout rootView = findViewById(R.id.activity_archive);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideKeyboard();
                rootView.clearFocus();
            }
        });

        ArchiveAdapter adapter = new ArchiveAdapter(this, filterNonAudio(getApplicationContext().fileList()));
        ListView listView = (ListView) findViewById(R.id.archive_list);
        listView.setAdapter(adapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardHandler.hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
