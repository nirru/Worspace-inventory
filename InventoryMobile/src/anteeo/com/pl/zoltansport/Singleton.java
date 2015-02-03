package anteeo.com.pl.zoltansport;

import holder.GroupItem;

/**
 * Created with IntelliJ IDEA. Date: 13/05/13 Time: 10:36
 */
public class Singleton {
	private static Singleton mInstance = null;

	private GroupItem itemCheckedList;
	private String barCode;
	private boolean isBarCodeToBind;

	private Singleton() {
//		itemCheckedList = new GroupItem();
		barCode = "";
		isBarCodeToBind = false;
	}


	public static Singleton getInstance() {
		if (mInstance == null) {
			mInstance = new Singleton();
		}
		return mInstance;
	}

	public GroupItem getItemCheckedList() {
		return this.itemCheckedList;
	}

	public void setItemCheckedList(GroupItem itemCheckedList) {
		this.itemCheckedList = itemCheckedList;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public boolean isBarCodeToBind() {
		return isBarCodeToBind;
	}

	public void setBarCodeToBind(boolean isBarCodeToBind) {
		this.isBarCodeToBind = isBarCodeToBind;
	}
}
