package com.hour24.light;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.curtain.Curtain_ViewControl;
import com.hour24.http.Http_Control_Light;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class Light_Main extends Fragment implements OnCheckedChangeListener {

	private static int section_number = 0;
	public static String alramFlag = null;

	public Light_Main() {
	}

	public static Light_Main newInstance(int section_number) {
		// TODO Auto-generated method stub
		Light_Main fragment = new Light_Main();
		Light_Main.section_number = section_number;
		return fragment;
	}

	// 기능관련 변수 저장
	public static boolean boollightSensor = false;
	public static boolean booallLight = false;

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;
	GridView gridView;
	ToggleButton tgbtn1, tgbtn2, tgbtn3;

	int deviceLength = 0;
	int hour = 0, min = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.light_main, container, false);
		Element.LightActivity = Light_Main.this.getActivity();

		listView = (ListView) view.findViewById(R.id.listView);
		gridView = (GridView) view.findViewById(R.id.gridView);
		tgbtn1 = (ToggleButton) view.findViewById(R.id.tgbtn1);
		tgbtn2 = (ToggleButton) view.findViewById(R.id.tgbtn2);
		tgbtn3 = (ToggleButton) view.findViewById(R.id.tgbtn3);

		tgbtn1.setOnCheckedChangeListener(this);
		tgbtn2.setOnCheckedChangeListener(this);
		tgbtn3.setOnCheckedChangeListener(this);

		arrayList = new ArrayList<List_States>();

		try {
			String jsonData = new Http_State_Device(Light_Main.this.getActivity(), "LightState").execute("").get();
			Log.e("devIoT", jsonData);
			jsonParse(jsonData);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 알람 상태 컨트롤
		// new SetAlramBtnControl(Light_Main.this.getActivity(), "light",
		// btnAlram).onChange();

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
		Log.e("devIoT", "Light_Main onPause");
		Element.LightActivity = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "Light_Main onResume");
		Element.LightActivity = Light_Main.this.getActivity();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.tgbtn1:

			new Http_Control_Light(Light_Main.this.getActivity(), "turn", "all", isChecked).execute("");
			tgbtn2.setBackgroundResource(R.drawable.ic_sensor_off);

			break;
		case R.id.tgbtn2:

			// 모든전등 비활성화
			tgbtn1.setChecked(false);
			tgbtn1.setBackgroundResource(R.drawable.ic_power_off);

			// 조도센서 활성화
			new Http_Control_Light(Light_Main.this.getActivity(), "auto", null, isChecked).execute("");

			if (isChecked) {
				tgbtn2.setBackgroundResource(R.drawable.ic_sensor_on);
			} else {
				tgbtn2.setBackgroundResource(R.drawable.ic_sensor_off);
			}
			break;
		case R.id.tgbtn3:
			if (isChecked) {
				tgbtn3.setBackgroundResource(R.drawable.ic_alram_on);
			} else {
				tgbtn3.setBackgroundResource(R.drawable.ic_alram_off);
			}
			break;
		}
	}

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			deviceLength = Device.length() - 1; // json 크기 , 모든 전등 컨트롤

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				boolean Turn = Boolean.parseBoolean(c.getString("Turn"));
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // Auto 정보 받아오기 위해서 트릭을 사용함
					tgbtn2.setChecked(Turn);
				} else if (PinName.equals("all")) {
					if (Turn) {
						tgbtn1.setBackgroundResource(R.drawable.ic_power_on);
					} else {
						tgbtn1.setBackgroundResource(R.drawable.ic_power_off);
					}
				} else {
					list_Add = new List_Add(Light_Main.this.getActivity(), arrayList, listView, PinName, Turn,
							NickName);
					list_Add.onAdd();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Light_Main.this.getActivity(), "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

}