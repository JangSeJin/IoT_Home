package com.hour24.security;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class Security_ViewControl {

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;

	public Security_ViewControl() {
		// TODO Auto-generated constructor stub
	}

	public void onView() {

		if (Element.SecurityActivity != null) {

			listView = (ListView) Element.SecurityActivity.findViewById(R.id.listView);

			if (listView != null) {

				arrayList = new ArrayList<List_States>();

				try {
					// 보안이 되어있는지 체크
					String jsonData = new Http_State_Device(Element.SecurityActivity, "ConfigState").execute("").get();
					Log.e("devIoT", jsonData);
					jsonParse1(jsonData);

					// 도어락
					jsonData = new Http_State_Device(Element.SecurityActivity, "DoorlockState").execute("").get();
					jsonParse2(jsonData);
					Log.e("devIoT", jsonData);

					// 창문열림감지
					jsonData = new Http_State_Device(Element.SecurityActivity, "WindowsState").execute("").get();
					jsonParse3(jsonData);
					Log.e("devIoT", jsonData);

					// 모션감지
					jsonData = new Http_State_Device(Element.SecurityActivity, "MotionState").execute("").get();
					jsonParse4(jsonData);
					Log.e("devIoT", jsonData);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
			}

		} else {

		}
	}

	// 통합보안
	private void jsonParse1(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Warning = Boolean.parseBoolean(json.getString("Warning"));

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "security-01", "통합보안", "", Warning);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "통합보안 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	// 도어락
	private void jsonParse2(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			String Turn = json.getString("Turn");

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "doorlock-01", "도어락", Turn, false);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "window-01", "창문열림감지", Turn, false);
			list_Add.onAdd();

			Log.e("devIoT", "창문열림 감지  : " + Turn + " / " + sensoerflag);

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "창문열림감지 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "motion-01", "모션감지", Turn, false);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "모션감지 데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
