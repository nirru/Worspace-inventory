package mydatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import anteeo.com.pl.zoltansport.CustomersDbAdapter;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "northwind.db";
	private static final int DATABASE_VERSION = 1;

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// you can use an alternate constructor to specify a database location
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		// super(context, DATABASE_NAME,
		// context.getExternalFilesDir(null).getAbsolutePath(), null,
		// DATABASE_VERSION);

	}

	public Cursor getProducts() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = { "docid _id", CustomersDbAdapter.KEY_SIZE_ID,
				CustomersDbAdapter.KEY_NAME, CustomersDbAdapter.KEY_PRODUCT_ID,
				CustomersDbAdapter.KEY_SIZE, CustomersDbAdapter.KEY_COLOR,
				CustomersDbAdapter.KEY_CODE, CustomersDbAdapter.KEY_STOCK,
				CustomersDbAdapter.KEY_SYMBOL };
		String sqlTables = CustomersDbAdapter.FTS_VIRTUAL_TABLE;

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

		c.moveToFirst();
		return c;

	}

	public Cursor searchCustomer(String inputText) throws SQLException {
		Log.w("INPUT TWXT", inputText);
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT docid as _id," + CustomersDbAdapter.KEY_SIZE_ID
				+ "," + CustomersDbAdapter.KEY_NAME + ","
				+ CustomersDbAdapter.KEY_PRODUCT_ID + ","
				+ CustomersDbAdapter.KEY_SIZE + ","
				+ CustomersDbAdapter.KEY_COLOR + ","
				+ CustomersDbAdapter.KEY_CODE + ","
				+ CustomersDbAdapter.KEY_STOCK + ","
				+ CustomersDbAdapter.KEY_SYMBOL + " from "
				+ CustomersDbAdapter.FTS_VIRTUAL_TABLE + " where "
				+ CustomersDbAdapter.KEY_SEARCH + " MATCH '" + inputText + "';";
		Log.w("QUEY TAG", query);
		Cursor mCursor = db.rawQuery(query, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

}
