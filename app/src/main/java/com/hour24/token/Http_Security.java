package com.hour24.token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class Http_Security extends AsyncTask<String, String, String> {

	Activity activity;
	Dialog dialog;

	private String phone = "";
	private String imei = "";

	public Http_Security(Activity activity, String phone, String imei) {
		this.activity = activity;
		this.phone = phone;
		this.imei = imei;
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

		DefaultHttpClient client = new DefaultHttpClient();

		try {
			// http://rsa.yangukmo.com:12347/RSAService?phnumber=01087905711&imeinumber=1234561651651611231231
			HttpPost post = new HttpPost(Element.URL_S + "/RSAService");

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("phnumber", phone));
			pairs.add(new BasicNameValuePair("imeinumber", imei));

			UrlEncodedFormEntity encoded = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
			post.setEntity(encoded);

			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "utf-8"));

			String line = null;

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}

			HttpParams httpparams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, 5000);
			HttpConnectionParams.setSoTimeout(httpparams, 5000);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();
		}

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