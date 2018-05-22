package com.hour24.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.hour24.curtain.Curtain_ViewControl;
import com.hour24.iot_home.Element;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Http_Control_Curtain_All extends AsyncTask<Integer, String, Integer> {

	Activity activity;
	String pinName;
	int turn;

	ProgressDialog dialog;

	public Http_Control_Curtain_All(Activity activity, String pinName, int turn) {
		this.activity = activity;
		this.pinName = pinName;
		this.turn = turn;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new ProgressDialog(activity);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Integer doInBackground(Integer... params) {

		int value = params[0];
		publishProgress("max", value + "");
		Log.e("devIoT", value + "");

		DefaultHttpClient client = new DefaultHttpClient();

		try {

			// value == device �� ����
			for (int i = 1; i <= value; i++) {

				HttpPost post = new HttpPost(Element.URL + "/CurtainHttp");

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("pinname", pinName + "-0" + i));
				pairs.add(new BasicNameValuePair("turn", turn + ""));
				pairs.add(new BasicNameValuePair("phone", Element.PHONE));
				pairs.add(new BasicNameValuePair("token", Element.TOKEN));

				Log.e("devIoT", pinName + "-0" + i + " / " + turn + " / " + Element.PHONE);

				UrlEncodedFormEntity encoded = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
				post.setEntity(encoded);
				client.execute(post);

				HttpParams httpparams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpparams, 5000);
				HttpConnectionParams.setSoTimeout(httpparams, 5000);

				// �۾� ���� ���� ������� �����ϱ� ���� ����� ������ ������ publishProgress() �� �Ѱ���.
				publishProgress("ing", i + "", pinName + "-0" + i);
			}

			return value;

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();
		}

		return value;
	}

	// onProgressUpdate() �Լ��� publishProgress() �Լ��� �Ѱ��� �����͵��� �޾ƿ�
	@Override
	protected void onProgressUpdate(String... progress) {
		if (progress[0].equals("ing")) {
			dialog.setProgress(Integer.parseInt(progress[1]));
			// dialog.setMessage(progress[2] + "������");
		} else if (progress[0].equals("max")) {
			// Toast.makeText(activity, "��ø� ��ٷ� �ֽñ� �ٶ��ϴ�.",
			// Toast.LENGTH_LONG).show();
			dialog.setMax(Integer.parseInt(progress[1]));
		}
	}

	// onPostExecute() �Լ��� doInBackground() �Լ��� ����Ǹ� �����
	@Override
	protected void onPostExecute(Integer result) {

		long saveTime = System.currentTimeMillis();
		long currTime = 0;
		while (currTime - saveTime < 2000) {
			currTime = System.currentTimeMillis();
		}

		new Curtain_ViewControl().onView();
		dialog.dismiss();
	}
}