package com.jerry.eventful.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerry.eventful.R;

import java.util.Date;

public class SettingsFragment extends PreferenceFragment {

    private ListPreference mDateFormat;
    private String PREF_DATEFORMAT = "pref_dateformat";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mDateFormat = (ListPreference) findPreference(PREF_DATEFORMAT);
        parseDateFormats();
    }

    private void parseDateFormats() {
        String[] dateEntries = getResources().getStringArray(R.array.dateformat_values);
        CharSequence parsedDateEntries[];
        parsedDateEntries = new String[dateEntries.length];
        Date now = new Date();

        for (int i = 0; i < dateEntries.length; i++) {

            String newDate;
            newDate = DateFormat.format(dateEntries[i], now).toString();

            parsedDateEntries[i] = newDate;
        }
        mDateFormat.setEntries(parsedDateEntries);
    }

}

