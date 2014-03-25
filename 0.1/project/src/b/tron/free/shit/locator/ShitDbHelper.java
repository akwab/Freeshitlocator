package b.tron.free.shit.locator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShitDbHelper extends SQLiteOpenHelper{
	private static final String DB_NAME = "shittable.db";
	private static final int DB_VERSION = 1;

	public ShitDbHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ShitTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ShitTable.onUpgrade(db, oldVersion, newVersion);
	}

}
