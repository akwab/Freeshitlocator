package b.tron.free.shit.locator;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ShitContentProvider extends ContentProvider{
	
	private ShitDbHelper Db;
	
	private static final int SHITS = 10;
	private static final int SHIT_ID = 20;

	private static final String AUTHORITY = "b.tron.free.shit.contentprovider";
	private static final String BASE_PATH = "shits";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/shits";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/shit";
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SHITS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SHIT_ID);
	}
	
	@Override
	public boolean onCreate() {
		Db = new ShitDbHelper(getContext());
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkCols(projection);
		
		queryBuilder.setTables(ShitTable.TABLE_SHIT);
		
		int uriType = sURIMatcher.match(uri);
		switch(uriType){
		case SHITS:
			break;
		case SHIT_ID:
			queryBuilder.appendWhere(ShitTable.C_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase dbase = Db.getWritableDatabase();
		Cursor cursor = queryBuilder.query(dbase, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	private void checkCols(String[] projection) {
		String[] available = {
				ShitTable.C_ID,	ShitTable.C_ITEM, ShitTable.C_COORDS,
				ShitTable.C_WHERE };
		if(projection != null){
			HashSet<String> requestedCols = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableCols = new HashSet<String>(Arrays.asList(available));
			if(!availableCols.containsAll(requestedCols)){
				throw new IllegalArgumentException("Unknown cs in projection");
			}
		}		
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = Db.getWritableDatabase();
		long id = 0;
		switch (uriType){
		case SHITS:
			id = sqlDB.insert(ShitTable.TABLE_SHIT, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDb = Db.getWritableDatabase();
		int rowsDel = 0;
		switch(uriType){
		case SHITS:
			rowsDel = sqlDb.delete(ShitTable.TABLE_SHIT, selection, selectionArgs);
			break;
		case SHIT_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDel = sqlDb.delete(ShitTable.TABLE_SHIT, ShitTable.C_ID + "=" + id, null);
			} else {
				rowsDel = sqlDb.delete(ShitTable.TABLE_SHIT, ShitTable.C_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		return rowsDel;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDb = Db.getWritableDatabase();
		int rowsUpd = 0;
		switch(uriType){
		case SHITS:
			rowsUpd = sqlDb.update(ShitTable.TABLE_SHIT, values, selection, selectionArgs);
			break;
		case SHIT_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsUpd = sqlDb.update(ShitTable.TABLE_SHIT, values, ShitTable.C_ID + "=" + id, null);
			} else {
				rowsUpd = sqlDb.update(ShitTable.TABLE_SHIT, values, ShitTable.C_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpd;
	}
	
	
	
	
}
