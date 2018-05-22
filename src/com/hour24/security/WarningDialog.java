package com.hour24.security;

import com.hour24.cctv.CCTV_Main;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class WarningDialog extends AppCompatActivity {

	SoundPool soundPool;
	int sound;

	Vibrator vibrator;

	public static AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.security_dialog);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				// 키잠금 해제하기
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				// 화면 켜기
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// 소리모드, 진동모드, 무음모드에 따라서 소리를 달리 표시하자
		// sound();
		dialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	private void sound() {
		// TODO Auto-generated method stub
		soundPool = new SoundPool(1, AudioManager.STREAM_RING, 2);

		// context, 리소스, 우선권
		sound = soundPool.load(WarningDialog.this, R.raw.ddok, 1);

		// 실행아이디, 좌볼륨, 우볼륨, 재생우선순위, 반벅여부, 속도
		soundPool.play(sound, 1, 1, 1, 100, 2);

		// 볼륨을 최대로 키운다.
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 현재 볼륨 가져오기
		int volume = am.getStreamVolume(AudioManager.STREAM_RING);
		am.setStreamVolume(AudioManager.STREAM_RING, 15, AudioManager.FLAG_PLAY_SOUND);

	}

	@SuppressLint("NewApi")
	private void dialog() {
		// TODO Auto-generated method stub

		Intent messageIntent = getIntent();
		String message = messageIntent.getStringExtra("message");

		if (message == null) {
			message = "이상유무가 감지됐습니다.";
		}

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WarningDialog.this,
				R.style.AppCompatAlertDialogStyle);

		alertBuilder.setMessage(message + "\n\n아래 'CCTV 보기'를 클릭하여 집안을 살펴보시기 바랍니다.");

		alertBuilder.setPositiveButton("CCTV 보기", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				// soundPool.stop(sound);
				Toast.makeText(WarningDialog.this, "잠시만 기다려 주시기 바랍니다.", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(WarningDialog.this, MainActivity.class);
				intent.putExtra("flag", "cctv");
				startActivity(intent);
				WarningDialog.this.finish();

			}
		});

		alertBuilder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// soundPool.stop(sound);

				WarningDialog.this.finish();

			}
		});
		// alertBuilder.show();
		alertDialog = alertBuilder.create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
			alertDialog.setCanceledOnTouchOutside(false);
		}

	}
}
