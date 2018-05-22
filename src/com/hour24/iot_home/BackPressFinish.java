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
		toast = Toast.makeText(activity, "'����' ��ư�� �ѹ� �� ������ �����մϴ�.",
				Toast.LENGTH_SHORT);
		toast.show();
	}

}