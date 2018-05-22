package com.hour24.rpi;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
public class RPi_Main extends Fragment {

	private static int section_number = 0;

	public RPi_Main() {
	}

	public static RPi_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		RPi_Main fragment = new RPi_Main();
		RPi_Main.section_number = section_number;
		return fragment;
	}

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rpi_main, container, false);
		Element.RPiActivity = RPi_Main.this.getActivity();

		listView = (ListView) view.findViewById(R.id.listView);

		arrayList = new ArrayList<List_States>();

		// list_Add = new List_Add(RPi_Main.this.getActivity(), arrayList,
		// listView, "fan-01", "온도", "45", true, true);
		// list_Add.onAdd();

		try {
			String jsonData = new Http_State_Device(RPi_Main.this.getActivity(), "CPUState").execute("").get();
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
		Log.e("devIoT", "RPi_Main onPause");
		Element.RPiActivity = null;
	}

	@Override
	public void onResume() {
		super.onPause();
		Log.e("devIoT", "RPi_Main onPause");
		Element.RPiActivity = RPi_Main.this.getActivity();
	}

	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub

		try {

			JSONObject json = new JSONObject(jsonData);

			String CPU_TEMP = json.getString("CPU_TEMP");
			boolean Turn = Boolean.parseBoolean(json.getString("Turn")); // fan
																			// on/off
			boolean Auto = Boolean.parseBoolean(json.getString("Auto")); // fan
																			// auto
																			// on/off

			list_Add = new List_Add(RPi_Main.this.getActivity(), arrayList, listView, "fan-01", "온도", CPU_TEMP, Turn,
					Auto);
			list_Add.onAdd();

			Log.e("devIoT", "cpu : " + Auto + "(auto) / " + CPU_TEMP + " / " + Turn + "(on,off");

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(RPi_Main.this.getActivity(), "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}