package com.hour24.light;

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
import android.widget.ToggleButton;

public class Light_ViewControl {

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;
	ToggleButton tgbtn1;

	public Light_ViewControl() {
		// TODO Auto-generated constructor stub
	}

	public void onView() {
		if (Element.LightActivity != null) {

			listView = (ListView) Element.LightActivity.findViewById(R.id.listView);
			if (listView != null) {
				arrayList = new ArrayList<List_States>();

				tgbtn1 = (ToggleButton) Element.LightActivity.findViewById(R.id.tgbtn1);

				try {
					String jsonData = new Http_State_Device(Element.LightActivity, "LightState").execute("").get();
					Log.e("devIoT", jsonData);
					jsonParse(jsonData);
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

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				boolean Turn = Boolean.parseBoolean(c.getString("Turn"));
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // Auto 정보 받아오기 위해서 트릭을 사용함
				} else if (PinName.equals("all")) {
					if (Turn) {
						tgbtn1.setBackgroundResource(R.drawable.ic_power_on);
					} else {
						tgbtn1.setBackgroundResource(R.drawable.ic_power_off);
					}
				} else {
					list_Add = new List_Add(Element.LightActivity, arrayList, listView, PinName, Turn, NickName);
					list_Add.onAdd();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.LightActivity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
