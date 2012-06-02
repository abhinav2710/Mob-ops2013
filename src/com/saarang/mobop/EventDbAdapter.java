package com.saarang.mobop;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class EventDbAdapter {
	public static final String KEY_TITLE 	= "title" ;
	public static final String KEY_DETAIL 	= "details" ;
	public static final String KEY_COORD	="coord" ;
	public static final String KEY_PHN		="phone" ;
	public static final String KEY_ROWID 	= "_id" ;
	public static final String KEY_FAV		="fav" ;

	private static final String TAG 		= "listadapter" ;
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	private static final String DATABASE_CREATE =
			"create table events (_id integer primary key autoincrement, "
			+ "title text not null, details text not null, coord text not null, phone text not null, fav text not null );";

	private static final String DATABASE_NAME 		= "data";
	private static final String DATABASE_TABLE 		= "events";
	private static final int DATABASE_VERSION 		= 2;

	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	 
	public EventDbAdapter(Context ctx){
		this.mCtx=ctx;
		ctx.deleteDatabase(DATABASE_NAME);
	}
	
	public EventDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE,"Event1");
		initialValues.put(KEY_COORD,"Coord Name");
		initialValues.put(KEY_PHN, "9999999999");
		initialValues.put(KEY_DETAIL,"Details");
		initialValues.put(KEY_FAV,"0");
		mDb.insert(DATABASE_TABLE, null, initialValues);
		
		initialValues.put(KEY_TITLE,"Event2");
		initialValues.put(KEY_COORD,"Coord Name");
		initialValues.put(KEY_PHN, "+919999999999");
		initialValues.put(KEY_DETAIL,"Details");
		initialValues.put(KEY_FAV,"0");
		mDb.insert(DATABASE_TABLE, null, initialValues);
		
		initialValues.put(KEY_TITLE,"Event3");
		initialValues.put(KEY_COORD,"Coord Name");
		initialValues.put(KEY_PHN, "+919999999999");
		initialValues.put(KEY_DETAIL,"Details");
		initialValues.put(KEY_FAV,"0");
		mDb.insert(DATABASE_TABLE, null, initialValues);
		
		initialValues.put(KEY_TITLE,"Event4");
		initialValues.put(KEY_COORD,"Coord Name");
		initialValues.put(KEY_PHN, "+919999999999");
		initialValues.put(KEY_DETAIL,"Details");
		initialValues.put(KEY_FAV,"0");
		mDb.insert(DATABASE_TABLE, null, initialValues);
		
		initialValues.put(KEY_TITLE,"Event5");
		initialValues.put(KEY_COORD,"Coord Name");
		initialValues.put(KEY_PHN, "+919999999999");
		initialValues.put(KEY_DETAIL,"Details");
		initialValues.put(KEY_FAV,"0");
		mDb.insert(DATABASE_TABLE, null, initialValues);
		
		return this;
		
	}

	public void close() {
		mDbHelper.close();
	}
	
	public Cursor fetchAllEvents() {
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
				KEY_COORD,KEY_DETAIL,KEY_PHN,KEY_FAV}, null, null, null, null, null);
	}
	
	public Cursor fetchEvent(long rowId) throws SQLException {
		Cursor mCursor =mDb.query(true, DATABASE_TABLE, 
					new String[] {KEY_ROWID,KEY_TITLE, KEY_COORD,KEY_DETAIL,KEY_PHN,KEY_FAV}, KEY_ROWID 
					+ "=" + rowId, null,null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public boolean updateEvent(long rowId, String fav) {
		ContentValues args = new ContentValues();
		args.put(KEY_FAV, fav);

		return mDb.update(DATABASE_TABLE, args, KEY_FAV + "=" + rowId, null) > 0;
	}

}