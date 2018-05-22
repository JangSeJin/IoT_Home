package com.hour24.iot_home;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.google.android.gcm.GCMRegistrar;
import com.hour24.http.Http_Control_Security;
import com.hour24.token.Http_Security;
import com.hour24.token.SHAKey;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressWarnings({ "unused", "deprecation" })
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ServerInfo extends AppCompatActivity {

	public static Activity activity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_info);
		activity = ServerInfo.this;

		new StartProc(ServerInfo.this).execute("");

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "ServerInfo onResume");
		activity = ServerInfo.this;
	}

	public class StartProc extends AsyncTask<String, String, String> {

		Activity activity;

		Dialog dialog;

		public StartProc(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new Dialog(activity, R.style.Dialog_Transparent);

			dialog.addContentView(new ProgressBar(activity),
					new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			dialog.show();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... params) {

			String result = params[0];
			result = "";

			GCMRegistrar.checkDevice(ServerInfo.this);
			GCMRegistrar.checkManifest(ServerInfo.this);
			GCMRegistrar.register(ServerInfo.this, Element.PROJECT_ID);

			Button btnOk = (Button) findViewById(R.id.btnOk);
			final EditText editURL = (EditText) findViewById(R.id.editURL);

			String url = new MySharedPreferences(ServerInfo.this).getValue("serverURL", "");

			// editURL.setText(url);
			editURL.setText(Element.URL);
			// editURL.setText("http://192.168.0.17:8090");

			btnOk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub

					TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String phone = systemService.getLine1Number();
					phone = "0" + phone.substring(phone.length() - 10, phone.length());
					String IMEI = systemService.getDeviceId();
					String URL = editURL.getText().toString();

					Log.e("devIoT", "phone : " + new SHAKey().convert(phone));
					Log.e("devIoT", "IMEI : " + new SHAKey().convert(IMEI));

					try {
						String result = new Http_Security(ServerInfo.this, new SHAKey().convert(phone),
								new SHAKey().convert(IMEI)).execute("").get();
						Log.e("devIoT", "security에서 넘어온 값 : " + result);

						if (result.equals("null") || result.equals("false") || result.contains("<html>")
								|| result.equals("") || result.length() < 5) {
							// 회원정보 매치 실패
							Toast.makeText(ServerInfo.this, "서버에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show();
						} else {
							// 토큰정보 저장
							Element.TOKEN = result;
							Element.PHONE = phone;
							Element.URL = URL;

							new MySharedPreferences(ServerInfo.this).set("serverURL", URL);

							Intent intent = new Intent(ServerInfo.this, MainActivity.class);
							startActivity(intent);
							// finish();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			String URL = editURL.getText().toString(); // editText 값 정보 획득
			Element.URL = URL; // static에 넣기

			// if (URL.length() != 0) {
			// TelephonyManager systemService = (TelephonyManager)
			// getSystemService(Context.TELEPHONY_SERVICE);
			// String phone = systemService.getLine1Number();
			// phone = "0" + phone.substring(phone.length() - 10,
			// phone.length());
			// Element.PHONE = phone;
			//
			// String IMEI = systemService.getDeviceId();
			// Element.IMEI = IMEI;
			//
			// Intent intent = new Intent(ServerInfo.this, MainActivity.class);
			// startActivity(intent);
			// finish();
			// }

			return result;
		}

		@Override
		protected void onProgressUpdate(String... progress) {

		}

		@Override
		protected void onPostExecute(String result) {
			// dialog_Progress.onProgress_dismiss();
			dialog.dismiss();
		}
	}
}