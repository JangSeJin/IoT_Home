package com.hour24.iot_home;

import android.app.Activity;
import android.widget.Toast;

public class BackPressFinish {

	private long backKeyPressedTime = 0;
	private Toast toast;

	private Activity activity;

	public BackPressFinish(Activity context) {
		this.activity = context;
	}

	public void onBack() {
		if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
			backKeyPressedTime = System.currentTimeMillis();
			showGuide();
			return;
		}
		if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
			activity.finish();
			toast.cancel();
		}
	}

	public void showGuide() {
		toast = Toast.makeText(activity, "'종료' 버튼을 한번 더 누르면 종료합니다.",
				Toast.LENGTH_SHORT);
		toast.show();
	}

}