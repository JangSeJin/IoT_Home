package com.hour24.doorlock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.hour24.http.Http_Contorl_DoorLock;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class NFC_Proc extends AppCompatActivity {

	public static int TYPE_MINE = 2;
	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;

	private boolean flagNFC;

	NfcAdapter nfcAdapter;
	PendingIntent pendingIntent;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doorlock_nfc_proc);

		TextView txtInfo = (TextView) findViewById(R.id.txtInfo);

		Intent intent;
		intent = getIntent();
		flagNFC = intent.getBooleanExtra("flag", false);
		// flag�� true�� ��� nfc�� ����Ϸ��� ������ ������
		Log.e("devIoT", "nfc�� ����Ϸ��°�? : " + flagNFC);

		if (flagNFC) {
			txtInfo.setText("����Ʈ���� ������� �±��ϸ�\n����� ������ �ڵ����� ��ϵ˴ϴ�.");
		} else {
			txtInfo.setText("��ø� ��ٷ� �ֽñ� �ٶ��ϴ�.\n������� �ڵ����� �����ϴ�.");
			try {
				boolean result = Boolean
						.parseBoolean(new Http_Contorl_DoorLock(NFC_Proc.this, "open").execute("").get());
				Log.e("devIoT", "doolrock result : " + result);
				if (result) {
					Toast.makeText(NFC_Proc.this, "������� �������ϴ�.", Toast.LENGTH_LONG).show();
					txtInfo.setText("������� �������ϴ�.");
					this.finish();
				} else {
					txtInfo.setText("������� ���µ� �����Ͽ����ϴ�.\nNFC�� �ٽ� �±��Ͽ� �ֽñ� �ٶ��ϴ�.");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

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

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter != null) {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// flagNFC�� true�� ��쿡�� ��Ͻ�Ų��.
		if (flagNFC) {
			if (intent != null) {
				processTag(intent); // processTag �޼ҵ� ȣ��
			} else {

			}
		} else {

		}
	}

	// onNewIntent �޼ҵ� ���� �� ȣ��Ǵ� �޼ҵ�
	private void processTag(Intent intent) {

		// �±׷� ��Ƽ��Ƽ ����-
		// ��Ƽ��Ƽ���� url�� ��ȣ,����̽���ȣ ����-
		// true or false����-
		// true�϶� ����� ����url����

		// ��ϵ� ����
		// String jsonData = "{\"PHONE\": \"" + Element.PHONE + "\", \"IMEI\":
		// \"" + Element.IMEI + "\"}";
		String jsonData = "application/com.hour24.iot_home";
		Log.e("devIoT", "jsonData : " + jsonData);
		// ������ �±׸� ����Ű�� ��ü
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		// �Է¹��� ���� ������ �±׿� ��

		NdefMessage message = null;

		message = createTagMessage(jsonData, TYPE_MINE);
		writeTag(message, detectedTag);
	}

	@SuppressLint("NewApi")
	public boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					return false;
				}

				if (ndef.getMaxSize() < size) {
					return false;
				}
				ndef.writeNdefMessage(message);
				Toast.makeText(NFC_Proc.this, "����� ������ ��ϵƽ��ϴ�.", Toast.LENGTH_SHORT).show();
				NFC_Proc.this.finish();

			} else {
				Toast.makeText(NFC_Proc.this, "���˵��� ���� NFC�̹Ƿ� ���� �����ϰ� ����մϴ�.", Toast.LENGTH_SHORT).show();

				NdefFormatable formatable = NdefFormatable.get(tag);
				if (formatable != null) {
					try {
						formatable.connect();
						formatable.format(message);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			return false;
		}

		return true;
	}

	private NdefMessage createTagMessage(String msg, int type) {
		NdefRecord[] records = new NdefRecord[1];

		if (type == TYPE_TEXT) { // TEXT �϶�
			records[0] = createTextRecord(msg, Locale.KOREAN, true);
		} else { // URI �϶�
			records[0] = createUriRecord(msg.getBytes());
		}

		NdefMessage mMessage = new NdefMessage(records);

		return mMessage;
	}

	private NdefRecord createTextRecord(String text, Locale locale, boolean encodeInUtf8) {
		final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
		final Charset utfEncoding = encodeInUtf8 ? Charsets.UTF_8 : Charset.forName("UTF-16");
		final byte[] textBytes = text.getBytes(utfEncoding);
		final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		final char status = (char) (utfBit + langBytes.length);
		final byte[] data = Bytes.concat(new byte[] { (byte) status }, langBytes, textBytes);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	}

	@SuppressLint("NewApi")
	private NdefRecord createUriRecord(byte[] data) {
		return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[0], data);
	}

}
