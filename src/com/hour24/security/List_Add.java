package com.hour24.security;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.ListView;

public class List_Add {

	Activity activity;
	String name;
	String NickName;
	String state;
	boolean flag;

	ListView listView;

	ArrayList<List_States> arrayList;
	List_Adapter list_Adapter;
	List_States list_States;

	public List_Add(Activity activity, ArrayList<List_States> arrayList, ListView listView, String name,
			String NickName, String state, boolean flag) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.listView = listView;
		this.name = name;
		this.NickName = NickName;
		this.state = state;
		this.flag = flag;
	}

	public void onAdd() {
		list_States = new List_States(name, NickName, state, flag);
		arrayList.add(list_States);
		list_Adapter = new List_Adapter(activity, R.layout.security_list_item, arrayList);

		listView.setAdapter(list_Adapter);
	}
}
