package com.hour24.doorlock;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_Contorl_DoorLock;
import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DoorLock_Main extends Fragment implements OnClickListener {

	private static int section_number = 0;

	public DoorLock_Main() {
	}

	public static DoorLock_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		DoorLock_Main fragment = new DoorLock_Main();
		DoorLock_Main.section_number = section_number;
		return fragment;

	}

	SwitchCompat switchFlag;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.doorlock_main, container, false);
		Element.DoorlockActivity = DoorLock_Main.this.getActivity();

		TextView btnWrite = (TextView) view.findViewById(R.id.btnWrite);
		btnWrite.setOnClickListener(this);

		TextView btnRead = (TextView) view.findViewById(R.id.btnRead);
		btnRead.setOnClickListener(this);

		switchFlag = (SwitchCompat) view.findViewById(R.id.switchFlag);

		// DoorlockState
		try {
			String jsonData = new Http_State_Device(DoorLock_Main.this.getActivity(), "DoorlockState").execute("")
					.get();
			jsonParse(jsonData);
			Log.e("devIoT", "DoorlockState : " + jsonData);
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
				boolean result = false;
				String flag = "";

				if (isChecked) {
					flag = "open";
					Toast.makeText(DoorLock_Main.this.getActivity(), "도어락 문을 열었습니다.", Toast.LENGTH_SHORT).show();
				} else {
					flag = "close";
					Toast.makeText(DoorLock_Main.this.getActivity(), "도어락 문을 닫았습니다.", Toast.LENGTH_SHORT).show();
				}

				try {
					result = Boolean.parseBoolean(
							new Http_Contorl_DoorLock(DoorLock_Main.this.getActivity(), flag).execute("").get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.e("devIoT", "doolrock result : " + result);
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
		Log.e("devIoT", "DoorlockActivity onPause");
		Element.DoorlockActivity = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "DoorLock_Main onResume");
		Element.DoorlockActivity = DoorLock_Main.this.getActivity();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (view.getId()) {
		case R.id.btnWrite:
			intent = new Intent(DoorLock_Main.this.getActivity(), NFC_Proc.class);
			intent.putExtra("flag", true);
			startActivity(intent);
			break;

		case R.id.btnRead:
			intent = new Intent(DoorLock_Main.this.getActivity(), NFC_Read.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			// DoorlockState : {"Auto":"true","Turn":"LOW"}
			String Turn = json.getString("Turn");
			if (Turn.equals("LOW")) {
				switchFlag.setChecked(false);
			} else {
				switchFlag.setChecked(true);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(DoorLock_Main.this.getActivity(), "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

}