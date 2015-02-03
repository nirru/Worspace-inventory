package com.fragemnt;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import anteeo.com.pl.zoltansport.InventoryListAdapter;
import anteeo.com.pl.zoltansport.LoginActivity;
import anteeo.com.pl.zoltansport.Product;
import anteeo.com.pl.zoltansport.R;
import anteeo.com.pl.zoltansport.UnknownBarcodeActivity;

public class AdoptionFragment extends Fragment {
	Context mContext;
	public static int ACTION_ADOPTION = 0;
	public static int ACTION_RELEASE = 1;
	public static int ACTION_STOCK = 2;

	int chosenAction;
	Boolean isNew;

	ArrayList<Product> products;
	InventoryListAdapter adapter;
	ListView inventoryListView;

	public AdoptionFragment(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(
				R.layout.activity_adoption_release_stock, container, false);
		init(rootView);
		return rootView;
	}

	private void init(View root) {
		((TextView) root.findViewById(R.id.loggedAsTextView))
				.setText("Zalogowano jako: " + LoginActivity.username);

		root.findViewById(R.id.backButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						AdoptionReleaseStockActivity.this.finish();
					}
				});

		root.findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO akcje w zależności od chosenAction i isNew

						Intent test = new Intent(
								mContext,
								UnknownBarcodeActivity.class);
						mContext.startActivity(test);
					}
				});

		inventoryListView = (ListView)root. findViewById(R.id.inventoryListView);
		reloadList();
	}
	
	 private void reloadList(){
	        products = new ArrayList<Product>();
	        adapter = new InventoryListAdapter(mContext, products);
	        inventoryListView.setAdapter(adapter);
	        //TODO akcje w zależności od chosenAction i isNew
	    }

}
