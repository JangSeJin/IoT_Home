package com.hour24.drawer;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.ListView;

public class List_Add {

	Activity activity;
	String name;
	int img;

	ListView listView;

	ArrayList<List_States> arrayList;
	List_Adapter list_Adapter;
	List_States list_States;

	public List_Add(Activity activity, ArrayList<List_States> arrayList, ListView listView, String name, int img) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.listView = listView;
		this.name = name;
		this.img = img;
	}

	public void onAdd() {
		list_States = new List_States(name, img);
		arrayList.add(list_States);
		list_Adapter = new List_Adapter(activity, R.layout.drawer_list_item, arrayList);

		listView.setAdapter(list_Adapter);
	}
}
