package com.hour24.security;

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

public class Security_ViewControl {

	List_Add list_Add;
	ArrayList<List_States> arrayList;

	ListView listView;

	public Security_ViewControl() {
		// TODO Auto-generated constructor stub
	}

	public void onView() {

		if (Element.SecurityActivity != null) {

			listView = (ListView) Element.SecurityActivity.findViewById(R.id.listView);

			if (listView != null) {

				arrayList = new ArrayList<List_States>();

				try {
					// ������ �Ǿ��ִ��� üũ
					String jsonData = new Http_State_Device(Element.SecurityActivity, "ConfigState").execute("").get();
					Log.e("devIoT", jsonData);
					jsonParse1(jsonData);

					// �����
					jsonData = new Http_State_Device(Element.SecurityActivity, "DoorlockState").execute("").get();
					jsonParse2(jsonData);
					Log.e("devIoT", jsonData);

					// â����������
					jsonData = new Http_State_Device(Element.SecurityActivity, "WindowsState").execute("").get();
					jsonParse3(jsonData);
					Log.e("devIoT", jsonData);

					// ��ǰ���
					jsonData = new Http_State_Device(Element.SecurityActivity, "MotionState").execute("").get();
					jsonParse4(jsonData);
					Log.e("devIoT", jsonData);

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

	// ���պ���
	private void jsonParse1(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Warning = Boolean.parseBoolean(json.getString("Warning"));

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "security-01", "���պ���", "", Warning);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "���պ��� ������ ó���� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	}

	// �����
	private void jsonParse2(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			String Turn = json.getString("Turn");

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "doorlock-01", "�����", Turn, false);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "������ ó���� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	}

	// â����������
	private void jsonParse3(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			boolean sensoerflag = false;
			String Turn = "";

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				Turn = c.getString("Turn"); // isAuto�϶� ������ �������� / �ƴҶ� open,
											// close
				String NickName = c.getString("NickName");

				if (PinName.equals("isAuto")) { // Auto ���� �޾ƿ��� ���ؼ� Ʈ���� �����
					sensoerflag = Boolean.parseBoolean(Turn);
				} else {

				}

			}

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "window-01", "â����������", Turn, false);
			list_Add.onAdd();

			Log.e("devIoT", "â������ ����  : " + Turn + " / " + sensoerflag);

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "â���������� ������ ó���� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	}

	// ��ǰ���
	private void jsonParse4(String jsonData) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);

			boolean Auto = Boolean.parseBoolean(json.getString("Auto"));
			String Turn = json.getString("Turn");
			// Turn�� LOW == ������X, HIGH == ������ O

			list_Add = new List_Add(Element.SecurityActivity, arrayList, listView, "motion-01", "��ǰ���", Turn, false);
			list_Add.onAdd();

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(Element.SecurityActivity, "��ǰ��� ������ ó���� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	}
}
