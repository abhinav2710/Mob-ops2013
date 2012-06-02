package com.saarang.mobop;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewActivity extends Activity implements OnGestureListener {
	
	private EventDbAdapter mDbHelper;
	private TextView mEvent;
	private TextView mDetails;
	private TextView mCoord;
	private Long mRowId;
	private static final String TAG  = "ViewActivity::Activity";
	private static String phn = new String();
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gestureDetector = new GestureDetector(this);
		Log.i(TAG, "Gesture Detector " + this.getClass());
			
		super.onCreate(savedInstanceState);
		Log.i(TAG, "View Created " + this.getClass());
		
		mDbHelper = new EventDbAdapter(this);
		Log.i(TAG, " Db created " + this.getClass());
		mDbHelper.open();
		Log.i(TAG, "View Created " + this.getClass());
		
		setContentView(R.layout.layout_view);
		setTitle(R.string.hello);
		Button favbtn = (Button) findViewById(R.id.fav);Log.i(TAG, "View Created " + this.getClass());
		Button callbtn=(Button) findViewById(R.id.Call);
		
		mEvent		=(TextView) findViewById(R.id.Event_name);
		mDetails	=(TextView) findViewById(R.id.Details);
		mCoord		=(TextView) findViewById(R.id.Contact);
		mRowId 		= (savedInstanceState == null) ? null:(Long) savedInstanceState.getSerializable(EventDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(EventDbAdapter.KEY_ROWID) : null ;
		}
		Log.i(TAG, " Btn created " + this.getClass());
		if (mRowId!=null){
			Log.i(TAG, "Row id " + Long.toString((long)mRowId));
			Cursor event = mDbHelper.fetchEvent(mRowId);
			Log.i(TAG, " event created " + this.getClass());
			startManagingCursor(event);
			Log.i(TAG, " event managed " + this.getClass());
			String temp=event.getString(event.getColumnIndexOrThrow(EventDbAdapter.KEY_FAV));
			Log.i(TAG, " temp created " + this.getClass());
			if(temp.contentEquals("1"))
			{
				Log.i(TAG, " if enterd created " + this.getClass());
				favbtn.setEnabled(true);
			}
			else
			{
				Log.i(TAG, " else enterd created " + this.getClass());
				favbtn.setEnabled(false);
			}
		}
		Log.i(TAG, "View Created " + this.getClass());
		populateFields();
		
		favbtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDbHelper.updateEvent(mRowId, "1");
				setResult(RESULT_OK);}});
		
		callbtn.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View view) {
				if (mRowId!=null) {
					Cursor mCursor =mDbHelper.fetchEvent(mRowId);
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
			}
		
		});
		
		super.onCreate(savedInstanceState);
		
	}
	
	public boolean onTouchEvent(MotionEvent me)
    {
    	return gestureDetector.onTouchEvent(me);
    }
	
	@SuppressWarnings("deprecation")
	private void populateFields() {

		if (mRowId != null) {
			Cursor event = mDbHelper.fetchEvent(mRowId);
			startManagingCursor(event);
			mEvent.setText(event.getString(event.getColumnIndexOrThrow(EventDbAdapter.KEY_TITLE)));
			mDetails.setText(event.getString(event.getColumnIndexOrThrow(EventDbAdapter.KEY_DETAIL)));
			mCoord.setText(event.getString(event.getColumnIndexOrThrow(EventDbAdapter.KEY_COORD))
							+"\n"+event.getString(event.getColumnIndexOrThrow(EventDbAdapter.KEY_PHN)));
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//saveState();
		outState.putSerializable(EventDbAdapter.KEY_ROWID, mRowId);
    }
	public void callnext(int n){
		
	/*	try {
			
			if(mRowId+n>4)
				
			if(mRowId+n<0)
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)(4));
			else
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)(mRowId+n));
			
			
			
		} catch (ActivityNotFoundException activityException) {
			Log.e(TAG, "Call failed");
		}*/
		Intent i = new Intent(this, ViewActivity.class);
		if(n==1)
		{
			if(mRowId==5){
			i.putExtra(EventDbAdapter.KEY_ROWID, (long)(1));
			startActivityForResult(i, 0);
			overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
			else
			{
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)(mRowId+1));
				startActivityForResult(i, 0);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
				
			}
		}
		else if(n==-1)
		{
			if(mRowId==1)
				
			{
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)(5));
				startActivityForResult(i, 0);
				overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			}
			else
			{
				i.putExtra(EventDbAdapter.KEY_ROWID, (long)(mRowId-1));
				startActivityForResult(i, 0);
				overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			}
		}
		
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		finish();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		//fillData();
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i(TAG, "FLING");
		if((e1.getX()-e2.getX())>0)
				callnext(1);
		if((e1.getX()-e2.getX())<0)
			callnext(-1);
		
		
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


		
}
