package anteeo.com.pl.zoltansport;

import holder.GroupItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		((TextView) findViewById(R.id.loggedAsTextView))
				.setText("Zalogowano jako: " + LoginActivity.username);

		findViewById(R.id.adoptionButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent adoption = new Intent(MenuActivity.this,
								SearchViewActivity.class);
						adoption.putExtra("ACTION",
								AdoptionReleaseStockActivity.ACTION_ADOPTION);
						MenuActivity.this.startActivity(adoption);
//						 finish();
					}
				});

		findViewById(R.id.releaseButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent orders = new Intent(MenuActivity.this,
								SearchViewActivity.class);
						MenuActivity.this.startActivity(orders);
//						 finish();
					}
				});

		findViewById(R.id.stockButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent stock = new Intent(MenuActivity.this,
								SearchViewActivity.class);
						stock.putExtra("ACTION",
								AdoptionReleaseStockActivity.ACTION_STOCK);
						MenuActivity.this.startActivity(stock);
//						 finish();
					}
				});

		findViewById(R.id.quitButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MenuActivity.this.finish();
						// TODO wyłączenie aplikacji
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_menu, menu);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Singleton.getInstance().setItemCheckedList(null);
		Singleton.getInstance().setBarCode(null);
		Singleton.getInstance().setBarCodeToBind(false);
	}
}
