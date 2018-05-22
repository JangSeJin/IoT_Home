package com.hour24.light;

import java.util.ArrayList;

import com.hour24.http.Http_Auto;
import com.hour24.http.Http_Control_Light;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class List_Adapter extends ArrayAdapter<List_States> {

	public static ArrayList<List_States> stateList;

	Context context;
	List_States state;
	ViewHolder holder;

	SwitchCompat switchAuto;

	@SuppressWarnings("static-access")
	public List_Adapter(Context context, int textViewResourceId, ArrayList<List_States> stateList) {
		super(context, textViewResourceId, stateList);
		this.context = context;
		this.stateList = new ArrayList<List_States>();
		this.stateList.addAll(stateList);
	}

	private class ViewHolder {
		LinearLayout layoutDevice;
		TextView txtNicname, txtName;
		SwitchCompat switchFlag;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		holder = null;
		// Log.v("devIoT", "getView : " + position);

		state = stateList.get(position);
		holder = new ViewHolder();

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.light_list_item, null);

			holder.layoutDevice = (LinearLayout) convertView.findViewById(R.id.layoutDevice);
			holder.txtNicname = (TextView) convertView.findViewById(R.id.txtNicname);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.switchFlag = (SwitchCompat) convertView.findViewById(R.id.switchFlag);

			holder.txtNicname.setText(state.getNickName());
			holder.txtName.setText(" (" + state.getName() + ")");

			switchControl(position);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private void switchControl(final int position) {
		// TODO Auto-generated method stub
		state = stateList.get(position);

		// 초기값 세팅
		holder.switchFlag.setChecked(state.isTurn());

		holder.switchFlag.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

				state = stateList.get(position);

				ToggleButton tgbtn2 = (ToggleButton) Element.LightActivity.findViewById(R.id.tgbtn2);

				// 조도센서 비활성화
				tgbtn2.setChecked(false);
				tgbtn2.setBackgroundResource(R.drawable.ic_sensor_off);

				// 조도센서를 죽인다.
				new Http_Control_Light(Element.LightActivity, "auto", null, false).execute("");

				new Http_Control_Light(Element.LightActivity, "turn", state.getName(), isChecked).execute("");
			}
		});

	}
}
