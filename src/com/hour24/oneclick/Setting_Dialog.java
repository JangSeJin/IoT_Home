package com.hour24.oneclick;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hour24.http.Http_State_Device;
import com.hour24.iot_home.Element;
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

public class Setting_Dialog {

	Activity activity;
	View view;
	String originalData;

	LinearLayout layoutAddDeviceInfoV;
	CheckBox[] chkDeviceInfo;
	TextView[] txtName;
	SwitchCompat[] switchFlag;
	EditText editName;
	String StateName = "";
	int deviceSize = 0;
	public static int whatisDeviceNum = 0;

	public static String jsonOneclick[] = new String[10];

	public Setting_Dialog(String originalData) {
		// TODO Auto-generated construc tor stub
		this.activity = Element.OneClickActivity;
		this.originalData = originalData;
	}

	public void onDialog() {
		// TODO Auto-generated method stub

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.oneclick_dialog_add, null);
		editName = (EditText) view.findViewById(R.id.editName);
		layoutAddDeviceInfoV = (LinearLayout) view.findViewById(R.id.layoutAddDeviceInfoV);

		// 제목 설정
		try {
			JSONArray array = new JSONArray(originalData);
			JSONObject c = array.getJSONObject(0);
			editName.setText(c.getString("flag"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

		alertBuilder.setPositiveButton("수정", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				// share데이터 - 클릭한데이터 = 클릭데이터가 없어진 데이터
				// 수정한 데이터 + 클릭데이터가 없어진 데이터 = 완성데이터

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
					Log.e("devIoT", "최종 수정 데이터 : " + jsonData);

					new Modify_Data(activity).onModify(jsonObject, originalData);

					// Toast.makeText(activity, "데이터 수정을 완료하였습니다.",
					// Toast.LENGTH_SHORT).show();

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

		alertBuilder.setNeutralButton("삭제", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				new Delete_Data(activity).onDelete(originalData);

				// 모든데이처 초기화
				for (int i = 0; i < deviceSize; i++) {
					jsonOneclick[i] = null;
				}
			}
		});

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

		// 먼저 전등을 보여준다.
		try {
			String jsonData = new Http_State_Device(activity, "LightState").execute("").get();
			whatisDeviceNum = 0;
			Log.e("devIoT", jsonData);
			jsonParse(jsonData);
			getInfo();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

							Log.e("devIoT", "jsonOneclick[whatisDeviceNum] : " + jsonOneclick[whatisDeviceNum] + "");
							String jsonData = new Http_State_Device(activity, StateName).execute("").get();

							// 이미 선택된 것들이 있으면 새로운 정보를 안불러오고 저장된 정보를 불러옴
							jsonParse(jsonData);

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
	private void jsonParse(String jsonData) {
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
				if (PinName.equals("isAuto")) {
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

				// 데이터를 새로 못넣었고, 이미 저장된 값을 계속 뿌린다.
				if (jsonOneclick[whatisDeviceNum] != null) {
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

				} else {
					// 저장된 데이터를 가져와서 체크박스를 체크한다.
					try {
						JSONArray array = new JSONArray(originalData);

						for (int j = 1; j < array.length(); j++) {
							JSONObject a = array.getJSONObject(j);
							String pinName = a.getString("pinName");
							String flag2 = a.getString("flag");

							if (pinName.equals(txtName[i].getText().toString())) {
								chkDeviceInfo[i].setChecked(true);
								switchFlag[i].setChecked(Boolean.parseBoolean(flag2));
							}

							Log.e("devIoT", "이전에 먼저 저장된 선택된 값 : " + pinName + " / " + flag2);
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
