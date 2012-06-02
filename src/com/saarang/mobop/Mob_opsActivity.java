package com.saarang.mobop;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Mob_opsActivity extends ListActivity {
	/** Called when the activity is first created. */
	public static final int ACTIVITY_VIEW	=0;
	public static final int ACITVITY_FAV	=1;
	private static final int VIEW_ID 		= Menu.FIRST;
	private static final int CALL_ID 		= Menu.FIRST + 1;
	private static final int FAV_ID 		= Menu.FIRST + 2;
	
	private static final String TAG  = "MobOps::Activity";
	private static String phn= new String();
	
	private EventDbAdapter mDbHelper;
	private ArrayList<String> menu=new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mDbHelper = new EventDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
		
	}
	
	@SuppressWarnings("deprecation")
	private void fillData() {
		final Cursor mEventCursor;
		mEventCursor = mDbHelper.fetchAllEvents();
		startManagingCursor(mEventCursor);

		String[] from = new String[]{EventDbAdapter.KEY_TITLE};

		int[] to = new int[]{R.id.text1};

		SimpleCursorAdapter events = 
			new SimpleCursorAdapter(this, R.layout.event_row, mEventCursor, from, to);
		setListAdapter(events);
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, FAV_ID, 0, R.string.menu_fav);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case FAV_ID:
				//favlist();
				return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, VIEW_ID, 0, R.string.menu_view);
		menu.add(Menu.NONE, CALL_ID, Menu.NONE, R.string.menu_call);
	}
	 
	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case VIEW_ID:
				
				Intent i = new Intent(this, ViewActivity.class);
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)item.getItemId());
				
				startActivityForResult(i, ACTIVITY_VIEW);
				
				return true;
			case CALL_ID:
				Long mRowId=(long) item.getItemId();
				if (mRowId!=null) {
					Cursor mCursor =mDbHelper.fetchEvent((long)item.getItemId());
					startManagingCursor(mCursor);
					phn=mCursor.getString(mCursor.getColumnIndexOrThrow(EventDbAdapter.KEY_PHN));
					
				}
				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+phn));
					startActivity(callIntent);
				} catch (ActivityNotFoundException activityException) {
					 Log.e(TAG, "Call failed");
					}
						
				return true;
			}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i(TAG, "List Item Selected " + this.getClass());
		Intent i = new Intent(this, ViewActivity.class);
		Log.i(TAG, "Intent initialised " + this.getClass());
		
		i.putExtra(EventDbAdapter.KEY_ROWID, id);
		
		Log.i(TAG, "Intent +RowId " + this.getClass());
		
		startActivityForResult(i, ACTIVITY_VIEW);
		Log.i(TAG, "Activity Started " + this.getClass());
		overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}
}