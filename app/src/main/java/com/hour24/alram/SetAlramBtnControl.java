package com.hour24.alram;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;

public class SetAlramBtnControl {

	Activity activity;
	String device;
	ImageView btnAlram;
	SwitchCompat switchOnOff;

	public SetAlramBtnControl(Activity activity, String device, ImageView btnAlram) {
		super();
		this.activity = activity;
		this.device = device;
		this.btnAlram = btnAlram;
	}

	public SetAlramBtnControl(Activity activity, String device, SwitchCompat switchOnOff) {
		super();
		this.activity = activity;
		this.device = device;
		this.switchOnOff = switchOnOff;
	}

	public SetAlramBtnControl(Activity activity, String device) {
		super();
		this.activity = activity;
		this.device = device;
	}

	public void onChange() {

		String result = new AlramSharedPreferences(activity).getValue(device);
		String[] flag = result.split("#");

		btnAlram.setImageDrawable(null);

		if (flag[0].equals("true")) {
			btnAlram.setBackgroundResource(R.drawable.ic_alram_on);
		} else {
			btnAlram.setBackgroundResource(R.drawable.ic_alram_off);
		}
	}

	public void onSwitch() {

		String result = new AlramSharedPreferences(activity).getValue(device);
		String[] flag = result.split("#");

		switchOnOff.setChecked(Boolean.parseBoolean(flag[0]));
	}
}
