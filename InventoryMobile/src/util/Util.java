package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by C-ShellWin on 12/12/2014.
 */
public class Util {

	public static void writeToPrefrefs(Context mContext, String result) {
		SharedPreferences.Editor prefs = mContext.getSharedPreferences(
				appconstant.AppConstant.PARCEABLE, Context.MODE_PRIVATE).edit();
		prefs.putString(appconstant.AppConstant.PREFS_USER_DETAILS, result);
		prefs.commit();
	}

	public static void writeBarcodePrefrefs(Context mContext, String result) {
		SharedPreferences.Editor prefs = mContext.getSharedPreferences(
				appconstant.AppConstant.PARCEABLE, Context.MODE_PRIVATE).edit();
		prefs.putString(appconstant.AppConstant.BARCODE, result);
		prefs.commit();
	}

	public static String readFromPrefs(Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				appconstant.AppConstant.PARCEABLE, mContext.MODE_PRIVATE);
		return sharedPreferences.getString(
				appconstant.AppConstant.PREFS_USER_DETAILS, "");
	}

	public static String readBarCodePrefs(Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				appconstant.AppConstant.PARCEABLE, mContext.MODE_PRIVATE);
		return sharedPreferences.getString(appconstant.AppConstant.BARCODE, "");
	}

	public static void CleanPrefs(Context mContext) {
		SharedPreferences.Editor prefs = mContext.getSharedPreferences(
				appconstant.AppConstant.PARCEABLE, Context.MODE_PRIVATE).edit();
		prefs.clear();
		prefs.commit();
	}

	public static void showOKAleart(Context context, String title,
			String message) {
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
	
	public static void setDialogLayoutParams(Context activity, Dialog dialog)
    {  
        dialog.getWindow().setLayout((int)(((Activity)activity).getWindow().peekDecorView().getWidth()*0.98),(int) (((Activity)activity).getWindow().peekDecorView().getHeight()*0.7));
    }
	
	public static void hideKeyboard(Context mContext , EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
