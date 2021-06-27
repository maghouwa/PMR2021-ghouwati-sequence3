package com.App1.app1.ui

import android.os.Bundle
import android.preference.PreferenceActivity
import com.App1.app1.R

class SettingsActivity : PreferenceActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
