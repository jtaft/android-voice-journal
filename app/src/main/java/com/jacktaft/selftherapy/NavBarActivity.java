package com.jacktaft.selftherapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


public class NavBarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ImageButton hamburgerButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private final String TAG = "NavBarActivity";

    private void hideMenuItems(Menu menu) {
        if (this.getClass().getName().contains("ArchiveActivity")) {
            menu.removeItem(R.id.archive);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch ((String)menuItem.getTitle()){
            case "Archive":
                Log.e(TAG,"Archive Selected");
                Intent archiveIntent = new Intent(NavBarActivity.this, ArchiveActivity.class);
                startActivity(archiveIntent);
                break;
            default:
                return false;
        }
        mDrawerLayout.closeDrawer(navigationView);
        return true;
    }

    @Override
    public void setContentView(int resId){
        DrawerLayout drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.nav_drawer, null);
        ConstraintLayout activityContainer = drawer.findViewById(R.id.activity_container);
        ConstraintLayout mainLayout = (ConstraintLayout) getLayoutInflater().inflate(resId, activityContainer);
        super.setContentView(drawer);
        Log.e(TAG, this.getClass().getName());


        mDrawerLayout = drawer.findViewById(R.id.drawer_layout);
        navigationView = drawer.findViewById(R.id.navigation);
        hamburgerButton = drawer.findViewById(R.id.hamburger_button);

        final Menu menu = navigationView.getMenu();

        hamburgerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(.5f);
                    KeyboardHandler.hideKeyboard();
                    hideMenuItems(menu);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                    mDrawerLayout.openDrawer(navigationView);
                    navigationView.bringToFront();
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }
}
