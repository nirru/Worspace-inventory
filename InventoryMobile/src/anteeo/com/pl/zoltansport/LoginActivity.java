package anteeo.com.pl.zoltansport;

import holder.GroupItem;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import util.Util;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.connection.ConnectionDetector;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LoginActivity extends ActionBarActivity {

	private static final String url = "http://inventory.zoltansport.pl/api/";
	public static String username = "";
	AsyncHttpClient client;

	EditText loginEditText;
	EditText passEditText;
	private GroupItem groupitem;
	
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginEditText = (EditText) findViewById(R.id.loginEditText);
		passEditText = (EditText) findViewById(R.id.passEditText);

		 cd = new ConnectionDetector(getApplicationContext());
		progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("Loading....");
		groupitem = new GroupItem();
		// TODO POTEM USUNĄĆ!
		loginEditText.setText("mpiotrowski");
		passEditText.setText("3cy3stki");

		findViewById(R.id.backButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						LoginActivity.this.finish();
					}
				});

		findViewById(R.id.loginButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							 isInternetPresent = cd.isConnectingToInternet();
							 
				                // check for Internet status
				                if (isInternetPresent) {
				                    // Internet Connection is Present
				                    // make HTTP requests
				                	login();
				                } else {
				                    // Internet connection is not present
				                    // Ask user to connect to Internet
				                    Util.showOKAleart(LoginActivity.this, "Message", "There is no connection present, please connect to internet first");
				                }
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

					}
				});
	}

	@Override
	protected void onDestroy() {
		logout();
		super.onDestroy();
	}

	JSONObject loginData() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("login", loginEditText.getText().toString());
			obj.put("password", passEditText.getText().toString());
		} catch (JSONException e) {

		}
		return obj;
	}

	private void login() throws UnsupportedEncodingException {
		progressDialog.show();
		client = new AsyncHttpClient();
		StringEntity params;
		params = new StringEntity(loginData().toString());
		Header[] headers = {
				new BasicHeader("Content-Type", "application/json"),
				new BasicHeader("Accept", "application/json") };

		client.post(LoginActivity.this, url + "session", headers, params,
				"application/json", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						progressDialog.dismiss();
						try {
							JSONObject response = new JSONObject(new String(
									responseBody));
							if (!response.isNull("status")) {
								if (response.getString("status").equals(
										"success")) {
									username = response.getString("logged_as");
									Intent menu = new Intent(LoginActivity.this,
											MenuActivity.class);
									LoginActivity.this.startActivity(menu);

								} else if (response.getString("status").equals(
										"error")) {
									progressDialog.dismiss();
									// TODO tutaj przydałaby się wiadomość
									// od Mirka, {message: "Błąd..."}
									// Toast.makeText(LoginActivity.this,
									// response.getString("message"),
									// Toast.LENGHT_LONG).show();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
//						Toast.makeText(
//								LoginActivity.this,
//								"Błąd!\nKod:" + statusCode + "\n"
//										+ error.getMessage(), Toast.LENGTH_LONG)
//								.show();
						progressDialog.dismiss();
						Util.showOKAleart(LoginActivity.this, "Message", "Its seems there are some problem in the connectivity");
					}
				});
	}

	private static void logout() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.delete(url + "session", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
			}
		});
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
