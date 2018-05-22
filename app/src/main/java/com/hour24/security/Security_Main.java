package com.hour24.security;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
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
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Security_Main extends Fragment {

	private static int section_number = 0;

	public Security_Main() {
	}

	public static Security_Main newInstance(int section_number) {
		// TODO Auto-generated method stub
		Security_Main fragment = new Security_Main();
		Security_Main.section_number = section_number;
		return fragment;
	}

	ListView listView;

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.security_main, container, false);
		Element.SecurityActivity = Security_Main.this.getActivity();

		listView = (ListView) view.findViewById(R.id.listView);
		arrayList = new ArrayList<List_States>();

		try {
			// 보안이 되어있는지 데이터
			String jsonData = new Http_State_Device(Security_Main.this.getActivity(), "ConfigState").execute("").get();
			Log.e("devIoT", "통합보안 데이터 : " + jsonData);
			jsonParse1(jsonData);

			// 도어락
			jsonData = new Http_State_Device(Security_Main.this.getActivity(), "DoorlockState").execute("").get();
			jsonParse2(jsonData);
			Log.e("devIoT", "도어락 데이터 : " + jsonData);

			// 창문열림감지
			jsonData = new Http_State_Device(Security_Main.this.getActivity(), "WindowsState").execute("").get();
			jsonParse3(jsonData);
			Log.e("devIoT", "창문열림감지 데이터 : " + jsonData);

			// 모션감지
			jsonData = new Http_State_Device(Security_Main.this.getActivity(), "MotionState").execute("").get();
			jsonParse4(jsonData);
			Log.e("devIoT", "모션감지 데이터 : " + jsonData);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		Log.e("devIoT", "Security_Main onPause");
		Element.SecurityActivity = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "Security_Main onResume");
		Element.SecurityActivity = Security_Main.this.getActivity();
	}

	// 통합보안
	private void jsonParse1(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Warning = Boolean.parseBoolean(json.getString("Warning"));

			list_Add = new List_Add(Security_Main.this.getActivity(), arrayList, listView, "security-01", "통합보안", "",
					Warning);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Security_Main.this.getActivity(), "통합보안 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	// 도어락
	private void jsonParse2(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			String Turn = json.getString("Turn");
			// Low 닫힌거

			list_Add = new List_Add(Security_Main.this.getActivity(), arrayList, listView, "doorlock-01", "도어락", Turn,
					false);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Security_Main.this.getActivity(), "도어락 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	// 창문열림감지
	private void jsonParse3(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			boolean sensoerflag = false;
			String Turn = "";

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				Turn = c.getString("Turn"); // isAuto일땐 센서의 동작유무 / 아닐땐 open,
											// close
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // Auto 정보 받아오기 위해서 트릭을 사용함
					sensoerflag = Boolean.parseBoolean(Turn);
				} else {

				}

			}

			list_Add = new List_Add(Security_Main.this.getActivity(), arrayList, listView, "window-01", "창문열림감지", Turn,
					sensoerflag);
			list_Add.onAdd();

			Log.e("devIoT", "창문열림 감지  : " + Turn + " / " + sensoerflag);

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Security_Main.this.getActivity(), "창문열림감지 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	// 모션감지
	private void jsonParse4(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Auto = Boolean.parseBoolean(json.getString("Auto"));
			String Turn = json.getString("Turn");
			// Turn이 LOW == 움직임X, HIGH == 움직임 O

			list_Add = new List_Add(Security_Main.this.getActivity(), arrayList, listView, "motion-01", "모션감지", Turn,
					Auto);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Security_Main.this.getActivity(), "모션센서 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}