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

import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class Http_Auto extends AsyncTask<String, String, String> {

	Activity activity;
	String URL;
	boolean flag;

	Dialog dialog;

	public Http_Auto(Activity activity, String URL, boolean flag) {
		this.activity = activity;
		this.URL = URL;
		this.flag = flag;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new Dialog(activity, R.style.Dialog_Transparent);

		dialog.addContentView(new ProgressBar(activity),
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.show();
		Log.e("devIoT", "Http_Auto");
	}

	@Override
	protected String doInBackground(String... params) {

		String result = params[0];
		result = "";

		DefaultHttpClient client = new DefaultHttpClient();

		try {

			HttpPost post = new HttpPost(Element.URL + "/" + URL);

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("auto", flag + ""));
			pairs.add(new BasicNameValuePair("phone", Element.PHONE + ""));
			pairs.add(new BasicNameValuePair("token", Element.TOKEN));

			UrlEncodedFormEntity encoded = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
			post.setEntity(encoded);

			client.execute(post);

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