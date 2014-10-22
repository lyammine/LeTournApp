package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class LetuNetworkThread extends Thread {
	
	/**
	 * OnLetuUpdateListener:
	 * 
	 * Events will be fired from the
	 * networking thread in order to
	 * enable interaction with the UI
	 * thread. In the activity using
	 * this networking thread, implement
	 * an OnLetuUpdateListener to
	 * receive those events
	 * 
	 * @author Devon Johnson
	 */
	public interface OnLetuUpdateListener {
		/**
		 * Called when the state of the
		 * network connection is found
		 * 
		 * @author Devon Johnson
		 * 
		 * @param state true if connected
		 */
		public void onNetworkStateObtained (boolean state);
		
		/**
		 * Called when the data from the
		 * web page is obtained.
		 * 
		 * @author Devon Johnson
		 * 
		 * @param data text of the HTML file
		 */
		public void onWebPageReaderObtained (BufferedReader reader);
		
		/**
		 * Called if an exception is
		 * encountered when attempting to
		 * retrieve the web page data
		 * 
		 * @author Devon Johnson
		 * 
		 * @param ex an Exception
		 */
		public void onExceptionRetrievingNetworkData (Exception ex);
	
	} // end OnLetuUpdateListener

	protected static final int DEFAULT_CONNECT_TIMEOUT = 3000;
	
	protected OnLetuUpdateListener listener;
	protected String urlString;
	protected Context context;
	
	/**
	 * Constructor for LetuNetworkThread. Can only
	 * be instantiated from one of the subclasses:
	 * SecureLetuNetworkThread and UnsecureLetuNetworkThread
	 * 
	 * @author Devon Johnson
	 * @param urlString string representing the URL
	 * @param context Application's context
	 */
	public LetuNetworkThread(String urlString, Context context) {
		this.urlString = urlString;
		this.context = context;
	} // end constructor
	
	/**
	 * @author Devon Johnson
	 * @param listener A listener for events sent from this thread
	 */
	public void setOnLetuUpdateListener(OnLetuUpdateListener listener) {
		this.listener = listener;
	} // end setOnLetuUpdateListener

	/**
	 * Checks the state of the Android device's networking.
	 * i.e. Is either WiFi or Cellular Data connected?
	 * Fires the onNetworkStateObtained event in the listener
	 * @author Devon Johnson
	 * @return true if network is connected
	 */
	protected boolean checkNetworkState() {
		
		boolean state = false;
		ConnectivityManager check = (ConnectivityManager) context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (check != null) {
			NetworkInfo[] infos = check.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo info : infos) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						state = true;
						break;
					} // end if
				} // end for
			} // end if infos != null
		} // end if check !- null
		
		if (listener != null)
			listener.onNetworkStateObtained(state);
		return state;
		
	} // end checkNetworkState
	
	/**
	 * Inherited from superclass Thread
	 * To run this the LetuNetworkThread,
	 * call thread.start()
	 * 
	 * @author Devon Johnson
	 */
	@Override
	public void run() {
		
		if (checkNetworkState())
			execute();
		
	} // end run
	
	/**
	 * Implemented by subclasses.
	 * Meant to load the webpage data
	 * and send back to the UI using
	 * the listener object. Declaring
	 * this abstract allows the subclass
	 * to decide whether connection is
	 * secure or not.
	 * 
	 * @author Devon Johnson
	 */
	protected abstract void execute();
	
} // end LetuNetworkThread
