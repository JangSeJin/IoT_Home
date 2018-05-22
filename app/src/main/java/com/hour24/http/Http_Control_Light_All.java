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
// public class Http_Control_Light_All extends AsyncTask<Integer, String,
// Integer> {
//
// Activity activity;
// boolean turn;
//
// ProgressDialog dialog;
//
// public Http_Control_Light_All(Activity activity, boolean turn) {
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
// publishProgress(i + "");
// Log.e("devIoT", value + "");
//
// DefaultHttpClient client = new DefaultHttpClient();
//
// try {
//
// // value == device �� ����
// for (int i = 1; i <= value; i++) {
//
// if (i == 1) {
// HttpPost post = new HttpPost(Element.URL + "/LightHttp");
//
// List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//
// pairs.add(new BasicNameValuePair("type", "turn")); // on/off/auto
// // �ϴ�
// // �����
// // �����Ű�ٴ�
// pairs.add(new BasicNameValuePair("pinname", "all"));
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
// // �۾� ���� ���� ������� �����ϱ� ���� ����� ������ ������ publishProgress() ��
// // �Ѱ���.
// publishProgress(i + "");
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
// // onProgressUpdate() �Լ��� publishProgress() �Լ��� �Ѱ��� �����͵��� �޾ƿ�
// @Override
// protected void onProgressUpdate(String... progress) {
// // if (progress[0].equals("ing")) {
// // dialog.setProgress(Integer.parseInt(progress[1]));
// // // dialog.setMessage(progress[2] + "������");
// // } else if (progress[0].equals("max")) {
// // dialog.setMax(Integer.parseInt(progress[1]));
// // }
// }
//
// // onPostExecute() �Լ��� doInBackground() �Լ��� ����Ǹ� �����
// @Override
// protected void onPostExecute(Integer result) {
// // dialog_Progress.onProgress_dismiss();
// dialog.dismiss();
// new Light_ViewControl().onView();
// }
// }