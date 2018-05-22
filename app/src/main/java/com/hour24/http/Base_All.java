// package com.hour24.http;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.apache.http.NameValuePair;
// import org.apache.http.client.entity.UrlEncodedFormEntity;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.impl.client.DefaultHttpClient;
// import org.apache.http.message.BasicNameValuePair;
// import org.apache.http.params.HttpConnectionParams;
// import org.apache.http.params.HttpParams;
// import org.apache.http.protocol.HTTP;
//
// import com.hour24.curtain.Curtain_ViewControl;
// import com.hour24.iot_home.Element;
// import com.hour24.light.Light_ViewControl;
//
// import android.app.Activity;
// import android.app.ProgressDialog;
// import android.os.AsyncTask;
// import android.util.Log;
//
// @SuppressWarnings("deprecation")
// public class Base_All extends AsyncTask<Integer, String, Integer> {
//
// Activity activity;
// boolean turn;
//
// ProgressDialog dialog;
//
// public Base_All(Activity activity, boolean turn) {
// this.activity = activity;
// this.turn = turn;
// }
//
// @Override
// protected void onPreExecute() {
// super.onPreExecute();
//
// dialog = new ProgressDialog(activity);
// dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
// dialog.setCancelable(false);
// dialog.setCanceledOnTouchOutside(false);
// dialog.show();
// }
//
// @SuppressWarnings("deprecation")
// @Override
// protected Integer doInBackground(Integer... params) {
//
// int value = params[0];
// publishProgress("max", value + "");
// Log.e("devIoT", value + "");
//
// DefaultHttpClient client = new DefaultHttpClient();
//
// try {
//
// // value == device 의 갯수
// for (int i = 1; i <= value; i++) {
//
// if (i == 1) {
// HttpPost post = new HttpPost(Element.URL + "/LightHttp");
//
// List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//
// pairs.add(new BasicNameValuePair("type", type)); // on/off/auto
// // 하는
// // 기능을
// // 실행시키겟다
// pairs.add(new BasicNameValuePair("pinname", pinname + ""));
// pairs.add(new BasicNameValuePair("turn", turn + ""));
// pairs.add(new BasicNameValuePair("token", Element.TOKEN));
//
// UrlEncodedFormEntity encoded = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
// post.setEntity(encoded);
// client.execute(post);
//
// HttpParams httpparams = client.getParams();
// HttpConnectionParams.setConnectionTimeout(httpparams, 5000);
// HttpConnectionParams.setSoTimeout(httpparams, 5000);
//
// // 작업 진행 마다 진행률을 갱신하기 위해 진행된 개수와 설명을 publishProgress() 로
// // 넘겨줌.
// publishProgress(I);
// }
// }
//
// return value;
//
// } catch (Exception e) {
// e.printStackTrace();
// client.getConnectionManager().shutdown();
// }
//
// return value;
// }
//
// // onProgressUpdate() 함수는 publishProgress() 함수로 넘겨준 데이터들을 받아옴
// @Override
// protected void onProgressUpdate(String... progress) {
// if (progress[0].equals("ing")) {
// dialog.setProgress(Integer.parseInt(progress[1]));
// // dialog.setMessage(progress[2] + "진행중");
// } else if (progress[0].equals("max")) {
// dialog.setMax(Integer.parseInt(progress[1]));
// }
// }
//
// // onPostExecute() 함수는 doInBackground() 함수가 종료되면 실행됨
// @Override
// protected void onPostExecute(Integer result) {
// // dialog_Progress.onProgress_dismiss();
// dialog.dismiss();
// new Light_ViewControl().onView();
// }
// }