package holder;

public class ChildItem {
	public String mSize_id;
	public String mName;
	public String product_id;
	public String mColor;
	public String mCode;
	public String mStock;
	public String mSize;
	public String mSymbol;
	public String barcode;
	public int count;
	public boolean box;
	public boolean focus;
	public boolean isBarCodeToBind;

	public ChildItem() {
	}

	public ChildItem(String mSize_id, String mName,String mColor,String mCode,String mSize,int count) {
		this.mSize_id = mSize_id;
		this.count = count;
		this.mName = mName;
		this.mColor = mColor;
		this.mCode = mCode;
		this.mSize = mSize;
	}

	public void setId(String id) {
		this.mSize_id = id;
	}

	public String getId() {
		return mSize_id;
	}
	
	public void setName(String mName) {
		this.mName = mName;
	}

	public String getName() {
		return mName;
	}
	
	public void setColor(String mColor) {
		this.mColor = mColor;
	}
	
	public String getColor() {
		return mColor;
	}
	
	public void setCode(String mCode) {
		this.mCode = mCode;
	}
	
	public String getCode() {
		return mCode;
	}
	
	public void setSize(String mSize) {
		this.mSize = mSize;
	}
	
	public String getSize() {
		return mSize;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
}

