package com.hour24.iot_home;

import android.app.Activity;

public class Element {

	public static Activity activity = null;

	public static String URL = "http://iot.yangukmo.com:12345";
	public static String URL_S = "http://rsa.yangukmo.com:12347";
	// http://rsa.yangukmo.com:12347/system/console : 보안콘솔

	public static String PROJECT_ID = "798883289428";

	public static String USER_ID = "";
	public static String IMEI = "";
	public static String PHONE = "";
	public static String REG_ID = "";
	public static String TOKEN = "";

	public static Activity LightActivity = null;
	public static Activity CurtainActivity = null;
	public static Activity SecurityActivity = null;
	public static Activity RPiActivity = null;
	public static Activity CCTVActivity = null;
	public static Activity OneClickActivity = null;
	public static Activity DoorlockActivity = null;
}
