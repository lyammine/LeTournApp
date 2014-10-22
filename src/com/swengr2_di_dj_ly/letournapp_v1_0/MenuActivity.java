package com.swengr2_di_dj_ly.letournapp_v1_0;

import android.app.Activity;
import android.widget.Toast;

import com.swengr2_di_dj_ly.letournapp_v1_0.LetuNetworkThread.OnLetuUpdateListener;

public abstract class MenuActivity extends Activity 
		implements OnLetuUpdateListener {
	
	protected LetuNetworkThread networkChannel;
	protected String url;
	
	/**
	 * When the status of the internet connection
	 * is obtained, this method is called. If
	 * internet is not connected, this displays
	 * a Toast message to inform the user that
	 * network connection is necessary.
	 * 
	 * @author Devon Johnson
	 */
	@Override
	public void onNetworkStateObtained(boolean state) {
		
		if (!state) { // if disconnected
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "Internet " +
							"must be connected!", Toast.LENGTH_LONG).show();
				} // end run
			});
		} // end if
		
	} // end onNetworkStateObtained
	
	/**
	 * Subclasses should implement this method
	 * to inflate all objects from the XML activity
	 * file into the .java file. i.e.
	 * this.textView = findViewById(R.id.textView1);
	 * 
	 * @author Devon Johnson
	 */
	protected abstract void initActivityObjects();
	
	/**
	 * Subclasses should implement this method
	 * to parse the data from the web page and
	 * do something with it in the UI. This method
	 * should not be executed on the UI thread.
	 * 
	 * @author Devon Johnson
	 */
	protected abstract void parseData();
	
	/**
	 * Starts the networkChannel by setting this
	 * as the listener and starting the thread.
	 * 
	 * @author Devon Johnson
	 * @throws NullPointerException if networkChannel is null
	 */
	protected void startNetworkThread()
			throws NullPointerException {
		
		if (networkChannel == null) {
			NullPointerException ex = new NullPointerException(
					"LetuNetworkThread object was not initialized");
			throw(ex);
		} // end if
		
		networkChannel.setOnLetuUpdateListener(this);
		networkChannel.start();
		
	} // end initAndStartNetworkThread
	
} // end MenuActivity abstract class
