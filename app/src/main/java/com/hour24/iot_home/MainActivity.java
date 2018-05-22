package com.hour24.iot_home;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.gcm.GCMRegistrar;
import com.hour24.cctv.CCTV_Main;
import com.hour24.curtain.Curtain_Main;
import com.hour24.doorlock.DoorLock_Main;
import com.hour24.drawer.List_Add;
import com.hour24.drawer.List_States;
import com.hour24.grid.Grid_Main;
import com.hour24.http.Http_Init;
import com.hour24.http.Http_UserInsert;
import com.hour24.innertemperature.Innertemperature_Main;
import com.hour24.light.Light_Main;
import com.hour24.oneclick.OneClick_Main;
import com.hour24.rpi.RPi_Main;
import com.hour24.security.Security_Main;
import com.hour24.weather.Weather_Main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	String[] menu = new String[] { "#디바이스", "전등", "커튼", "CCTV", "도어락", "보안", "#기타", "OneClick 기능", "날씨정보", "실내온도",
			"라즈베리파이" };
	int[] img = new int[] { 0, R.drawable.ic_light, R.drawable.ic_curtain, R.drawable.ic_cctv, R.drawable.ic_doorlock,
			R.drawable.ic_security, 0, R.drawable.ic_start, R.drawable.ic_weather, R.drawable.ic_temperature,
			R.drawable.ic_rpi };

	Toolbar toolbar;
	ActionBarDrawerToggle drawerToggle;
	DrawerLayout drawerLayout;
	ListView listDrawer;
	TextView txtTitle;
	ImageView imgGrid;

	int position = 0;

	String device = null;

	BackPressFinish backPressFinish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Element.activity = MainActivity.this;

		// 유저정보등록

		try {
			Element.REG_ID = GCMRegistrar.getRegistrationId(MainActivity.this);
			// String result = new Http_UserInsert(MainActivity.this,
			// Element.PHONE, Element.REG_ID).execute("").get();
			// Element.USER_ID = result;

			String result = new Http_Init(MainActivity.this).execute("").get();
			Log.e("devIoT", "인증키 유효정보 : " + result);

			if (result.equals("false") || result.contains("<html>") || result.equals("")) {
				Toast.makeText(MainActivity.this, "인증키 정보가 유요하지 않습니다.", Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(MainActivity.this,
				// ServerInfo.class);
				// startActivity(intent);
				this.finish();
			} else { // 인증키 유효
				ServerInfo.activity.finish();
				ServerInfo.activity = null;
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// toolbar setting
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		imgGrid = (ImageView) findViewById(R.id.imgGrid);

		imgGrid.setOnClickListener(this);

		// drawer
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				Log.i("devDraw", "onDrawerClosed");

			}

			@Override
			public void onDrawerOpened(View drawerView) {

				Log.i("devDraw", "onDrawerOpened");
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				// Log.i("devDraw", "onDrawerSlide : " + slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				// Log.i("devDraw", "onDrawerStateChanged new state : " +
				// newState);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.setDrawerIndicatorEnabled(true);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// 첫 페이지 설정
		Intent intent = getIntent();
		String flag2 = intent.getStringExtra("flag");

		if (flag2 == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.container, Grid_Main.newInstance(0)).commit();
		} else if (flag2.equals("cctv")) {
			getSupportFragmentManager().beginTransaction().replace(R.id.container, CCTV_Main.newInstance(5)).commit();
		} else {

		}

		// getSupportFragmentManager().beginTransaction().replace(R.id.container,
		// OneClick_Main.newInstance(0)).commit();

		// drawer 메뉴추가
		listDrawer = (ListView) findViewById(R.id.listDrawer);
		ArrayList<List_States> arrayList = new ArrayList<List_States>();
		for (int i = 0; i < menu.length; i++) {
			List_Add list_Add = new List_Add(MainActivity.this, arrayList, listDrawer, menu[i], img[i]);
			list_Add.onAdd();
		}

		// drawer click
		listDrawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				FragmentManager fragmentManager = getSupportFragmentManager();
				MainActivity.this.position = position;

				switch (position) {
				case 1: // 전등
					fragmentManager.beginTransaction().replace(R.id.container, Light_Main.newInstance(position))
							.commit();
					break;
				case 2: // 커튼
					fragmentManager.beginTransaction().replace(R.id.container, Curtain_Main.newInstance(position))
							.commit();
					break;
				case 3: // CCTV
					fragmentManager.beginTransaction().replace(R.id.container, CCTV_Main.newInstance(position))
							.commit();
					break;
				case 4: // 도어락
					fragmentManager.beginTransaction().replace(R.id.container, DoorLock_Main.newInstance(position))
							.commit();
					break;
				case 5: // 보안
					fragmentManager.beginTransaction().replace(R.id.container, Security_Main.newInstance(position))
							.commit();
					break;
				case 7: // OneClick 기능
					fragmentManager.beginTransaction().replace(R.id.container, OneClick_Main.newInstance(position))
							.commit();
					break;
				case 8: // 날씨정보
					fragmentManager.beginTransaction().replace(R.id.container, Weather_Main.newInstance(position))
							.commit();
					break;
				case 9: // 라즈베리파이
					fragmentManager.beginTransaction()
							.replace(R.id.container, Innertemperature_Main.newInstance(position)).commit();
					break;
				case 10: // 온도
					fragmentManager.beginTransaction().replace(R.id.container, RPi_Main.newInstance(position)).commit();
					break;
				default:
					break;
				}

				drawerLayout.closeDrawers();
			}
		});

		backPressFinish = new BackPressFinish(MainActivity.this);

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		backPressFinish.onBack();
	}

	public void onSectionAttached(int postion) {
		if (postion == 0) {
			txtTitle.setText("모든 디바이스");
		} else {
			txtTitle.setText(menu[postion]);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgGrid:
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, Grid_Main.newInstance(0)).commit();
			drawerLayout.closeDrawers();
			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
}