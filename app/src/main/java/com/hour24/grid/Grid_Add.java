package com.hour24.grid;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.GridView;
import android.widget.ListView;

public class Grid_Add {

	Activity activity;
	GridView gridView;

	ArrayList<Grid_States> arrayList;
	Gird_Adapter grid_Adapter;
	Grid_States grid_States;

	int img;
	String device;
	String state;

	public Grid_Add(Activity activity, ArrayList<Grid_States> arrayList, GridView gridView, int img, String device,
			String state) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.gridView = gridView;
		this.img = img;
		this.device = device;
		this.state = state;
	}

	public void onAdd() {
		grid_States = new Grid_States(img, device, state);
		arrayList.add(grid_States);
		grid_Adapter = new Gird_Adapter(activity, R.layout.grid_list_item, arrayList);

		gridView.setAdapter(grid_Adapter);
	}
}
