package anteeo.com.pl.zoltansport;

import custom.DialogResult;
import custom.Result;
import util.Util;
import holder.ChildItem;
import holder.GroupItem;
import adapter.BarCodeDialogAdapter;
import adapter.PrivacyAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BarCodeDialog implements Result {

	private static BarCodeDialog instance;
	private Context mContext;
	private Dialog customDialog;
	private ImageView dilog_cross_button;
	private ListView listView;
	private Button btn_another;
	private TextView desc, barCode_text;
	private GroupItem groupItem;
	BarCodeDialogAdapter adapter;
	private DialogResult dialogResult;
	private Button another_button;
	private String barcode_scanned_result;

	public static BarCodeDialog getInstance() {

		if (instance == null) {
			instance = new BarCodeDialog();
		}
		return instance;
	}

	public void createDialog(Context mContext, GroupItem groupItem,
			String barcode_scanned_result, DialogResult dialogResult) {
		this.mContext = mContext;
		this.groupItem = groupItem;
		customDialog = new Dialog(mContext);
		this.barcode_scanned_result = barcode_scanned_result;
		this.dialogResult = dialogResult;
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		customDialog.setContentView(R.layout.bar_code_popup);
		customDialog.setCanceledOnTouchOutside(false);
		Util.setDialogLayoutParams(mContext, customDialog);
		customDialog.show();
		init();
		setAdapter();
	}

	private void init() {
		// TODO Auto-generated method stub
		dilog_cross_button = (ImageView) customDialog
				.findViewById(R.id.cross_button);
		another_button = (Button) customDialog.findViewById(R.id.another_one);
		listView = (ListView) customDialog.findViewById(R.id.listView1);
		desc = (TextView) customDialog.findViewById(R.id.TextView01);
		barCode_text = (TextView) customDialog.findViewById(R.id.scanned_code);
		barCode_text.setText(barcode_scanned_result);
		dilog_cross_button.setOnClickListener(l);
		another_button.setOnClickListener(l);

	}

	private void setAdapter() {
		adapter = new BarCodeDialogAdapter(mContext, groupItem, customDialog,
				BarCodeDialog.this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onCheckBoxClick(Dialog customDialog) {
		// TODO Auto-generated method stub
		customDialog.cancel();
		showCheckedResultFromDialog();
	}

	public String showCheckedResultFromDialog() {
		String result = "";
		if (adapter.getBox().size() == 0) {
		} else {
			for (ChildItem p : adapter.getBox()) {
				if (p.box) {
					ChildItem childItem = new ChildItem(p.mSize_id, p.mName,
							p.mColor, p.mCode, p.mSize, 1);

					dialogResult.onClose(p);
				}
			}

		}

		// Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
		return result.trim();
	}

	View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.another_one:
				customDialog.cancel();
				break;

			case R.id.cross_button:
				customDialog.cancel();
				break;

			default:
				break;
			}
		}
	};

}
