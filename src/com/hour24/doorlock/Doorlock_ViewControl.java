package com.hour24.doorlock;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;

public class Doorlock_ViewControl {

	public Doorlock_ViewControl() {

	}

	SwitchCompat switchFlag;

	public void onView() {

		if (Element.CurtainActivity != null) {

			switchFlag = (SwitchCompat) Element.CurtainActivity.findViewById(R.id.switchFlag);

			if (switchFlag != null) {

				try {
					String jsonData = new Http_State_Device(Element.CurtainActivity, "DoorlockState").execute("").get();
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

			// DoorlockState : {"Auto":"true","Turn":"LOW"}
			String Turn = json.getString("Turn");
			if (Turn.equals("LOW")) {
				switchFlag.setChecked(false);
			} else {
				switchFlag.setChecked(true);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.CurtainActivity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}
