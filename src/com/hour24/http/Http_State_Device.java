package com.hour24.http;

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
public class Http_State_Device extends AsyncTask<String, String, String> {

	Activity activity;
	String URL;

	Dialog dialog;

	public Http_State_Device(Activity activity, String URL) {
		this.activity = activity;
		this.URL = URL;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new Dialog(activity, R.style.Dialog_Transparent);

		dialog.addContentView(new ProgressBar(activity),
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... params) {

		String result = params[0];
		result = "";

		DefaultHttpClient client = new DefaultHttpClient();

		try {

			HttpPost post = new HttpPost(Element.URL + "/" + URL);

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("token", Element.TOKEN));

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