package com.hour24.oneclick;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.iot_home.MySharedPreferences;
import com.hour24.iot_home.R;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class Delete_Data {

	Activity activity;
	MySharedPreferences mySharedPreferences;

	String success = "";

	public Delete_Data(Activity activity) {
		super();
		this.activity = activity;

		mySharedPreferences = new MySharedPreferences(activity);
	}

	public void onDelete(String originalData) {
		// TODO Auto-generated method stub
		String jsonData = mySharedPreferences.getValue("OneClick", "");
		Log.e("devIoT", "mySharedPreferences에 있는 데이터 : " + jsonData);

		Log.e("devIoT", "삭제되기전 데이터 : " + jsonData);
		Log.e("devIoT", "삭제할 데이터 : " + originalData);

		String sub1 = "", sub2 = "", sub3 = "", sub4 = "";
		String data1 = "", data2 = "", data3 = "", data4 = "";

		data1 = ",{\"OneClickElement\":" + originalData + "}";
		sub1 = jsonData.replace(data1, "");
		Log.e("devIoT", "sub1 " + sub1);

		data2 = "{\"OneClickElement\":" + originalData + "},";
		sub2 = jsonData.replace(data2, "").trim();
		Log.e("devIoT", "sub2 " + sub2);

		data3 = ",{\"OneClickElement\":" + originalData + "},";
		sub3 = jsonData.replace(data3, "");
		Log.e("devIoT", "sub3 " + sub3);

		data4 = "{\"OneClickElement\":" + originalData + "}";
		sub4 = jsonData.replace(data4, "").trim();
		Log.e("devIoT", "sub4 " + sub4);

		if (!data1.equals(sub1)) {
			success = sub1;
		} else if (!data3.equals(sub3)) {
			success = sub3;
		} else {
			success = "";
		}

		if (!data2.equals(sub2)) {
			success = sub2;
		}

		if (sub4.equals("{\"OneClick\":[]}")) {
			success = "false";
		}
		Log.e("devIoT", "삭제된 데이터 : " + success);

		new MySharedPreferences(activity).clear("OneClick");
		new MySharedPreferences(activity).set("OneClick", success);

		jsonParse(success);
	}

	// 리스트뷰에 추가
	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub

		List_Add one_list_Add;
		ArrayList<List_States> one_arrayList = new ArrayList<List_States>();
		ListView one_listView = (ListView) activity.findViewById(R.id.listView);

		one_list_Add = new List_Add(activity, one_arrayList, one_listView, true, "", "");
		one_list_Add.onAdd();

		if (jsonData.equals("false")) {

		} else {

			String cellName = "";

			try {

				JSONObject json = new JSONObject(jsonData);
				JSONArray arry = json.getJSONArray("OneClick");

				for (int i = 0; i < arry.length(); i++) {
					JSONObject json2 = arry.getJSONObject(i);
					JSONArray arry2 = json2.getJSONArray("OneClickElement");

					for (int j = 0; j < arry2.length(); j++) {
						JSONObject c = arry2.getJSONObject(j);
						String pinName = c.getString("pinName");
						String flag = c.getString("flag");

						// Log.e("devIoT", pinName + " / " + flag);

						if (pinName.equals("Name")) {
							cellName = flag;
						}
					}
					one_list_Add = new List_Add(activity, one_arrayList, one_listView, false, cellName,
							arry2.toString());
					one_list_Add.onAdd();
					Log.e("devIoT", "OneClickElement : " + arry2.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(activity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
