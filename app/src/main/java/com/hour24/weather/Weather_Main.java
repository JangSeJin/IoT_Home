package com.hour24.weather;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.cctv.CCTV_Main;
import com.hour24.cctv.WebView_Control;
import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Weather_Main extends Fragment {

	private static int section_number = 0;

	public Weather_Main() {
	}

	public static Weather_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		Weather_Main fragment = new Weather_Main();
		Weather_Main.section_number = section_number;
		return fragment;
	}

	TextView txtValue;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.weather_main, container, false);

		WebView webView = (WebView) view.findViewById(R.id.webView);
		txtValue = (TextView) view.findViewById(R.id.txtValue);

		// webview setting
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setVerticalScrollbarOverlay(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new WebView_Control(Weather_Main.this.getActivity()));

		webView.loadUrl("http://www.norumoe.com/hour24/now.jsp");

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(section_number);
	}

}