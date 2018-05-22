package com.hour24.oneclick;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.MySharedPreferences;
import com.hour24.iot_home.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Dialog {

	Activity activity;
	View view;

	LinearLayout layoutAddDeviceInfoV;
	CheckBox[] chkDeviceInfo;
	TextView[] txtName;
	SwitchCompat[] switchFlag;
	EditText editName;
	String StateName = "";
	int deviceSize = 0;
	public static int whatisDeviceNum = 0;

	public static String jsonOneclick[] = new String[10];

	public Add_Dialog(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	public void onAdd() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.oneclick_dialog_add, null);

		editName = (EditText) view.findViewById(R.id.editName);
//		editName.setText("dd");
		editName.setHint("기능 이름을 입력하여 주시기 바랍니다.");

		layoutAddDeviceInfoV = (LinearLayout) view.findViewById(R.id.layoutAddDeviceInfoV);

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

		alertBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// Data를 넣어볼까

				// getInfo()했던 데이터를 다 가져옴
				try {

					JSONArray jsonArray = new JSONArray();
					JSONObject jsonObject = new JSONObject();

					JSONObject jsonObjectSub = new JSONObject();
					jsonObjectSub.put("pinName", "Name");
					jsonObjectSub.put("flag", editName.getText().toString());
					jsonArray.put(jsonObjectSub);

					for (int i = 0; i < deviceSize; i++) {
						if (jsonOneclick[i] != null) {

							JSONObject json = new JSONObject(jsonOneclick[i]);
							JSONArray state = json.getJSONArray("State");

							for (int j = 0; j < state.length(); j++) {
								JSONObject c = state.getJSONObject(j);
								String pinName = c.getString("pinName");
								String flag = c.getString("flag");
								Log.e("devIoT", "dialog 클릭 " + pinName + " / " + flag);

								jsonObjectSub = new JSONObject();
								jsonObjectSub.put("pinName", pinName);
								jsonObjectSub.put("flag", flag);

								jsonArray.put(jsonObjectSub);
							}
						} else {
						}
					}

					String jsonData = jsonObject.put("OneClickElement", jsonArray) + "";
					Log.e("devIoT", "최종데이터 : " + jsonData);

					new Add_Data(activity).onAdd(jsonObject);

					// 모든데이처 초기화
					for (int i = 0; i < deviceSize; i++) {
						jsonOneclick[i] = null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(activity, "데어티를 처리하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
				}

			}
		});

		alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new MySharedPreferences(activity).clear("OneClick");

				// 모든데이처 초기화
				for (int i = 0; i < deviceSize; i++) {
					jsonOneclick[i] = null;
				}
			}
		});

		// alertBuilder.setNegativeButton("취소", null);

		alertBuilder.setView(view);
		AlertDialog alertDialog = alertBuilder.create();

		showDevice();

		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
	}

	// 디바이스 정보 표시
	private void showDevice() {
		// TODO Auto-generated method stub
		String[] strNames = { "전등", "커튼", "보안", "도어락", "CCTV" };
		TextView[] txtDevcieName = new TextView[strNames.length];
		LinearLayout layoutDevices = (LinearLayout) view.findViewById(R.id.layoutDevices);

		for (int i = 0; i < strNames.length; i++) {
			txtDevcieName[i] = new TextView(activity);
			txtDevcieName[i].setTextColor(Color.parseColor("#000000"));
			txtDevcieName[i].setTextSize(16);
			txtDevcieName[i].setClickable(true);
			txtDevcieName[i].setText("#" + strNames[i] + "  ");
			layoutDevices.addView(txtDevcieName[i]);

			txtDevcieName[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub

					layoutAddDeviceInfoV.removeAllViews();

					if (editName.length() == 0) {
						Toast.makeText(activity, "기능 이름을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
					} else {

						String name = ((TextView) view).getText().toString().trim();
						StateName = "";
						Log.e("devIoT", "what click : " + name);

						try {
							if (name.equals("#전등")) {
								StateName = "LightState";
								whatisDeviceNum = 0;
							} else if (name.equals("#커튼")) {
								StateName = "CurtainState";
								whatisDeviceNum = 1;
							} else if (name.equals("#보안")) {
								StateName = "WindowsState";
								whatisDeviceNum = 2;
							} else if (name.equals("#도어락")) {
								StateName = "DoorlockState";
								whatisDeviceNum = 3;
							} else if (name.equals("#CCTV")) {
								StateName = "RaspicamState";
								whatisDeviceNum = 4;
							}

							Log.e("devIoT", jsonOneclick[whatisDeviceNum] + "");
							String jsonData = new Http_State_Device(activity, StateName).execute("").get();

							// 이미 선택된 것들이 있으면 새로운 정보를 안불러오고 저장된 정보를 불러옴
							if (jsonOneclick[whatisDeviceNum] == null) {
								jsonParse(jsonData, true);
							} else {
								jsonParse(jsonData, false);
							}

							Log.e("devIoT", jsonData);

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

		}
	}

	// flag == true : 새로운 정보
	// flag == false : 저장된 정보
	private void jsonParse(String jsonData, boolean flag) {
		// TODO Auto-generated method stub
		try {

			JSONObject json = new JSONObject(jsonData);
			JSONArray Device = json.getJSONArray("Device");

			deviceSize = Device.length();

			// CheckBox 배열 객체 생성
			chkDeviceInfo = new CheckBox[Device.length()];
			// TextView 배열 객체 생성
			txtName = new TextView[Device.length()];
			// Switch 객체 생성
			switchFlag = new SwitchCompat[Device.length()];

			Log.e("devIoT", "AddDialog 배열크기 : " + Device.length());

			for (int i = 0; i < Device.length(); i++) {
				JSONObject c = Device.getJSONObject(i);

				String PinName = c.getString("PinName");
				String NickName = c.getString("NickName");
				Log.e("devIoT", "AddDialog : PinName : " + PinName + " / NickName : " + NickName);

				chkDeviceInfo[i] = new CheckBox(activity);
				txtName[i] = new TextView(activity);
				switchFlag[i] = new SwitchCompat(activity);

				// isAuto의 이름이 이상해서 바꿔줌
				if (PinName.equals("isAuto") || PinName.equals("all")) {
					// chkDeviceInfo[i].setText("자동 ");
					// txtName[i].setText("");
				} else {
					chkDeviceInfo[i].setText(NickName + " ");
					txtName[i].setText(PinName);

					// 체크박스, 이름, switch를 뿌려준다.
					LinearLayout layoutH = new LinearLayout(activity);
					LinearLayout layoutH1 = new LinearLayout(activity);
					layoutH.setLayoutParams(
							new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					layoutH.setOrientation(LinearLayout.HORIZONTAL);

					LinearLayout layoutH2 = new LinearLayout(activity);
					layoutH2.setLayoutParams(
							new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					layoutH2.setOrientation(LinearLayout.HORIZONTAL);
					layoutH2.setGravity(Gravity.RIGHT);

					layoutH1.addView(chkDeviceInfo[i]);
					layoutH1.addView(txtName[i]);
					layoutH2.addView(switchFlag[i]);

					layoutH.addView(layoutH1);
					layoutH.addView(layoutH2);

					layoutAddDeviceInfoV.addView(layoutH);

					chkDeviceInfo[i].setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							getInfo();
						}
					});

					switchFlag[i].setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							getInfo();
						}
					});
				}

				// 이미 선택된 값들 처리
				if (!flag) {
					String jsonDataAlready = jsonOneclick[whatisDeviceNum];
					JSONObject json2 = new JSONObject(jsonDataAlready);
					JSONArray State = json2.getJSONArray("State");

					for (int j = 0; j < State.length(); j++) {
						JSONObject a = State.getJSONObject(j);
						String pinName2 = a.getString("pinName");
						boolean flag2 = Boolean.parseBoolean(a.getString("flag"));

						// 선택된 값하고 나열된 pinName하고 같으면 처리를 한다.
						if (txtName[i].getText().toString().equals(pinName2)) {
							chkDeviceInfo[i].setChecked(true);
							switchFlag[i].setChecked(flag2);
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(activity, "데이터 처리를 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	// checkbox, switch에서 클릭된 정보를 가져온다.
	private void getInfo() {
		// TODO Auto-generated method stub

		try {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();

			// oneclick기능의 이름을 미리 입력한다.
			Log.e("devIoT", "device size : " + deviceSize + "");
			for (int i = 0; i < deviceSize; i++) {
				if (chkDeviceInfo[i].isChecked() == true) {

					String pinName = txtName[i].getText().toString();
					boolean flag = switchFlag[i].isChecked();

					Log.e("devIoT", pinName + " / " + flag);
					JSONObject jsonObjectSub = new JSONObject();
					jsonObjectSub.put("pinName", pinName);
					jsonObjectSub.put("flag", flag + "");

					jsonArray.put(jsonObjectSub);

				} else {
				}
			}
			jsonOneclick[whatisDeviceNum] = jsonObject.put("State", jsonArray).toString();
			Log.e("devIoT", "whatisDeviceNum? : " + whatisDeviceNum + " / " + jsonOneclick[whatisDeviceNum]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
