package adapter;

import java.util.ArrayList;

import custom.ShrinkList;

import holder.ChildHolder;
import holder.ChildItem;
import holder.GroupItem;
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
import android.widget.SearchView;
import android.widget.TextView;
import animation.ListYouAnimation;
import anteeo.com.pl.zoltansport.R;

/**
 * Created by C-ShellWin on 12/28/2014.
 */
public class PrivacyAdapter extends BaseAdapter {

	private Context mContext;
	private GroupItem groupitem;
	private LayoutInflater inflater;
	ListView mListView;
	private ShrinkList shrinkList;

	public PrivacyAdapter(Context mContext, GroupItem groupitem,
			ListView mListView,ShrinkList shrinkList) {
		this.mContext = mContext;
		this.groupitem = groupitem;
		this.mListView = mListView;
		this.shrinkList = shrinkList;
		// Log.e("ITEM SIZE", " " + groupitem.items.size());
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
		holder.selectionBox.setOnClickListener(ll);
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


	View.OnClickListener ll = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < groupitem.items.size(); i++) {
				CheckBox checkBox = (CheckBox) v
						.findViewById(R.id.selection_imageview);
				getItem(i).box = false;
				if (checkBox.isChecked()) {
					getItem((Integer) checkBox.getTag()).box = true;
				} else {
					getItem((Integer) checkBox.getTag()).box = false;
				}
			}
			
			notifyDataSetChanged();
			shrinkList.shrink();
		}
	};

}
