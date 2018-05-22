package com.hour24.innertemperature;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Innertemperature_Main extends Fragment {

	private static int section_number = 0;

	public Innertemperature_Main() {
	}

	public static Innertemperature_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		Innertemperature_Main fragment = new Innertemperature_Main();
		Innertemperature_Main.section_number = section_number;
		return fragment;
	}

	LinearLayout layout;
	TextView txtValue;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.inner_temp, container, false);

		layout = (LinearLayout) view.findViewById(R.id.layout);
		txtValue = (TextView) view.findViewById(R.id.txtValue);
		layout.setClickable(true);
		txtValue.setClickable(true);

		try {
			String jsonData = new Http_State_Device(Innertemperature_Main.this.getActivity(), "TempState").execute("")
					.get();
			jsonParse(jsonData, false);
			Log.e("devIoT", jsonData);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try {
					String jsonData = new Http_State_Device(Innertemperature_Main.this.getActivity(), "TempState")
							.execute("").get();
					jsonParse(jsonData, true);
					Log.e("devIoT", jsonData);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(section_number);
	}

	private void jsonParse(String jsonData, boolean updateFlag) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			String temperature = json.getString("temperature");

			if (temperature.equals("null")) {
				txtValue.setText("20.2");
			} else {
				txtValue.setText(temperature);
			}

			if (updateFlag) {
				Toast.makeText(Innertemperature_Main.this.getActivity(), "업데이트 하였습니다.", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Innertemperature_Main.this.getActivity(), "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

}