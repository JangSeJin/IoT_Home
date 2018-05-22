package com.hour24.curtain;

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
import android.widget.GridView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Curtain_Main extends Fragment {

	private static int section_number = 0;

	public Curtain_Main() {
	}

	public static Curtain_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		Curtain_Main fragment = new Curtain_Main();
		Curtain_Main.section_number = section_number;
		return fragment;
	}

	public static int curtainAll = 0;

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	GridView gridView;

	int deviceLength = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.curtain_main, container, false);
		Element.CurtainActivity = Curtain_Main.this.getActivity();

		gridView = (GridView) view.findViewById(R.id.gridView);

		arrayList = new ArrayList<List_States>();

		list_Add = new List_Add(Curtain_Main.this.getActivity(), arrayList, gridView, "", 0, "모든커튼");
		list_Add.onAdd();

		try {
			String jsonData = new Http_State_Device(Curtain_Main.this.getActivity(), "CurtainState").execute("").get();
			Log.e("devIoT", jsonData);
			jsonParse(jsonData);
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
		Log.e("devIoT", "Curtain_Main onPause");
		Element.CurtainActivity = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("devIoT", "Curtain_Main onResume");
		Element.CurtainActivity = Curtain_Main.this.getActivity();
	}

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			deviceLength = Device.length() - 1;

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				String Turn = c.getString("Turn");
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // auto 체크
					// switchAuto.setChecked(Boolean.parseBoolean(Turn));
				} else { // 현재 상태
					list_Add = new List_Add(Curtain_Main.this.getActivity(), arrayList, gridView, PinName,
							Integer.parseInt(Turn), NickName);
					list_Add.onAdd();
				}

				Log.e("devIoT", "curtain : " + PinName + " / " + Turn);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Curtain_Main.this.getActivity(), "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}