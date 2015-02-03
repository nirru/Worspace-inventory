package com.custom;

import com.example.zbartest.ZXingScannerView;

public interface ResponseHandler {
	
	public void onSuccess(String result, ZXingScannerView mScannerView, boolean isScanOn);

}
