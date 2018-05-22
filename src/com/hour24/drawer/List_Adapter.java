package com.hour24.drawer;

import java.util.ArrayList;

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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
		LinearLayout layoutTop, layoutBottom;
		TextView txtName, txtTopName;
		ImageView imgIcon;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		holder = null;
		// Log.v("devIoT", "getView menu : " + position);

		state = stateList.get(position);
		holder = new ViewHolder();

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);

			holder.layoutTop = (LinearLayout) convertView.findViewById(R.id.layoutTop);
			holder.layoutBottom = (LinearLayout) convertView.findViewById(R.id.layoutBottom);
			holder.txtTopName = (TextView) convertView.findViewById(R.id.txtTopName);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

			// 메뉴 세팅
			menuControl(position);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private void menuControl(int position) {
		// TODO Auto-generated method stub
		state = stateList.get(position);
		if (state.getName().contains("#")) {
			holder.layoutTop.setVisibility(View.VISIBLE);
			holder.layoutBottom.setVisibility(View.GONE);
			holder.txtTopName.setText(state.getName().replace("#", ""));

			holder.imgIcon.setVisibility(View.GONE);
		} else {
			holder.layoutTop.setVisibility(View.GONE);
			holder.layoutBottom.setVisibility(View.VISIBLE);
			holder.txtName.setText(state.getName());

			holder.imgIcon.setImageResource(state.getImg());
		}
	}

}
