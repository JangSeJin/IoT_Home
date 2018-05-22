package com.hour24.alram;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlramSharedPreferences {

	Activity activity;
	SharedPreferences preferences;

	public AlramSharedPreferences(Activity activity) {
		super();
		this.activity = activity;

		preferences = PreferenceManager.getDefaultSharedPreferences(activity);
	}

	public void set(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getValue(String key) {
		return preferences.getString(key, "false#0#0");
	}

	public void clear(String key) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
}
