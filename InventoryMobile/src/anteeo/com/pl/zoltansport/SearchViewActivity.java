package anteeo.com.pl.zoltansport;

import holder.ChildItem;
import holder.GroupItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import mydatabase.MyDatabase;
import util.Util;
import adapter.BarCodeDialogAdapter;
import adapter.MainListAdapter;
import adapter.PrivacyAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import appconstant.AppConstant;
import asyn.RequestFailure;
import asyn.RequestHandler;
import asyn.RequestListener;

import com.custom.BinderAsync;
import com.custom.LogInAsync;
import com.example.zbartest.ScannerActivity;

import custom.DialogResult;
import custom.Result;
import custom.ShrinkList;

public class SearchViewActivity extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener,
		DialogResult, ShrinkList {

	private ListView mListView, mMainListView;

	private SearchView searchView, secondary_search_view;
	private CustomersDbAdapter mDbHelper;

	private TextView inspectionDate;

	private GroupItem groupItem;
	private GroupItem groupItemMainList;
	private GroupItem itemCheckList;
	private GroupItem barCodeDialogList, barCodeDialogUniqueList;
	private Cursor products;
	private MyDatabase db;

	private ImageView barCode, wifi;
	private Button back_buButton, save_buButton;

	private String message;
	private PrivacyAdapter adapter;
	private MainListAdapter mainAdapter;

	private EditText bar_code;

	private RelativeLayout rel_left, rel_reight, search_relative;
	// private boolean isFinalSaveClick = false;
	List<ChildItem> usedChild;

	public static final String BARCODE = "http://inventory.zoltansport.pl/api/barcodes/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testmain);

		setActonBAr(1);
		copyDatabase();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		init();
		findFocus();
		searchClick();

	}

	private void init() {
		// TODO Auto-generated method stub
		// Bundle extras = getIntent().getExtras();
		search_relative = (RelativeLayout) findViewById(R.id.searc_relative);
		bar_code = (EditText) findViewById(R.id.bar_code);
		rel_left = (RelativeLayout) findViewById(R.id.leftLayout);
		rel_reight = (RelativeLayout) findViewById(R.id.rightLayout);
		back_buButton = (Button) findViewById(R.id.backButton);
		save_buButton = (Button) findViewById(R.id.saveButton);
		barCode = (ImageView) findViewById(R.id.barcode1);
		wifi = (ImageView) findViewById(R.id.wifi);
		searchView = (SearchView) findViewById(R.id.search);
		secondary_search_view = (SearchView) findViewById(R.id.search_second);
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);

		usedChild = new ArrayList<ChildItem>();
		groupItemMainList = new GroupItem();
		mMainListView = (ListView) findViewById(R.id.main_list);
		mListView = (ListView) findViewById(R.id.list);

		barCode.setOnClickListener(l);
		wifi.setOnClickListener(l);
		back_buButton.setOnClickListener(l);
		save_buButton.setOnClickListener(l);

		rel_left.setVisibility(View.VISIBLE);
		rel_reight.setVisibility(View.GONE);

		searchView.setQuery("", false);

		bar_code.requestFocus();
		bar_code.setFocusable(true);

		rel_reight.setVisibility(View.GONE);
		rel_left.setVisibility(View.VISIBLE);
		message = Singleton.getInstance().getBarCode();
		if ((message != null) && (message.length() > 0)) {
			bar_code.setText(message);
			groupItemMainList = Singleton.getInstance().getItemCheckedList();
			Log.e("MAIN GROUP LIST COUNT AHETR DCAN", ""
					+ groupItemMainList.items.size());
			Singleton.getInstance().setBarCode(message);
			getDataForScannedbarCode();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		products.close();
		db.close();
	}

	public boolean onQueryTextChange(String newText) {
		showResults(newText + "*");
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		showResults(query + "*");
		return false;
	}

	public boolean onClose() {
		showResults("");
		return false;
	}

	private void showResults(String query) {
		rel_reight.setVisibility(View.GONE);
		rel_left.setVisibility(View.VISIBLE);
		Cursor cursor = db.searchCustomer((query != null ? query.toString()
				: "@@@@"));

		if (cursor == null) {
			//
		} else {
			Log.e("CustomersDbAdapter.KEY_NAME", "" + query);
			setListViewToAdapter(cursor);
		}
	}

	private void setListViewToAdapter(Cursor cursor) {
		// Log.e("CURSOR COUNT", " " + cursor.getCount());
		groupItem = new GroupItem();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			do {
				ChildItem childItem = new ChildItem();
				String size_id = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_SIZE_ID));
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_NAME));
				String product_id = cursor
						.getString(cursor
								.getColumnIndexOrThrow(CustomersDbAdapter.KEY_PRODUCT_ID));
				String size = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_SIZE));
				String color = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_COLOR));
				String code = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_CODE));
				String stock = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_STOCK));
				String symbol = cursor.getString(cursor
						.getColumnIndexOrThrow(CustomersDbAdapter.KEY_SYMBOL));

				childItem.mSize_id = size_id;
				childItem.mName = name;
				childItem.product_id = product_id;
				childItem.mSize = size;
				childItem.mColor = color;
				childItem.mCode = code;
				childItem.mStock = stock;
				childItem.mSymbol = symbol;
				childItem.box = false;
				// childItem.count = "1";
				groupItem.items.add(childItem);
			}
			// move the cursor's pointer up one position.
			while (cursor.moveToNext());
		}
		// Log.e("Group Item Size", "" + groupItem.items.size());
		adapter = new PrivacyAdapter(SearchViewActivity.this, groupItem,
				mListView, SearchViewActivity.this);
		mListView.setAdapter(adapter);
	}

	private void setActonBAr(int k) {
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.header);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		// getActionBar().setIcon(R.drawable.side_drawer);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
	}

	private void copyDatabase() {
		db = new MyDatabase(this);
		products = db.getProducts();
		db.close();
		// Log.e("PRODUCT COUNT SIZE", "" + products.getCount());
	}

	View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.wifi:
				showAlert();
				break;

			case R.id.barcode1:
				startBarCodeScanner();
				break;

			case R.id.backButton:
				finish();
				break;

			case R.id.saveButton:
				stockUpdation();
				break;

			default:
				break;
			}
		}
	};

	protected void showAlert() {
		// TODO Auto-generated method stub
		Util.showOKAleart(SearchViewActivity.this, "Message", "Coming Soon");
	}

	protected void saveData() {
		// TODO Auto-generated method stub
		showCheckedResult();
		removeDuplicacySec();
		rel_reight.setVisibility(View.VISIBLE);
		rel_left.setVisibility(View.GONE);
		mainAdapter = new MainListAdapter(SearchViewActivity.this,
				itemCheckList, mMainListView);
		mMainListView.setAdapter(mainAdapter);
	}

	private void stockUpdation() {
		updateStock();
	}

	protected void startBarCodeScanner() {
		// TODO Auto-generated method stub
		itemCheckList = Singleton.getInstance().getItemCheckedList();
		if (itemCheckList != null) {
			Log.e("this is true", "" + itemCheckList.items.size());
			Singleton.getInstance().setItemCheckedList(itemCheckList);
		} else {
			itemCheckList = new GroupItem();
			Log.e("this is false", "" + itemCheckList.items.size());
			Singleton.getInstance().setItemCheckedList(itemCheckList);
		}

		startActivity(new Intent(SearchViewActivity.this, ScannerActivity.class));
	}

	public String showCheckedResult() {
		String result = "";
		if (adapter.getBox().size() == 0) {
			groupItemMainList = itemCheckList;
		} else {
			for (ChildItem p : adapter.getBox()) {
				if (p.box) {
					ChildItem childItem = new ChildItem(p.mSize_id, p.mName,
							p.mColor, p.mCode, p.mSize, 1);
					childItem.setId(p.mSize_id);
					childItem.setName(p.mName);
					childItem.setCode((p.mCode));
					childItem.setColor((p.mColor));
					childItem.setSize(p.mSize);
					childItem.setCount(1);
					childItem.barcode = Singleton.getInstance().getBarCode();
					childItem.isBarCodeToBind = Singleton.getInstance()
							.isBarCodeToBind();
					groupItemMainList.items.add(childItem);
				}
			}
			Singleton.getInstance().setBarCodeToBind(false);
			Singleton.getInstance().setBarCode("");
			itemCheckList = new GroupItem();
			itemCheckList.items = groupItemMainList.items;
			Singleton.getInstance().setItemCheckedList(itemCheckList);
		}
		// Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
		return result.trim();
	}

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
				finish();
			}
		});
		alertDialog.show();
	}

	private void removeDuplicacySec() {
		if (groupItemMainList != null) {
			itemCheckList = new GroupItem();
			for (int i = 0; i < groupItemMainList.items.size(); i++) {
				ChildItem childItem = getItem(i);
				compareAndRemove(childItem);
				itemCheckList.items.add(childItem);
			}
			groupItemMainList = itemCheckList;
		} else {
			Toast.makeText(SearchViewActivity.this, "Nothing to update",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void compareAndRemove(ChildItem childItem) {
		int match = 0;
		for (int i = 0; i < groupItemMainList.items.size(); i++) {
			ChildItem childItem2 = getItem(i);
			if (childItem.getId().trim().equals(childItem2.getId())) {
				match = match + 1;
				if (match == 2) {
					groupItemMainList.items.remove(i);
					childItem.setId(childItem.mSize_id);
					childItem.setName(childItem.mName);
					childItem.setCode((childItem.mCode));
					childItem.setColor((childItem.mColor));
					childItem.setSize(childItem.mSize);
					childItem.setCount(childItem.count + 1);
					childItem.focus = false;
					break;
				} else {
				}
			} else {
				childItem.setId(childItem.mSize_id);
				childItem.setName(childItem.mName);
				childItem.setCode((childItem.mCode));
				childItem.setColor((childItem.mColor));
				childItem.setSize(childItem.mSize);
				childItem.setCount(childItem.count);
				childItem.focus = false;
			}
		}

	}

	public ChildItem getItem(int position) {
		return groupItemMainList.items.get(position);
	}

	public ChildItem getItemFromDialog(int position) {
		return barCodeDialogList.items.get(position);
	}

	private void getDataForScannedbarCode() {
		String url = BARCODE + message.trim();
		RequestHandler requestHandler = RequestHandler.getInstance();
		requestHandler.make_get_Request(SearchViewActivity.this, url,
				new RequestListener() {

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						Object json = null;
						try {
							json = new JSONTokener(result).nextValue();
							if (json instanceof JSONObject) {
								JSONObject jsonObject = (JSONObject) json;
								if (jsonObject.getString("status").equals(
										"success")) {
									Singleton.getInstance().setBarCodeToBind(
											false);
									getProductDetails(jsonObject
											.getString("products"));
									presentSearchView();
									showDataToDailog();
								} else {
									Singleton.getInstance().setBarCodeToBind(
											true);
									showFailureAleart(
											"Message",
											"There is no product related to the code, please select one",
											SearchViewActivity.this);
									// presentSearchView();
								}
							} else if (json instanceof JSONArray) {
								JSONArray jsonArray = (JSONArray) json;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new RequestFailure() {

					@Override
					public void onFailure(String result) {
						// TODO Auto-generated method stub
						Log.e("This Is failure Called", "YES");
						showFailureAleart("Message", result,
								SearchViewActivity.this);
					}
				});
	}

	private void getProductDetails(String message) {
		barCodeDialogList = new GroupItem();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(message.toString().trim());

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ChildItem childItem = new ChildItem(
						jsonObject.getString(CustomersDbAdapter.KEY_SIZE_ID),
						jsonObject.getString(CustomersDbAdapter.KEY_NAME),
						jsonObject.getString(CustomersDbAdapter.KEY_COLOR),
						jsonObject.getString(CustomersDbAdapter.KEY_CODE),
						jsonObject.getString(CustomersDbAdapter.KEY_SIZE), 1);
				childItem.setId(childItem.mSize_id);
				childItem.setName(childItem.mName);
				childItem.setCode((childItem.mCode));
				childItem.setColor((childItem.mColor));
				childItem.setSize(childItem.mSize);
				childItem.setCount(1);
				barCodeDialogList.items.add(childItem);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void showDataToDailog() {
		removeDuplicacyFromDialog();
		BarCodeDialog barCodeDialog = BarCodeDialog.getInstance();
		barCodeDialog.createDialog(SearchViewActivity.this, barCodeDialogList,
				message, SearchViewActivity.this);
	}

	@Override
	public void onClose(ChildItem p) {
		// TODO Auto-generated method stub
		ChildItem childItem = new ChildItem(p.mSize_id, p.mName, p.mColor,
				p.mCode, p.mSize, 1);
		childItem.setId(p.mSize_id);
		childItem.setName(p.mName);
		childItem.setCode((p.mCode));
		childItem.setColor((p.mColor));
		childItem.setSize(p.mSize);
		childItem.setCount(1);
		childItem.barcode = Singleton.getInstance().getBarCode();
		groupItemMainList.items.add(childItem);

		Singleton.getInstance().setBarCode("");
		itemCheckList = new GroupItem();
		itemCheckList.items = groupItemMainList.items;

		removeDuplicacySec();
		searchView.clearFocus();
		bar_code.requestFocus();
		bar_code.setText("");
		rel_reight.setVisibility(View.VISIBLE);
		rel_left.setVisibility(View.GONE);
		mainAdapter = new MainListAdapter(SearchViewActivity.this,
				itemCheckList, mMainListView);
		mMainListView.setAdapter(mainAdapter);

	}

	private void updateStock() {
		if (itemCheckList != null) {
			if (itemCheckList.items.size() > 0) {
				if (rel_reight.getVisibility() == View.VISIBLE) {
					bindBarcode();
				} else {
					showMainContainerAlert(
							"Message",
							"You are not under main list , You have to be under main list to perform updation. Do you want to go to main list",
							SearchViewActivity.this);
				}

			} else {
				Util.showOKAleart(SearchViewActivity.this, "Message",
						"No Item to update");
			}

		} else {
			Util.showOKAleart(SearchViewActivity.this, "Message",
					"No Item to update");
		}
	}

	private void bindBarcode() {
		if (message != null) {
			new BinderAsync(SearchViewActivity.this, AppConstant.BIND_BARCODE,
					itemCheckList, message).execute("");
		} else {
			new LogInAsync(SearchViewActivity.this, AppConstant.UPDATE_STOCK,
					itemCheckList).execute("");
		}
	}

	@Override
	public void shrink() {
		// TODO Auto-generated method stub
		showmainList();
	}

	private void showmainList() {
		bar_code.setText("");
		searchView.clearFocus();
		searchView.setVisibility(View.GONE);
		secondary_search_view.setVisibility(View.VISIBLE);
		saveData();
		bar_code.requestFocus();
		bar_code.setFocusable(true);
	}

	private void findFocus() {
		bar_code.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_NEXT
						|| actionId == EditorInfo.IME_ACTION_DONE) {
					Util.hideKeyboard(SearchViewActivity.this, bar_code);
					fetchDataOnNextlickIfEditFieldNotEmpty();
					return true;
				}
				return false;
			}
		});
	}

	private void searchClick() {
		searchView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("SERCHVIEW FIRST CLICKED", "" + 1);
				secondary_search_view.setVisibility(View.GONE);
				searchView.setVisibility(View.VISIBLE);
				searchView.requestFocus();
				searchView.setFocusable(true);
				searchView.setFocusableInTouchMode(true);
				searchView.setQuery("", false);
			}
		});

		secondary_search_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("SERCHVIEW SECOND CLICKED", "" + 2);
				secondary_search_view.setVisibility(View.GONE);
				searchView.requestFocus();
				searchView.setVisibility(View.VISIBLE);
				searchView.setQuery("", false);

			}
		});

		bar_code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("SERCHVIEW THIRD CLICKED", "" + 3);
				bar_code.requestFocus();
				bar_code.setFocusable(true);
				bar_code.setFocusableInTouchMode(true);
				// secondary_search_view.setVisibility(View.GONE);
				// searchView.setVisibility(View.VISIBLE);
				// searchView.setQuery("", false);
				showResults("");
			}
		});
	}

	private void fetchDataOnNextlickIfEditFieldNotEmpty() {
		secondary_search_view.setVisibility(View.GONE);
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		searchView.setFocusable(true);
		searchView.setFocusableInTouchMode(true);
		if (bar_code.getText().toString().equals("")) {
			Log.e("FIELD IS EMPTY", "" + "YES");
			presentSearchView();
		} else {
			Log.e("----FIELD IS EMPTY---", "" + "NO");
			Singleton.getInstance().setBarCode(
					bar_code.getText().toString().trim());
			Singleton.getInstance().setItemCheckedList(itemCheckList);
			message = bar_code.getText().toString().trim();
			getDataForScannedbarCode();
		}
	}

	private void presentSearchView() {
		bar_code.requestFocus();
		secondary_search_view.setVisibility(View.GONE);
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		searchView.setQuery("", false);
	}

	public void showFailureAleart(String title, String message, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Message")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						presentSearchView();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								bar_code.setText("");
								bar_code.requestFocus();
								bar_code.setFocusable(true);
								itemCheckList = Singleton.getInstance()
										.getItemCheckedList();
								if (itemCheckList != null) {
									rel_reight.setVisibility(View.VISIBLE);
									rel_left.setVisibility(View.GONE);
									mainAdapter = new MainListAdapter(
											SearchViewActivity.this,
											itemCheckList, mMainListView);
									mMainListView.setAdapter(mainAdapter);
								} else {
									Toast.makeText(
											SearchViewActivity.this,
											"There is no saved item under the main list",
											Toast.LENGTH_SHORT).show();
									rel_reight.setVisibility(View.VISIBLE);
									rel_left.setVisibility(View.GONE);
									itemCheckList = new GroupItem();
									mainAdapter = new MainListAdapter(
											SearchViewActivity.this,
											itemCheckList, mMainListView);
									mMainListView.setAdapter(mainAdapter);
								}

							}
						});
		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void showMainContainerAlert(String title, String message,
			Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Message")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								rel_reight.setVisibility(View.VISIBLE);
								rel_left.setVisibility(View.GONE);
								mainAdapter = new MainListAdapter(
										SearchViewActivity.this, itemCheckList,
										mMainListView);
								mMainListView.setAdapter(mainAdapter);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						bar_code.setText("");
						bar_code.requestFocus();
						bar_code.setFocusable(true);

					}
				});
		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	private void compareAndRemoveFromDialog(ChildItem childItem) {
		int matchCount = 0;
		barCodeDialogUniqueList = new GroupItem();
		for (int i = 0; i < barCodeDialogList.items.size(); i++) {
			ChildItem childItem2 = getItemFromDialog(i);
			if (childItem.getId().trim().equals(childItem2.getId())) {
				matchCount = matchCount + 1;
				if (matchCount >= 2) {
					barCodeDialogList.items.remove(i);
				}
				else {
					barCodeDialogUniqueList.items.add(childItem);
				}
                   
			} else {
				barCodeDialogUniqueList.items.add(childItem);
			}
		}
		
		

	}

	private void removeDuplicacyFromDialog() {
		if (barCodeDialogList != null) {
			for (int i = 0; i < barCodeDialogList.items.size(); i++) {
				ChildItem childItem = getItemFromDialog(i);
				compareAndRemoveFromDialog(childItem);
			}
			
			barCodeDialogList = barCodeDialogUniqueList;

		} else {
			Toast.makeText(SearchViewActivity.this, "Nothing to update",
					Toast.LENGTH_SHORT).show();
		}

	}

}
