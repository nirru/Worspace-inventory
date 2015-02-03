package adapter;

import util.Util;
import holder.ChildHolder;
import holder.ChildItem;
import holder.GroupItem;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import animation.ListYouAnimation;
import anteeo.com.pl.zoltansport.R;

/**
 * Created by C-ShellWin on 12/28/2014.
 */
public class MainListAdapter extends BaseAdapter {

	private Context mContext;
	private GroupItem groupitem;
	private LayoutInflater inflater;
	ListView mListView;

	public MainListAdapter(Context mContext, GroupItem groupitem,
			ListView mListView) {
		this.mContext = mContext;
		this.groupitem = groupitem;
		this.mListView = mListView;
		inflater = LayoutInflater.from(mContext);
//		Log.e("GROUP ITEM SIZE", "" + groupitem.items.size());
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
			convertView = inflater.inflate(R.layout.row, parent, false);
			holder = new ChildHolder();
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		holder.mDelete = (ImageView) convertView.findViewById(R.id.imageView1);
		holder.mName = (TextView) convertView.findViewById(R.id.sname);
		holder.mColor = (TextView) convertView.findViewById(R.id.scolor);
		holder.mSize = (TextView) convertView.findViewById(R.id.ssize);
		holder.mAmount = (EditText) convertView.findViewById(R.id.enter_amount);
		holder.mAmount.requestFocus();
		holder.mAmount.setFocusable(item.focus);
		holder.mAmount.setFocusableInTouchMode(item.focus);
		holder.mAmount.setText("" + item.count);
		holder.mName.setText(item.mName);
		holder.mColor.setText(item.mColor);
		if (item.mSize == null || item.mSize.equals("") || item.mSize == "null") {
			holder.mSize.setText("( " + "No size avilable" + " )");
		} else {
			holder.mSize.setText("( " + item.mSize + " )");
		}
		holder.mDelete.setTag((position));
		holder.mDelete.setOnClickListener(l);
		holder.mAmount.setTag(position);
		holder.mAmount.setOnClickListener(ll);
		holder.mAmount.setOnEditorActionListener(onEdit);

		return convertView;
	}

	View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = (Integer) v.getTag();
			groupitem.items.remove(id);
			if (mListView != null) {
				mListView.getChildAt(id).startAnimation(
						ListYouAnimation.inFromLeftAnimation());
				notifyDataSetChanged();
			}

		}
	};

	View.OnClickListener ll = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setEditField(v);

		}
	};

	private void setEditField(View v) {
		EditText ed = (EditText) v.findViewById(R.id.enter_amount);
		
		for (int i = 0; i < groupitem.items.size(); i++) {
			getItem(i).focus = false;
		}
		ed.requestFocus();
		ed.setFocusable(true);
		ed.setFocusableInTouchMode(true);
		if (ed.requestFocus()) {
			Log.e("ON", "" + "HAI");
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
			getItem((Integer) ed.getTag()).focus = true;
			updateAmount(ed);
		}

	}

	private void updateAmount(EditText ed) {
		final int id = (Integer) ed.getTag();
		final ChildItem childItem = getItem(id);
		ed.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					childItem.setCount(Integer.parseInt(s.toString()));
				} else {
					childItem.setCount(0);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// notifyDataSetChanged();
			}
		});
	}
	
	OnEditorActionListener onEdit = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (v instanceof EditText) {
					EditText edText = (EditText)v.findViewById(R.id.enter_amount);
				    Util.hideKeyboard(mContext, edText);
				}
				
				return true;
			}
			return false;
		}
	};
	
}
