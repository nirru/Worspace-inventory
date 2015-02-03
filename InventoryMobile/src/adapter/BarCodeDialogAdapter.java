package adapter;

import java.util.ArrayList;

import custom.Result;

import holder.ChildHolder;
import holder.ChildItem;
import holder.GroupItem;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import animation.ListYouAnimation;
import anteeo.com.pl.zoltansport.R;

/**
 * Created by C-ShellWin on 12/28/2014.
 */
public class BarCodeDialogAdapter extends BaseAdapter {

	private Context mContext;
	private GroupItem groupitem;
	private LayoutInflater inflater;
	private Dialog customDialog;
	private Result result;

	public BarCodeDialogAdapter(Context mContext, GroupItem groupitem,
			Dialog customDialog, Result result) {
		this.mContext = mContext;
		this.groupitem = groupitem;
		this.customDialog = customDialog;
		this.result = result;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (groupitem != null)
			return groupitem.items.size();
		else
			return 0;
	}

	@Override
	public ChildItem getItem(int position) {
		return groupitem.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		ChildItem item = getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.suggestor_row, parent,
					false);
			holder = new ChildHolder();
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		holder.selectionBox = (CheckBox) convertView
				.findViewById(R.id.selection_imageview);
		holder.selectionBox.setOnCheckedChangeListener(myCheckChangList);
		holder.selectionBox.setTag(position);
		holder.selectionBox.setChecked(item.box);
		holder.mName = (TextView) convertView.findViewById(R.id.sname);
		holder.mColor = (TextView) convertView.findViewById(R.id.scolor);
		holder.mSize = (TextView) convertView.findViewById(R.id.ssize);

		holder.mName.setText(item.mName);
		holder.mColor.setText(item.mColor);
		if (item.mSize == null || item.mSize.equals("") || item.mSize == "null") {
			holder.mSize.setText("( " + "No size avilable" + " )");
		} else {
			holder.mSize.setText("( " + item.mSize + " )");
		}
		return convertView;
	}

	public ArrayList<ChildItem> getBox() {
		ArrayList<ChildItem> box = new ArrayList<ChildItem>();
		for (ChildItem p : groupitem.items) {
			if (p.box)
				box.add(p);
		}
		return box;
	}

	CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getItem((Integer) buttonView.getTag()).box = isChecked;
			result.onCheckBoxClick(customDialog);
		}
	};

}
