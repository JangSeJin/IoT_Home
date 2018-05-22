package com.hour24.grid;

import java.util.ArrayList;

import com.hour24.cctv.CCTV_Main;
import com.hour24.curtain.Curtain_Main;
import com.hour24.doorlock.DoorLock_Main;
import com.hour24.innertemperature.Innertemperature_Main;
import com.hour24.iot_home.Element;
import com.hour24.iot_home.MainActivity;
import com.hour24.iot_home.R;
import com.hour24.light.Light_Main;
import com.hour24.oneclick.OneClick_Main;
import com.hour24.rpi.RPi_Main;
import com.hour24.security.Security_Main;
import com.hour24.weather.Weather_Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

@SuppressLint("NewApi")
public class Grid_Main extends Fragment {

	private static int section_number = 0;

	public Grid_Main() {
	}

	public static Grid_Main newInstance(int section_number) {
		// TODO Auto-generated method stub

		Grid_Main fragment = new Grid_Main();
		Grid_Main.section_number = section_number;
		return fragment;
	}

	String[] menu = new String[] { "전등", "커튼", "CCTV", "도어락", "보안", "날씨정보", "실내온도", "라즈베리파이", "OneClick" };
	int[] img = new int[] { R.drawable.ic_light, R.drawable.ic_curtain, R.drawable.ic_cctv, R.drawable.ic_doorlock,
			R.drawable.ic_security, R.drawable.ic_weather, R.drawable.ic_temperature, R.drawable.ic_rpi,
			R.drawable.ic_start };

	Grid_Add grid_Add;
	ArrayList<Grid_States> arrayList;

	EditText editURL;
	GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.grid_main, container, false);

		editURL = (EditText) view.findViewById(R.id.editURL);
		gridView = (GridView) view.findViewById(R.id.gridView);

		editURL.setText(Element.URL);

		arrayList = new ArrayList<Grid_States>();

		for (int i = 0; i < menu.length; i++) {
			grid_Add = new Grid_Add(Grid_Main.this.getActivity(), arrayList, gridView, img[i], menu[i], "");
			grid_Add.onAdd();
		}

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				switch (position) {
				case 0:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, Light_Main.newInstance(position + 1)).commit();
					break;
				case 1:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, Curtain_Main.newInstance(position + 1)).commit();
					break;
				case 2:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, CCTV_Main.newInstance(position + 1)).commit();
					break;
				case 3:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, DoorLock_Main.newInstance(position + 1)).commit();
					break;
				case 4:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, Security_Main.newInstance(position + 1)).commit();
					break;
				case 5:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, Weather_Main.newInstance(position + 3)).commit();
					break;
				case 6:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, Innertemperature_Main.newInstance(position + 3)).commit();
					break;
				case 7:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, RPi_Main.newInstance(position + 3)).commit();
					break;
				case 8:
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, OneClick_Main.newInstance(position + 2)).commit();
					break;

				default:
					break;
				}
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(section_number);
	}
}