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

public class Add_Data {

	Activity activity;
	MySharedPreferences mySharedPreferences;

	public Add_Data(Activity activity) {
		super();
		this.activity = activity;

		mySharedPreferences = new MySharedPreferences(activity);
	}

	public void onAdd(JSONObject jsonOneclick) {
		// TODO Auto-generated method stub
		String jsonData = mySharedPreferences.getValue("OneClick", "");
		Log.e("devIoT", "mySharedPreferences에 있는 데이터 : " + jsonData);

		if (jsonData.equals("") || jsonData.equals("false")) {
			// 데이터가 아무것도 없을 경우에는 그냥 집어 넣겠다.
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();

				// jsonArray를 만들기위해서 어쩔수없음

				jsonArray.put(jsonOneclick);
				jsonObject.put("OneClick", jsonArray);

				Log.e("devIoT", jsonObject + "");

				new MySharedPreferences(activity).clear("OneClick");
				new MySharedPreferences(activity).set("OneClick", jsonObject.toString());
				jsonParse(jsonObject.toString());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(activity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}
		} else { // 기존 데이터가 있을 경우
			try {
				// 원래있던 데이터를 파싱한다.
				JSONObject jsonBig = new JSONObject(jsonData);
				JSONArray arry = jsonBig.getJSONArray("OneClick");

				// 다시 넣을것
				JSONObject jsonMain = new JSONObject();
				JSONObject jsonSub = new JSONObject();
				JSONArray arryMain = new JSONArray();

				// 새로추가한것
				arryMain.put(jsonOneclick);
				jsonSub = new JSONObject();
				jsonSub.put("OneClickElement", arryMain);

				jsonMain.put("OneClick", arryMain);

				for (int i = 0; i < arry.length(); i++) {
					JSONObject json2 = arry.getJSONObject(i);
					JSONArray arry2 = json2.getJSONArray("OneClickElement");

					Log.e("devIoT", "기존데이터 : " + arry2);
					jsonSub = new JSONObject();
					jsonSub.put("OneClickElement", arry2);

					arryMain.put(jsonSub);
					jsonMain.put("OneClick", arryMain);
				}

				Log.e("devIoT", "진짜 최종데이터 : " + jsonMain.toString());

				new MySharedPreferences(activity).set("OneClick", jsonMain.toString());
				jsonParse(jsonMain.toString());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(activity, "데이터를 처리하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}

		}

	}

	// 리스트뷰에 추가
	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub

		List_Add one_list_Add;
		ArrayList<List_States> one_arrayList = new ArrayList<List_States>();
		ListView one_listView = (ListView) activity.findViewById(R.id.listView);

		one_list_Add = new List_Add(activity, one_arrayList, one_listView, true, "", "");
		one_list_Add.onAdd();

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

					Log.e("devIoT", pinName + " / " + flag);

					if (pinName.equals("Name")) {
						cellName = flag;
					}
				}
				one_list_Add = new List_Add(activity, one_arrayList, one_listView, false, cellName, arry2.toString());
				one_list_Add.onAdd();
				Log.e("devIoT", "OneClickElement : " + arry2.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(activity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
