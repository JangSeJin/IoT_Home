package com.hour24.rpi;

import java.util.ArrayList;

import com.hour24.iot_home.R;

import android.app.Activity;
import android.widget.GridView;
import android.widget.ListView;

public class List_Add {

	Activity activity;
	String name;
	String NickName;
	String state;
	boolean turn;
	boolean auto;

	ListView listView;

	ArrayList<List_States> arrayList;
	List_Adapter list_Adapter;
	List_States list_States;

	public List_Add(Activity activity, ArrayList<List_States> arrayList, ListView listView, String name,
			String NickName, String state, boolean turn, boolean auto) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.arrayList = arrayList;
		this.listView = listView;
		this.name = name;
		this.NickName = NickName;
		this.state = state;
		this.turn = turn;
		this.auto = auto;
	}

	public void onAdd() {
		list_States = new List_States(name, NickName, state, turn, auto);
		arrayList.add(list_States);
		list_Adapter = new List_Adapter(activity, R.layout.rpi_list_item, arrayList);

		listView.setAdapter(list_Adapter);
	}
}
