package com.hour24.curtain;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class Curtain_ViewControl {

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	GridView gridView;

	public Curtain_ViewControl() {

	}

	public void onView() {

		if (Element.CurtainActivity != null) {

			gridView = (GridView) Element.CurtainActivity.findViewById(R.id.gridView);

			if (gridView != null) {

				arrayList = new ArrayList<List_States>();

				list_Add = new List_Add(Element.CurtainActivity, arrayList, gridView, "", Curtain_Main.curtainAll,
						"모튼커튼");
				list_Add.onAdd();

				try {
					String jsonData = new Http_State_Device(Element.CurtainActivity, "CurtainState").execute("").get();
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
				String Turn = c.getString("Turn");
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // auto 체크
					// switchAuto.setChecked(Boolean.parseBoolean(Turn));
				} else { // 현재 상태
					list_Add = new List_Add(Element.CurtainActivity, arrayList, gridView, PinName,
							Integer.parseInt(Turn), NickName);
					list_Add.onAdd();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.CurtainActivity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
