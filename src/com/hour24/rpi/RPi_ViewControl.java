package com.hour24.rpi;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class RPi_ViewControl {

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;

	public RPi_ViewControl() {
		// TODO Auto-generated constructor stub
	}

	public void onView() {
		if (Element.RPiActivity != null) {

			listView = (ListView) Element.RPiActivity.findViewById(R.id.listView);

			if (listView != null) {
				arrayList = new ArrayList<List_States>();

				try {
					String jsonData = new Http_State_Device(Element.RPiActivity, "CPUState").execute("").get();
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

			String CPU_TEMP = json.getString("CPU_TEMP");
			boolean Auto = Boolean.parseBoolean(json.getString("Auto"));
			boolean Turn = Boolean.parseBoolean(json.getString("Turn"));

			list_Add = new List_Add(Element.RPiActivity, arrayList, listView, "fan-01", "온도", CPU_TEMP, Turn, Auto);
			list_Add.onAdd();

			Log.e("devIoT", "cpu : " + Auto + "(auto) / " + CPU_TEMP + " / " + Turn + "(on,off");

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.RPiActivity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
