package anteeo.com.pl.zoltansport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CustomersDbAdapter {

	public static final String KEY_ROWID = "rowid";
	public static final String KEY_SIZE_ID = "size_id";
	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_SIZE = "size";
	public static final String KEY_NAME = "name";
	public static final String KEY_COLOR = "color";
	public static final String KEY_CODE = "code";
	public static final String KEY_STOCK = "stock";
	public static final String KEY_SYMBOL = "symbol";
	public static final String KEY_SEARCH = "searchData";

	private static final String TAG = "CustomersDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "CustomerData";
	public static final String FTS_VIRTUAL_TABLE = "CustomerInfo";
	private static final int DATABASE_VERSION = 1;

	// Create a FTS3 Virtual Table for fast searches
	private static final String DATABASE_CREATE = "CREATE VIRTUAL TABLE "
			+ FTS_VIRTUAL_TABLE + " USING fts3(" + KEY_SIZE_ID + "," + KEY_NAME
			+ "," + KEY_PRODUCT_ID + "," + KEY_SIZE + "," + KEY_COLOR + ","
			+ KEY_CODE + "," + KEY_STOCK + "," + KEY_SYMBOL + "," + KEY_SEARCH
			+ "," + " UNIQUE (" + KEY_SIZE_ID + "));";

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
			onCreate(db);
		}
	}

	public CustomersDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public CustomersDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long createCustomer(String size_id, String name, String product_id,
			String size, String color, String code, String stock, String symbol) {

		ContentValues initialValues = new ContentValues();
		String searchValue = size_id + " " + name + " " + product_id + " "
				+ size + " " + color + " " + code + " " + stock + " " + symbol;
		initialValues.put(KEY_SIZE_ID, size_id);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_PRODUCT_ID, product_id);
		initialValues.put(KEY_SIZE, size);
		initialValues.put(KEY_COLOR, color);
		initialValues.put(KEY_CODE, code);
		initialValues.put(KEY_STOCK, stock);
		initialValues.put(KEY_SYMBOL, symbol);
		initialValues.put(KEY_SEARCH, searchValue);

		return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
	}

	public Cursor searchCustomer(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		String query = "SELECT docid as _id," + KEY_SIZE_ID + "," + KEY_NAME
				+ "," + KEY_PRODUCT_ID + "," + KEY_SIZE + "," + KEY_COLOR + ","
				+ KEY_CODE + "," + KEY_STOCK + "," + KEY_SYMBOL + " from "
				+ FTS_VIRTUAL_TABLE + " where " + KEY_SEARCH + " MATCH '"
				+ inputText + "';";
		Log.w(TAG, query);
		Cursor mCursor = mDb.rawQuery(query, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean deleteAllCustomers() {

		int doneDelete = 0;
		doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public boolean checkForClientTables() {
		String whereClause = "Select * from "
				+ CustomersDbAdapter.FTS_VIRTUAL_TABLE;

		Cursor cursor = mDb.rawQuery(whereClause, null);
		if (cursor.getCount() == 0)
			return false;
		if (cursor.getCount() > 0)
			return true;

		cursor.close();
		return false;
	}
}