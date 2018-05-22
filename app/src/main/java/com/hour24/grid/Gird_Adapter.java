package com.hour24.grid;

import java.util.ArrayList;

import com.hour24.curtain.Curtain_Main;
import com.hour24.http.Http_Control_Light;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;
import com.hour24.light.Light_Main;
import com.hour24.security.Security_Main;
import com.hour24.weather.Weather_Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Gird_Adapter extends ArrayAdapter<Grid_States> {

	public static ArrayList<Grid_States> stateList;

	Context context;
	Grid_States state;
	ViewHolder holder;

	SwitchCompat switchAuto;

	@SuppressWarnings("static-access")
	public Gird_Adapter(Context context, int textViewResourceId, ArrayList<Grid_States> stateList) {
		super(context, textViewResourceId, stateList);
		this.context = context;
		this.stateList = new ArrayList<Grid_States>();
		this.stateList.addAll(stateList);
	}

	private class ViewHolder {
		LinearLayout layoutGrid;
		ImageView imgIcon;
		TextView txtDevice, txtState;
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
			convertView = layoutInflater.inflate(R.layout.grid_list_item, null);

			holder.layoutGrid = (LinearLayout) convertView.findViewById(R.id.layoutGrid);
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.txtDevice = (TextView) convertView.findViewById(R.id.txtDevice);
			holder.txtState = (TextView) convertView.findViewById(R.id.txtState);

			holder.imgIcon.setImageResource(state.getImg());
			holder.txtDevice.setText(state.getDevice());
			holder.txtState.setText(state.getState());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

}
