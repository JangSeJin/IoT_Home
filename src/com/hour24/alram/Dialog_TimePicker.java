package com.hour24.alram;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;
import com.hour24.iot_home.ServerInfo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

public class Dialog_TimePicker {

	Activity activity;
	String device = "";
	int position = 0;

	Dialog dialog;
	TimePicker timePicker;
	SwitchCompat switchOnOff;

	ImageView btnAlram;

	int hour = 0, min = 0;

	// �˶� �޴���
	AlarmManager mManager;
	// ���� �Ͻ�
	GregorianCalendar mCalendar;

	public Dialog_TimePicker(Activity activity, String device) {
		super();
		this.activity = activity;
		this.device = device;
	}

	@SuppressWarnings("static-access")
	public void onDialog() {

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.alram_dialog, null);
		timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		switchOnOff = (SwitchCompat) view.findViewById(R.id.switchOnOff);

		// btnAlram = (ImageView)
		// Element.LightActivity.findViewById(R.id.btnAlram);

		// switch ��ư�� �����Ѵ�.
		new SetAlramBtnControl(activity, device, switchOnOff).onSwitch();
		// �ð�����
		new SetTimePicker(activity, timePicker, device).onSet();

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

		alertBuilder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				hour = timePicker.getCurrentHour();
				min = timePicker.getCurrentMinute();
				new AlramSharedPreferences(activity).set(device, switchOnOff.isChecked() + "#" + hour + "#" + min);
				String result = new AlramSharedPreferences(activity).getValue(device);
				Log.e("devIoT", result);

				setAlarm(Element.activity);

				// �˶� ��ư�� �ٲ��ش�.
				btnAlram.setImageDrawable(null);
				if (switchOnOff.isChecked() == true) {
					btnAlram.setBackgroundResource(R.drawable.ic_alram_on);
					// setAlarm();
				} else {
					btnAlram.setBackgroundResource(R.drawable.ic_alram_off);
					// resetAlarm();
				}

				Log.e("devIoT", switchOnOff.isChecked() + "");
			}
		});

		alertBuilder.setNegativeButton("���", null);

		alertBuilder.setView(view);
		alertBuilder.create();
		dialog = alertBuilder.create();
		dialog.show();
	}

	// �˶��� ����
	private void setAlarm(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Intent Intent = new Intent(activity, AlarmReceive.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);

		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pIntent);
	}

	// �˶��� ����
	private void resetAlarm() {

		AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

		Intent Intent = new Intent(activity, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(activity, 0, Intent, 0);
		alarmManager.cancel(pIntent);
	}

	// �˶��� ���� �ð��� �߻��ϴ� ����Ʈ �ۼ�
	private PendingIntent pendingIntent() {

		// light == 0
		// curtain == 1

		int flag = 0;

		if (device.equals("light")) {
			flag = 0;
		} else if (device.equals("curtain")) {
			flag = 1;
		} else {
			flag = 0;
		}

		Intent intent = new Intent(Element.activity, AlramPage.class);
		intent.putExtra("flag", "true");
		intent.putExtra("device", device);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(Element.activity, flag, intent, 0);

		return pendingIntent;
	}

}
