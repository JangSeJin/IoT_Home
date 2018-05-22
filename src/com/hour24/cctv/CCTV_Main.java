package com.hour24.cctv;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_Control_CCTV;
import com.hour24.http.Http_Control_CCTV_Moving;
import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CCTV_Main extends Fragment implements OnClickListener {

	private static int section_number = 0;

	public CCTV_Main() {
	}

	public static CCTV_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		CCTV_Main fragment = new CCTV_Main();
		CCTV_Main.section_number = section_number;
		return fragment;
	}

	WebView webView;
	LinearLayout layoutBlind;
	SwitchCompat switchFlag;
	TextView txtValue;
	SeekBar seekBarH, seekBarV;
	ImageView img112, img119;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.cctv_main, container, false);
		Element.CCTVActivity = CCTV_Main.this.getActivity();

		switchFlag = (SwitchCompat) view.findViewById(R.id.switchFlag);
		webView = (WebView) view.findViewById(R.id.webView);
		layoutBlind = (LinearLayout) view.findViewById(R.id.layoutBlind);
		seekBarV = (SeekBar) view.findViewById(R.id.seekBarV);
		seekBarH = (SeekBar) view.findViewById(R.id.seekBarH);
		img112 = (ImageView) view.findViewById(R.id.img112);
		img119 = (ImageView) view.findViewById(R.id.img119);

		InItwebView(webView);

		img112.setOnClickListener(this);
		img119.setOnClickListener(this);

		// http://iot.yangukmo.com:12345/RaspicamState
		try {
			String jsonData = new Http_State_Device(CCTV_Main.this.getActivity(), "RaspicamState").execute("").get();
			jsonParse(jsonData);
			Log.e("devIoT", jsonData);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switchFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

				new Http_Control_CCTV(CCTV_Main.this.getActivity(), isChecked).execute("");
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, CCTV_Main.newInstance(5)).commit();

			}
		});

		seekBarH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			// seekbar 터치 후
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// 0 ~ 180
				int value = 1;
				value = seekBar.getProgress() * 10;

				if (value == 100) {
					value = 99;
				}

				new Http_Control_CCTV_Moving(CCTV_Main.this.getActivity(), "steplr", value).execute("");
				// Toast.makeText(CCTV_Main.this.getActivity(), "steplr" + " : "
				// + value, 0).show();
			}

			// seekbar 터치
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			// seekbar를 움직였을때
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		seekBarV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			// seekbar 터치 후
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// 40 50 60 70 80 90 99
				int value = 0;
				value = seekBar.getProgress();

				switch (seekBar.getProgress()) {
				case 0:
					value = 40;
					break;
				case 1:
					value = 50;
					break;
				case 2:
					value = 60;
					break;
				case 3:
					value = 70;
					break;
				case 4:
					value = 80;
					break;
				case 5:
					value = 90;
					break;
				default:
					break;
				}
				new Http_Control_CCTV_Moving(CCTV_Main.this.getActivity(), "steptb", value).execute("");
				// Toast.makeText(CCTV_Main.this.getActivity(), "steptb" + " : "
				// + value, 0).show();
			}

			// seekbar 터치
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			// seekbar를 움직였을때
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(section_number);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("devIoT", "Curtain_Main onPause");
		Element.CurtainActivity = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "CCTV_Main onResume");
		Element.CCTVActivity = CCTV_Main.this.getActivity();
	}

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Turn = Boolean.parseBoolean(json.getString("Turn"));
			Log.e("devIoT", "CCTV State : " + Turn);

			switchFlag.setChecked(Turn);

			new Http_Control_CCTV(CCTV_Main.this.getActivity(), Turn).execute("");

			if (Turn) {
				layoutBlind.setVisibility(View.GONE);
			} else {
				layoutBlind.setVisibility(View.VISIBLE);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void InItwebView(WebView webView) {
		// TODO Auto-generated method stub

		// webview setting

		webView.loadUrl(Element.URL + "/RaspicamStream");

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setAppCacheEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setVerticalScrollbarOverlay(true);
		webView.setHorizontalScrollBarEnabled(false);

		webView.setWebViewClient(new WebView_Control(CCTV_Main.this.getActivity()));

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (view.getId()) {
		case R.id.img112:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"));
			startActivity(intent);
			break;
		case R.id.img119:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}