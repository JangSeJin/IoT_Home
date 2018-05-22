package com.hour24.iot_home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {

	Activity activity;
	SharedPreferences preferences;

	public MySharedPreferences(Activity activity) {
		super();
		this.activity = activity;
		preferences = PreferenceManager.getDefaultSharedPreferences(activity);
	}

	public void set(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getValue(String key, String Default) {
		return preferences.getString(key, Default);
	}

	public void clear(String key) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
}
