package anteeo.com.pl.zoltansport;


import holder.ChildItem;
import holder.GroupItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import util.Util;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import appconstant.AppConstant;

import com.example.zbartest.FormatSelectorDialogFragment;
import com.example.zbartest.MainActivity;
import com.example.zbartest.MessageDialogFragment;
import com.example.zbartest.ZXingScannerView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import custom.TinyDB;

public class AdoptionReleaseStockActivity extends ActionBarActivity implements
		MessageDialogFragment.MessageDialogListener,
		ZXingScannerView.ResultHandler,
		FormatSelectorDialogFragment.FormatSelectorDialogListener {

	public static int ACTION_ADOPTION = 0;
	public static int ACTION_RELEASE = 1;
	public static int ACTION_STOCK = 2;

	int chosenAction;
	Boolean isNew;

	ArrayList<Product> products;
	InventoryListAdapter adapter;
	ListView inventoryListView;
	private Context mContext;
	private ProgressDialog progressDialog;
	private static AsyncHttpClient client;
	private String scannedResult;
	GroupItem groupitem;
	TinyDB tinyDB;

	/** ----------------------Camera Scanner---------------- **/
	private static final String FLASH_STATE = "FLASH_STATE";
	private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
	private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
	private ZXingScannerView mScannerView;
	private boolean mFlash;
	private boolean mAutoFocus;
	private ArrayList<Integer> mSelectedIndices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
			mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true);
			mSelectedIndices = savedInstanceState
					.getIntegerArrayList(SELECTED_FORMATS);
		} else {
			mFlash = false;
			mAutoFocus = true;
			mSelectedIndices = null;
		}

		setupFormats();
		getSupportActionBar().hide();
		setContentView(R.layout.activity_adoption_release_stock);

		init();

		Bundle b = getIntent().getExtras();
		if (b != null) {
			chosenAction = (Integer) b.get("ACTION");
			if (chosenAction == ACTION_RELEASE) {
				isNew = (Boolean) b.get("NEW");
			}
		}

		((TextView) findViewById(R.id.loggedAsTextView))
				.setText("Zalogowano jako: " + LoginActivity.username);

		findViewById(R.id.backButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						AdoptionReleaseStockActivity.this.finish();
					}
				});

		findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO akcje w zależności od chosenAction i isNew
						//
						 Intent test = new Intent(
						 AdoptionReleaseStockActivity.this,
						 MainActivity.class);
						 AdoptionReleaseStockActivity.this.startActivity(test);

