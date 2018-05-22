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
				// Ű��� �����ϱ�
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				// ȭ�� �ѱ�
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// �Ҹ����, �������, ������忡 ���� �Ҹ��� �޸� ǥ������
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

		// context, ���ҽ�, �켱��
		sound = soundPool.load(WarningDialog.this, R.raw.ddok, 1);

		// ������̵�, �º���, �캼��, ����켱����, �ݹ�����, �ӵ�
		soundPool.play(sound, 1, 1, 1, 100, 2);

		// ������ �ִ�� Ű���.
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// ���� ���� ��������
		int volume = am.getStreamVolume(AudioManager.STREAM_RING);
		am.setStreamVolume(AudioManager.STREAM_RING, 15, AudioManager.FLAG_PLAY_SOUND);

	}

	@SuppressLint("NewApi")
	private void dialog() {
		// TODO Auto-generated method stub

		Intent messageIntent = getIntent();
		String message = messageIntent.getStringExtra("message");

		if (message == null) {
			message = "�̻������� �����ƽ��ϴ�.";
		}

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WarningDialog.this,
				R.style.AppCompatAlertDialogStyle);

		alertBuilder.setMessage(message + "\n\n�Ʒ� 'CCTV ����'�� Ŭ���Ͽ� ������ ���캸�ñ� �ٶ��ϴ�.");

		alertBuilder.setPositiveButton("CCTV ����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				// soundPool.stop(sound);
				Toast.makeText(WarningDialog.this, "��ø� ��ٷ� �ֽñ� �ٶ��ϴ�.", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(WarningDialog.this, MainActivity.class);
				intent.putExtra("flag", "cctv");
				startActivity(intent);
				WarningDialog.this.finish();

			}
		});

		alertBuilder.setNegativeButton("�ݱ�", new DialogInterface.OnClickListener() {

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
