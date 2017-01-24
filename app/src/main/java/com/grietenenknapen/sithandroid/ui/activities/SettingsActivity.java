package com.grietenenknapen.sithandroid.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.ui.fragments.MainPreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainPreferenceFragment())
                .commit();
    }
}
