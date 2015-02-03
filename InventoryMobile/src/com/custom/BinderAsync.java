package com.custom;

import holder.ChildItem;
import holder.GroupItem;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
//import android.util.Log;
import android.util.Log;
import anteeo.com.pl.zoltansport.LoginActivity;
import anteeo.com.pl.zoltansport.MenuActivity;
import appconstant.AppConstant;

public class BinderAsync extends AsyncTask<String, String, String> {

	ProgressDialog mProgressDialog;
	Context context;
	String url;
	private GroupItem groupItem;
	private String barcode;

	public BinderAsync(Context context, String url, GroupItem groupItem,
			String barcode) {
		this.context = context;
		this.mProgressDialog = new ProgressDialog(context);
		this.url = url;
		this.barcode = barcode;
		this.groupItem = groupItem;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		showDialog();
	}

	@Override
	protected String doInBackground(String... aurl) {
		// Log.e("MAY AYA", "" + "do in background in sigin");
		callLogin();
		return null;
	}

	@Override
	protected void onPostExecute(String unused) {
		closeDialog();
		onSuccesFull();
	}

	void showDialog() {
		mProgressDialog.setMessage("Loading....");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	void closeDialog() {
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void callLogin() {
		for (int i = 0; i < groupItem.items.size(); i++) {
			ChildItem childItem = getItem(i);
//			Log.e("SIZE_ID", "" + childItem.getId());
//			Log.e("STOCK AMOUNT", "" + childItem.getCount());
			if (childItem.isBarCodeToBind == false) {
//				Log.e("False", "" + childItem.isBarCodeToBind);
				PostJson postJson = new PostJson(url, childItem.getId(), "2", ""
						+ childItem.getCount());
				postJson.postDataToServer();
				
			} else {
//				Log.e("TRUE", "" + childItem.isBarCodeToBind);
				BindBarCodePostJson bindBarCodePostJson = new BindBarCodePostJson(
						AppConstant.BIND_BARCODE, childItem.getId(), childItem.barcode);
				bindBarCodePostJson.postDataToServer();
				PostJson postJson = new PostJson(url, childItem.getId(), "2", ""
						+ childItem.getCount());
				postJson.postDataToServer();
			}
			
			
		}
	}

	public ChildItem getItem(int position) {
		return groupItem.items.get(position);
	}

	private void onSuccesFull() {
		closeDialog();
		showOKAleart(context, "Message", "Stock updated successfully");
	}

	@SuppressWarnings("deprecation")
	public void showOKAleart(final Context context, String title, String message) {
		final AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle("Message");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
				Intent i = new Intent(context,MenuActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
//				((Activity)context).finish();
			}
		});
		alertDialog.show();
	}

}
