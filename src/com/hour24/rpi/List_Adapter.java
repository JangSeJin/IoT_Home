package com.hour24.rpi;

import java.util.ArrayList;

import com.hour24.http.Http_Auto;
import com.hour24.http.Http_Control_CPU;
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
		TextView txtNicname, txtName, txtState;
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
			convertView = layoutInflater.inflate(R.layout.rpi_list_item, null);

			holder.txtNicname = (TextView) convertView.findViewById(R.id.txtNicname);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtState = (TextView) convertView.findViewById(R.id.txtState);
			holder.switchFlag = (SwitchCompat) convertView.findViewById(R.id.switchFlag);

			holder.txtNicname.setText(state.getNickName());
			holder.txtName.setText(" (" + state.getState() + "กษ)");
			holder.txtState.setText("Fan ");
			holder.switchFlag.setChecked(state.isAuto());

			switchFlagControl(position);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private void switchFlagControl(int position) {
		// TODO Auto-generated method stub
		state = stateList.get(position);

		holder.switchFlag.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				new Http_Control_CPU(Element.RPiActivity, isChecked).execute("");
			}
		});
	}

}
