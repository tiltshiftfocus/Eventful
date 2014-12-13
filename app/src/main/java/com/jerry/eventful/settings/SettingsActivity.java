package com.jerry.eventful.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.jerry.eventful.R;

public class SettingsActivity extends Activity {

    private String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle instance) {
        PACKAGE_NAME = this.getPackageName();
        setTheme();
        super.onCreate(instance);
        setContentView(R.layout.activity_settings);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().
                replace(R.id.settings_layout, new SettingsFragment()).
                commit();


    }

    private void setTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("darktheme", false) == true) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }
}
