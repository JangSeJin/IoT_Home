package com.hour24.oneclick;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.GridView;
import android.widget.ListView;

public class List_Add {

	Activity activity;
	boolean flag;
	String name;
	String others;

	ListView listView;

	ArrayList<List_States> arrayList;
	List_Adapter list_Adapter;
	List_States list_States;

	public List_Add(Activity activity, ArrayList<List_States> arrayList, ListView listView, boolean flag, String name,
			String others) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.listView = listView;
		this.flag = flag;
		this.name = name;
		this.others = others;
	}

	public void onAdd() {
		list_States = new List_States(flag, name, others);
		arrayList.add(list_States);
		list_Adapter = new List_Adapter(activity, R.layout.oneclick_list_item, arrayList);

		listView.setAdapter(list_Adapter);
	}
}
