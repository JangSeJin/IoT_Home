package com.hour24.curtain;

import java.util.ArrayList;

import com.hour24.http.Http_Control_Curtain;
import com.hour24.http.Http_Control_Curtain_All;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
		LinearLayout layoutGrid, layoutDevice;
		SeekBar seekBar;
		TextView txtNicname, txtName;
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
			convertView = layoutInflater.inflate(R.layout.curtain_grid_item, null);

			holder.layoutGrid = (LinearLayout) convertView.findViewById(R.id.layoutGrid);
			holder.layoutDevice = (LinearLayout) convertView.findViewById(R.id.layoutDevice);
			holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
			holder.txtNicname = (TextView) convertView.findViewById(R.id.txtNicname);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

			holder.txtNicname.setText(state.getNickName());
			holder.txtName.setText(state.getName());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		seekbarViewInIt(position);
		seekbarClickControl(position);
		// StepButtnControl(position);

		return convertView;
	}

	private void seekbarViewInIt(int position) {
		// TODO Auto-generated method stub
		state = stateList.get(position);

		holder.seekBar.setProgress(state.getTurn());

		if (state.getNickName().equals("모튼커튼")) {
			holder.seekBar.setProgressDrawable(ResourcesCompat.getDrawable(Element.CurtainActivity.getResources(),
					R.drawable.style_seekbar_progress_all, null));
		} else {
			holder.txtName.setVisibility(View.VISIBLE);
		}

	}

	private void seekbarClickControl(final int position) {
		// TODO Auto-generated method stub

		holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			// seekbar 터치 후
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.e("devIoT", "onStopTrackingTouch : " + state.getName() + " / " + seekBar.getProgress());

				state = stateList.get(position);

				if (state.getName().equals("")) {
					new Http_Control_Curtain_All(Element.CurtainActivity, "curtain", seekBar.getProgress()).execute(2);
					Curtain_Main.curtainAll = seekBar.getProgress();
					// new ProgressBar_LoadingInfo(Element.CurtainActivity,
					// 3000, "잠시만 기다려 주시기 바랍니다.").execute("");
				} else {
					new Http_Control_Curtain(Element.CurtainActivity, state.getName(), seekBar.getProgress())
							.execute("");
				}
			}

			// seekbar 터치
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.e("devIoT", "onStartTrackingTouch : " + state.getName() + " / " + seekBar.getProgress());
			}

			// seekbar를 움직였을때
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				Log.e("devIoT", "onProgressChanged : " + state.getName() + " / " + seekBar.getProgress());

			}
		});
	}
}
