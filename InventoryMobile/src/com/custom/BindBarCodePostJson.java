package com.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import util.Util;

import android.util.Base64;
import android.util.Log;
import anteeo.com.pl.zoltansport.AdoptionReleaseStockActivity;
import anteeo.com.pl.zoltansport.CustomersDbAdapter;
import appconstant.AppConstant;

public class BindBarCodePostJson {

	String size_id, barcode;
	String url;

	public BindBarCodePostJson(String url, String size_id, String barcode) {
		this.url = url;
		this.size_id = size_id;
		this.barcode = barcode;
		this.url = url;
		Log.e("--TAG--", "Size_id" + " " + size_id);
		
	}

	public String postDataToServer() {
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			// 3. build jsonObject
			JSONObject jsonParams = new JSONObject();
			jsonParams.put(AppConstant.SIZE_ID, size_id);
			jsonParams.put(AppConstant.BARCODE, barcode);

			JSONObject jsonParams1 = new JSONObject();
			jsonParams1.put(AppConstant.BARCODE, jsonParams);
			Log.e("JSON PARASM 1", "" + jsonParams1);
			// 4. convert JSONObject to JSON to String
			json = jsonParams1.toString();

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Authorization", getB64Auth());
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			Log.e("RESPONSE FROM SERVER", ""
					+ httpResponse.getStatusLine().getStatusCode());

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		Log.e("RESULT", "" + result);
		return result;
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = ""; 
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
  
		inputStream.close();
		return result;
	}

	private String getB64Auth() {
		String source = "mpiotrowski" + ":" + "3cy3stki";
		String ret = "Basic "
				+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE
						| Base64.NO_WRAP);
		return ret;
	}
}
