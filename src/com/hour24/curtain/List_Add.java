package com.hour24.curtain;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.GridView;
import android.widget.ListView;

public class List_Add {

	Activity activity;
	String name;
	int turn;
	String NickName;

	GridView gridView;

	ArrayList<List_States> arrayList;
	List_Adapter list_Adapter;
	List_States list_States;

	public List_Add(Activity activity, ArrayList<List_States> arrayList, GridView gridView, String name, int turn,
			String NickName) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.gridView = gridView;
		this.name = name;
		this.turn = turn;
		this.NickName = NickName;
	}

	public void onAdd() {
		list_States = new List_States(name, turn, NickName);
		arrayList.add(list_States);
		list_Adapter = new List_Adapter(activity, R.layout.curtain_grid_item, arrayList);

		gridView.setAdapter(list_Adapter);
	}
}
