package com.hour24.oneclick;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_Control_OneClick;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.MySharedPreferences;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class OneClick_Main extends Fragment {

	private static int section_number = 0;

	public OneClick_Main() {
	}

	public static OneClick_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		OneClick_Main fragment = new OneClick_Main();
		OneClick_Main.section_number = section_number;
		return fragment;
	}

	List_Add one_list_Add;
	ArrayList<List_States> one_arrayList;

	ListView one_listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.oneclick_main, container, false);
		Element.OneClickActivity = OneClick_Main.this.getActivity();

		one_listView = (ListView) view.findViewById(R.id.listView);

		one_arrayList = new ArrayList<List_States>();
		one_list_Add = new List_Add(OneClick_Main.this.getActivity(), one_arrayList, one_listView, true, "", "");
		one_list_Add.onAdd();

		MySharedPreferences mySharedPreferences = new MySharedPreferences(OneClick_Main.this.getActivity());
		String jsonData = mySharedPreferences.getValue("OneClick", "");
		if (!jsonData.equals("") || !jsonData.equals("false")) {
			jsonParse(jsonData);
		}
		Log.e("devIoT", "OneClick : " + jsonData);

		one_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				if (position == 0) { // Add
					Log.e("devIoT", "oneclick Add");
					new Add_Dialog(OneClick_Main.this.getActivity()).onAdd();
				} else {
					ArrayList<List_States> arrayList = List_Adapter.stateList;
					List_States state = arrayList.get(position);
					Log.e("devIoT", "listClick : " + state.getjsonArray());

					new Http_Control_OneClick(OneClick_Main.this.getActivity(), state.getjsonArray()).execute("");

					Toast.makeText(OneClick_Main.this.getActivity(), "'" + state.getName() + "' 실행하였습니다.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(section_number);
	}

	// 리스트뷰에 추가
	private void jsonParse(String jsonData) {
		// TODO Auto-generated method stub

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
				one_list_Add = new List_Add(OneClick_Main.this.getActivity(), one_arrayList, one_listView, false,
						cellName, arry2.toString());
				one_list_Add.onAdd();
				Log.e("devIoT", "OneClickElement : " + arry2.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// Toast.makeText(OneClick_Main.this.getActivity(), "데이터 처리를
			// 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}
}