//						if (groupitem != null ) {
//							if (groupitem.items.size()>0) {
//								save();
//							}
//							else{
//								Toast.makeText(AdoptionReleaseStockActivity.this, "No Item To Update", Toast.LENGTH_LONG).show();
//							}
//						}
						
					}
				});

		// reloadList();
	}

	@Override
	public void onResume() {
		super.onResume();
		mScannerView.setResultHandler(this);
		mScannerView.startCamera();
		mScannerView.setFlash(mFlash);
		mScannerView.setAutoFocus(mAutoFocus);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(FLASH_STATE, mFlash);
		outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
		outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
	}

	@Override
	public void handleResult(Result rawResult) {
		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
		}
		scannedResult = rawResult.getText().toString().trim();
		showMessageDialog("Contents = " + rawResult.getText() + ", Format = "
				+ rawResult.getBarcodeFormat().toString());

	}

	public void showMessageDialog(String message) {
		makeRequest();
		DialogFragment fragment = MessageDialogFragment.newInstance(
				"Scan Results", message, this);
		fragment.show(getSupportFragmentManager(), "scan_results");

	}

	public void closeMessageDialog() {
		closeDialog("scan_results");
	}

	public void closeFormatsDialog() {
		closeDialog("format_selector");
	}

	public void closeDialog(String dialogName) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		DialogFragment fragment = (DialogFragment) fragmentManager
				.findFragmentByTag(dialogName);
		if (fragment != null) {
			fragment.dismiss();
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// Resume the camera
		mScannerView.startCamera();
		mScannerView.setFlash(mFlash);
		mScannerView.setAutoFocus(mAutoFocus);
	}

	@Override
	public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
		mSelectedIndices = selectedIndices;
		setupFormats();
	}

	public void setupFormats() {
		List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
		if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
			mSelectedIndices = new ArrayList<Integer>();
			for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
				mSelectedIndices.add(i);
			}
		}

		for (int index : mSelectedIndices) {
			formats.add(ZXingScannerView.ALL_FORMATS.get(index));
		}
		if (mScannerView != null) {
			mScannerView.setFormats(formats);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mScannerView.stopCamera();
		closeMessageDialog();
		closeFormatsDialog();
	}

	private void init() {
		tinyDB = new TinyDB(AdoptionReleaseStockActivity.this);
		groupitem = new GroupItem();
		inventoryListView = (ListView) findViewById(R.id.inventoryListView);
		this.mContext = AdoptionReleaseStockActivity.this;
		progressDialog = new ProgressDialog(mContext);
		client = new AsyncHttpClient();
		mScannerView = (ZXingScannerView) findViewById(R.id.zXingScannerView1);
		// makeRequest();
	}

	private void reloadList() {
		products = new ArrayList<Product>();
		adapter = new InventoryListAdapter(this, products);
		inventoryListView.setAdapter(adapter);
		// TODO akcje w zależności od chosenAction i isNew
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.menu_inventory, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	//
	// //noinspection SimplifiableIfStatement
	// if (id == R.id.action_settings) {
	// return true;
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }

	private void makeRequest() {
		progressDialog.setMessage("Loading...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		String url = "http://inventory.zoltansport.pl/api/barcodes/"
				+ scannedResult;
		client.setBasicAuth("mpiotrowski", "3cy3stki");
		client.get(url, responseHandler);
	}

	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {
			// TODO Auto-generated method stub
			Object json = null;
			try {
				progressDialog.dismiss();
				String response = String.valueOf(new String(bytes, "UTF-8"));
				Log.e("NAME AND SURNAME", "" + response);
				// showOKAleart(mContext, "TITKE", response);
				json = new JSONTokener(response).nextValue();
				if (json instanceof JSONObject) {
					Log.e("<<<<sfsfsfs>>>", "" + response);
					JSONObject jsonObject = (JSONObject) json;
					if (jsonObject.get("status").equals("success")) {
						String message = jsonObject.getString("products");
						Log.e("<<<<MESSAGEAEQA>>>", "" + message);
						util.Util.writeToPrefrefs(
								AdoptionReleaseStockActivity.this, message);
						getFriendsList(message);
						setListviewToAdapter();
					} else {
						Log.e("Scan Result", "" + scannedResult);
						Util.writeBarcodePrefrefs(AdoptionReleaseStockActivity.this, scannedResult);
						startActivity(new Intent(
								AdoptionReleaseStockActivity.this,
								MainActivity.class));
						finish();
//						showOKAleart(mContext, "Message", "ERROR");
					}
					groupitem.title = "Friends" + "(" + 0 + ")";

				} else if (json instanceof JSONArray) {
					Log.e("<<<<123123232>>>", "" + response);
					JSONArray jsonArray = (JSONArray) json;
					getFriendsList(response);
					setListviewToAdapter();
				}
			} catch (UnsupportedEncodingException e1) {

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable error) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			if (error.getMessage() != null) {
				showOKAleart(mContext, "ERROR TITLE", error.getMessage());
			} else {
				showOKAleart(mContext, "ERROR TITLE", "PROBLEM OCCURED");
			}
		}
	};

	public void showOKAleart(Context context, String title, String message) {
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
			}
		});
		alertDialog.show();
	}

	private void getFriendsList(String message) {
		JSONArray jsonArray;

		try {
			jsonArray = new JSONArray(message.toString().trim());
			if (jsonArray.length() > 1) {
				Util.writeBarcodePrefrefs(AdoptionReleaseStockActivity.this, scannedResult);
				startActivity(new Intent(
						AdoptionReleaseStockActivity.this,
						MainActivity.class));
				finish();
			}
			else{
				
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setListviewToAdapter() {
		if (groupitem != null) {
//			listAdapter = new ListGroupAdapter(mContext, groupitem, 1);
//			inventoryListView.setAdapter(listAdapter);
		} else {
			Toast.makeText(
					mContext,
					"No Friend to add to group, please first add friends first",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private void save() {
		progressDialog.setMessage("Loading");
		progressDialog.show();
		inventoryListView.getChildAt(1);
//		ChildItem childItem = new ChildItem();
		Log.e("CHILD ITEM LENGHRT", "" + groupitem.items.get(0).mSize_id);
		ChildItem childItem = groupitem.items.get(0);
		Log.e("CHILD DATA", "" + childItem.mSize_id);

		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put(AppConstant.SIZE_ID, groupitem.items.get(0).mSize_id);
			jsonParams.put(AppConstant.BARCODE, Util.readBarCodePrefs(AdoptionReleaseStockActivity.this));

			JSONObject jsonParams1 = new JSONObject();
			jsonParams1.put(AppConstant.BARCODE, jsonParams);
			Log.e("JSON PARASM 1", "" + jsonParams1);
			StringEntity entity = new StringEntity(jsonParams1.toString());
			client.setBasicAuth("mpiotrowski", "3cy3stki");
			Header[] headers = {new BasicHeader("Content-Type","application/json"), new BasicHeader("Accept","application/json")};
			client.post(AdoptionReleaseStockActivity.this, "http://inventory.zoltansport.pl/api/barcodes", headers, entity, "application/json", responseHandler2);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ChildItem getItem(int arg0) {
		return groupitem.items.get(arg0);
	}

	AsyncHttpResponseHandler responseHandler2 = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {
			// TODO Auto-generated method stub
			Object json = null;
			try {
//				progressDialog.dismiss();
				String response = String.valueOf(new String(bytes, "UTF-8"));
				Log.e("NAME AND SURNAME", "" + response);
				// showOKAleart(mContext, "TITKE", response);
				json = new JSONTokener(response).nextValue();
				if (json instanceof JSONObject) {
					Log.e("<<<<sfsfsfs>>>", "" + response);
					JSONObject jsonObject = (JSONObject) json;
					if (jsonObject.get("status").equals("success")) {
//						showOKAleart(mContext, "Message", "Saved Succesfull");
						updateStock();
					} else {
						showOKAleart(mContext, "Message", "Already Updated");
					}

				} else if (json instanceof JSONArray) {
					Log.e("<<<<123123232>>>", "" + response);
					JSONArray jsonArray = (JSONArray) json;

				}
			} catch (UnsupportedEncodingException e1) {

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
			progressDialog.dismiss();

		}
	};
	
	private void updateStock(){
		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put(AppConstant.SIZE_ID, groupitem.items.get(0).mSize_id);
			jsonParams.put(AppConstant.ACTION_TYPE, 0);
			jsonParams.put(AppConstant.STOCK, 12);
			Log.e("JSON PARASM 1", "" + jsonParams);
			StringEntity entity = new StringEntity(jsonParams.toString());
			client.setBasicAuth("mpiotrowski", "3cy3stki");
			Header[] headers = {new BasicHeader("Content-Type","application/json"), new BasicHeader("Accept","application/json")};
			client.post(AdoptionReleaseStockActivity.this, "http://inventory.zoltansport.pl/api/barcodes", headers, entity, "application/json", responseHandler3);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	AsyncHttpResponseHandler responseHandler3 = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {
			// TODO Auto-generated method stub
			Object json = null;
			try {
				progressDialog.dismiss();
				String response = String.valueOf(new String(bytes, "UTF-8"));
				Log.e("NAME AND SURNAME", "" + response);
				// showOKAleart(mContext, "TITKE", response);
				json = new JSONTokener(response).nextValue();
				if (json instanceof JSONObject) {
					Log.e("<<<<sfsfsfs>>>", "" + response);
					JSONObject jsonObject = (JSONObject) json;
					if (jsonObject.get("status").equals("success")) {
						showOKAleart(mContext, "Message", "Saved Succesfull");
					} else {
						showOKAleart(mContext, "Message", "Already Updated");
					}

				} else if (json instanceof JSONArray) {
					Log.e("<<<<123123232>>>", "" + response);
					JSONArray jsonArray = (JSONArray) json;

				}
			} catch (UnsupportedEncodingException e1) {

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
			progressDialog.dismiss();

		}
	};
}
