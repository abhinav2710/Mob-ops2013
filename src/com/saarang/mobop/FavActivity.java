package com.saarang.mobop;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

public class FavActivity extends ListActivity {
	
	private EventDbAdapter mDbHelper ;
	private ArrayList<String> favs =null ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_view);
		mDbHelper = new EventDbAdapter(this);
		mDbHelper.open();
		fillfavData();
		registerForContextMenu(getListView());

	}
	
	@SuppressWarnings("deprecation")
	private void fillfavData(){
		favs=new ArrayList<String>();
		
		for (long rowid=1;rowid<=5;rowid++){
			Cursor mCursor =mDbHelper.fetchEvent(rowid);
			startManagingCursor(mCursor);
			if(mCursor.getString(mCursor.getColumnIndexOrThrow(EventDbAdapter.KEY_FAV))=="1")
				{
					favs.add(mCursor.getString(mCursor.getColumnIndexOrThrow(EventDbAdapter.KEY_TITLE)));	
				}
			
		}
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, favs));
	 }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//saveState();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
