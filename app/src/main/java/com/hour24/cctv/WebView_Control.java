package com.hour24.cctv;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebView_Control extends WebViewClient {

	Activity activity;
	int flag;

	Dialog dialog;
	Toast toast;

	String msg;

	public WebView_Control(Activity activity) {
		super();
		this.activity = activity;

		dialog = new Dialog(activity, R.style.Dialog_Transparent);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.addContentView(new ProgressBar(activity),
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {

		dialog.show();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		Log.e("devIoT", "WebViewClient onPageFinished()");

		view.clearHistory();
		view.clearCache(true);
		view.clearView();
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		if (dialog.isShowing()) {
			dialog.dismiss();

			Toast.makeText(activity, "페이지를 로드할 수 없습니다.", Toast.LENGTH_SHORT).show();
		}
		Log.e("devIoT", "WebViewClient error : " + errorCode);
	}
}
