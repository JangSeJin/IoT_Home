package com.hour24.iot_home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import com.google.android.gcm.GCMBaseIntentService;
import com.hour24.curtain.Curtain_ViewControl;
import com.hour24.http.Http_UserInsert;
import com.hour24.light.Light_ViewControl;
import com.hour24.rpi.RPi_ViewControl;
import com.hour24.security.WarningDialog;
import com.hour24.security.Security_ViewControl;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e("devIoT", "onError");
	}

	@Override
	protected void onMessage(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("devIoT", "onMessage");

		final String ticker = intent.getStringExtra("ticker");
		String message = intent.getStringExtra("message");
		Log.e("devIoT", "GCM onMessage : " + ticker + " / " + message);

		// config
		// 통합보안 새로고침

		// view를 컨트롤 하려면 아래 와 같은 쓰레드를 사용한다.
		if (ticker.equals("warning")) { // 경고알람 수신
			Intent dialogIntent = new Intent(context, WarningDialog.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			dialogIntent.putExtra("message", message);
			context.startActivity(dialogIntent);
		} else if (Element.activity != null) {
			Element.activity.runOnUiThread(new Runnable() {
				public void run() {
					if (ticker.equals("Light")) {
						new Light_ViewControl().onView();
					} else if (ticker.equals("Curtain")) {
						new Curtain_ViewControl().onView();
					} else if (ticker.equals("cpufan")) {
						new RPi_ViewControl().onView();
					} else if (ticker.equals("config")) {
						new Security_ViewControl().onView();
					} else {
						Log.e("devIoT", "ticker 정보 없음 : " + ticker);
					}
				}
			});
		} else

		{

		}
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e("devIoT", "onRegistered : " + regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e("devIoT", "onUnregistered : " + regId);

	}
}
