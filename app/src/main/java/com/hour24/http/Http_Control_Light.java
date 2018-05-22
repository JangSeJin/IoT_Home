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
import com.hour24.light.Light_ViewControl;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class Http_Control_Light extends AsyncTask<String, String, String> {

	Activity activity;
	String pinname;
	boolean turn;
	String type;

	Dialog dialog;

	public Http_Control_Light(Activity activity, String type, String pinname, boolean turn) {
		this.activity = activity;
		this.type = type;
		this.pinname = pinname;
		this.turn = turn;
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
		Log.e("devIoT", "Http_Control_Light auto / turn : " + type);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... params) {

		String result = params[0];
		result = "";

		DefaultHttpClient client = new DefaultHttpClient();

		try {

			HttpPost post = new HttpPost(Element.URL + "/LightHttp");

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("type", type)); // on/off/auto 하는
																// 기능을
			// 실행시키겟다
			pairs.add(new BasicNameValuePair("pinname", pinname + ""));
			pairs.add(new BasicNameValuePair("turn", turn + ""));
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

		if (pinname == null) {

		} else if (pinname.equals("all")) {
			publishProgress(0 + "");
		} else {

		}

		return result;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		// if (progress[0].equals(0)) {
		// Log.e("devIoT", "http_control_light 에서 조도센서를 꺼버림");
		// new Http_Control_Light(Element.LightActivity, "auto", null,
		// false).execute("");
		// } else {
		//
		// }
	}

	@Override
	protected void onPostExecute(String result) {

		dialog.dismiss();
		if (pinname == null) {

		} else if (pinname.equals("all")) {
			new Light_ViewControl().onView();
			new Http_Control_Light(Element.LightActivity, "auto", null, false).execute("");
		} else {

		}
	}
}