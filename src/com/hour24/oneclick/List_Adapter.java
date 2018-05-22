package com.hour24.oneclick;

import java.util.ArrayList;

import com.hour24.http.Http_Auto;
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
import android.widget.Toast;
import android.widget.ToggleButton;

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
		LinearLayout layoutAdd, layoutShow;
		TextView txtName;
		ImageView imgSetting;
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
			convertView = layoutInflater.inflate(R.layout.oneclick_list_item, null);

			holder.layoutAdd = (LinearLayout) convertView.findViewById(R.id.layoutAdd);
			holder.layoutShow = (LinearLayout) convertView.findViewById(R.id.layoutShow);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.imgSetting = (ImageView) convertView.findViewById(R.id.imgSetting);

			holder.txtName.setText(state.getName());

			holder.imgSetting.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("devIoT", "OneClickSetting");
					state = stateList.get(position);
					new Setting_Dialog(state.getjsonArray()).onDialog();
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		isAddListView(position);

		return convertView;
	}

	private void isAddListView(int position) {
		// TODO Auto-generated method stub

		state = stateList.get(position);

		if (state.isFlag()) {
			holder.layoutAdd.setVisibility(View.VISIBLE);
			holder.layoutShow.setVisibility(View.GONE);
		} else {
			holder.layoutAdd.setVisibility(View.GONE);
			holder.layoutShow.setVisibility(View.VISIBLE);
		}

	}
}
