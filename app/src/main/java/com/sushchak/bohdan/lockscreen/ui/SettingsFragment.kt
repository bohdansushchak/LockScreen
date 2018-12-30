package com.sushchak.bohdan.lockscreen.ui

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.sushchak.bohdan.lockscreen.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey)
    }
}