package b.tron.free.shit.locator;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShitPage extends Activity {
	private EditText etItem;
	private EditText etCoords;
	private EditText etWhere;
	
	private Uri todouri;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_shit_page);
		
		etItem = (EditText) findViewById(R.id.shit_item);
		etCoords = (EditText) findViewById(R.id.shit_coords);
		etWhere = (EditText) findViewById(R.id.shit_where);
		Button bConfirm = (Button) findViewById(R.id.bConfirmShit);
		
		Bundle extras = getIntent().getExtras();
		
		todouri = (bundle == null) ? null : (Uri)bundle.getParcelable
				(ShitContentProvider.CONTENT_ITEM_TYPE);
		
		if(extras != null){
			todouri = extras.getParcelable(ShitContentProvider.CONTENT_ITEM_TYPE);
			fillData(todouri);
		}
		
		bConfirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (TextUtils.isEmpty(etItem.getText().toString()))
					makeToast();
				else{
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	
	private void fillData(Uri uri) {
		String[] projection = { ShitTable.C_ITEM,
				ShitTable.C_COORDS, ShitTable.C_WHERE };
		Cursor cursor = getContentResolver().query(uri, projection, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			etItem.setText(cursor.getString(cursor.getColumnIndexOrThrow(ShitTable.C_ITEM)));
			etCoords.setText(cursor.getString(cursor.getColumnIndexOrThrow(ShitTable.C_COORDS)));
			etWhere.setText(cursor.getString(cursor.getColumnIndexOrThrow(ShitTable.C_WHERE)));
			
			cursor.close();
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(ShitContentProvider.CONTENT_ITEM_TYPE, todouri);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shit_page, menu);
		return true;
	}

	private void saveState() {
		String item = (String) etItem.getText().toString();
		String coords = (String) etCoords.getText().toString();
		String where = (String) etWhere.getText().toString();
		
		if (item.length() == 0 && coords.length() == 0)
			return;
		
		ContentValues values = new ContentValues();
		values.put(ShitTable.C_ITEM, item);
		values.put(ShitTable.C_COORDS, coords);
		values.put(ShitTable.C_WHERE, where);
		
		if (todouri == null)
			todouri = getContentResolver().insert(ShitContentProvider.CONTENT_URI, values);
		else
			getContentResolver().update(todouri, values, null, null);
	}
	
	private void makeToast() {
		Toast.makeText(ShitPage.this, "Please maintain a summary",
				Toast.LENGTH_LONG).show();
	}

}
