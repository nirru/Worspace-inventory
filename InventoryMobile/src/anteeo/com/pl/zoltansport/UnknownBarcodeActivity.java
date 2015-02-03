package anteeo.com.pl.zoltansport;


import holder.GroupItem;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UnknownBarcodeActivity extends ActionBarActivity {

	ArrayList<Product> products;
	ListView inventoryListView;
	GroupItem groupitem;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unknown_barcode);

		((TextView) findViewById(R.id.loggedAsTextView))
				.setText("Zalogowano jako: " + LoginActivity.username);

		findViewById(R.id.backButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UnknownBarcodeActivity.this.finish();
					}
				});

		// reloadList();

		// products.add(new Product("Testowe obuwie", 2));
	}

	private void init() {
		mContext = UnknownBarcodeActivity.this;
		inventoryListView = (ListView) findViewById(R.id.inventoryListView);
		groupitem = new GroupItem();
	}

//	private void reloadList() {
//		products = new ArrayList<Product>();
//		adapter = new UnknownBarcodeListAdapter(this, products);
//		inventoryListView.setAdapter(adapter);
//		// TODO akcje w zależności od chosenAction i isNew
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_unknown_barcode, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void getFriendsList(String message) {
		JSONArray jsonArray;

		try {
			jsonArray = new JSONArray(message.toString().trim());
			groupitem.title = "Friends" + "(" + jsonArray.length() + ")";
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//				ChildItem child = new ChildItem();
//				child.mProductName = jsonObject
//						.getString(AppConstant.PRODUCT_NAME);
//				child.mColorCode = jsonObject
//						.getString(AppConstant.PRODUCT_COLOR);
//				child.mSize = jsonObject.getString(AppConstant.PRODUCT_SIZE);
//				child.box = false;
//				groupitem.items.add(child);
//			}
			// Log.e("<<<<<<<<<<RECEIVED REQUEST API LIST ARRAY", "" +
			// items.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

//	private void setListviewToAdapter() {
//		if (groupitem != null) {
//			adapter = new ListGroupAdapter(mContext, groupitem, 2);
//			inventoryListView.setAdapter(adapter);
//		} else {
//			Toast.makeText(
//					mContext,
//					"No Friend to add to group, please first add friends first",
//					Toast.LENGTH_LONG).show();
//		}
//	}
}
