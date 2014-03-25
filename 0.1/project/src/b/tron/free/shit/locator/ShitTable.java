package b.tron.free.shit.locator;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShitTable {

	public static final String TABLE_SHIT = "shit";
	public static final String C_ID = "_id";
	public static final String C_ITEM = "shititem";
	public static final String C_COORDS = "shitcoords";
	public static final String C_WHERE = "shitwhere";
	
	private static final String DATABASE_CREATE = "create table "
	+ TABLE_SHIT + "(" + C_ID
	+ " integer primary key autoincrement, "
	+ C_ITEM + " text not null, "
	+ C_COORDS + " text not null, "
	+ C_WHERE + " text not null" 
	+ ");";
	
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ShitTable.class.getName(),
				"Upgrading database from version" + oldVersion + " to "
		+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIT);
		onCreate(db);
	}
}
