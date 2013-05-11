package com.example.wificlient;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Config extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config);
	}
	
}