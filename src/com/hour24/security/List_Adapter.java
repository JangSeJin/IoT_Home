package com.hour24.security;

import java.util.ArrayList;

import com.hour24.http.Http_Control_Motion;
import com.hour24.http.Http_Control_Security;
import com.hour24.http.Http_Control_Windows;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class List_Adapter extends ArrayAdapter<List_States> {

	public static ArrayList<List_States> stateList;

	Context context;
	List_States state;
	ViewHolder holder;

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
		Log.v("devSign", "getView : " + position);

		state = stateList.get(position);
		holder = new ViewHolder();

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.security_list_item, null);

			holder.txtNicname = (TextView) convertView.findViewById(R.id.txtNicname);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtState = (TextView) convertView.findViewById(R.id.txtState);
			holder.switchFlag = (SwitchCompat) convertView.findViewById(R.id.switchFlag);

			holder.txtNicname.setText(state.getNickName());
			holder.txtName.setText(" (" + state.getName() + ")");

			// state가 ""일때 ●를 지운다.
			if (state.getState().equals("")) {
				holder.txtState.setText("");
			}

			// switch 세팅
			holder.switchFlag.setChecked(state.isFlag());

			if (state.getNickName().equals("통합보안")) {

			} else if (state.getNickName().equals("도어락")) {
				// low가 닫힌거
				if (state.getState().equals("LOW")) {
					holder.txtState.setTextColor(Color.parseColor("#00ff00"));
				} else {
					holder.txtState.setTextColor(Color.parseColor("#ff0000"));
				}
				holder.switchFlag.setVisibility(View.GONE);
			} else if (state.getNickName().equals("창문열림감지")) {
				if (state.getState().equals("CLOSE")) {
					holder.txtState.setTextColor(Color.parseColor("#00ff00"));
				} else {
					holder.txtState.setTextColor(Color.parseColor("#ff0000"));
				}
				holder.switchFlag.setVisibility(View.GONE);
			} else if (state.getNickName().equals("모션감지")) {
				if (state.getState().equals("LOW")) { // 움직임x, 비감지
					holder.txtState.setTextColor(Color.parseColor("#00ff00"));
				} else {
					holder.txtState.setTextColor(Color.parseColor("#ff0000"));
				}
				holder.switchFlag.setVisibility(View.GONE);
			} else {

			}

			holder.switchFlag.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					state = stateList.get(position);

					if (state.getNickName().equals("통합보안")) {
						new Http_Control_Security(Element.SecurityActivity, isChecked).execute("");
					} else if (state.getNickName().equals("도어락")) {

					} else if (state.getNickName().equals("창문열림감지")) {
						new Http_Control_Windows(Element.SecurityActivity, isChecked).execute("");
					} else if (state.getNickName().equals("모션감지")) {
						new Http_Control_Motion(Element.SecurityActivity, isChecked).execute("");
					}

				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

}
