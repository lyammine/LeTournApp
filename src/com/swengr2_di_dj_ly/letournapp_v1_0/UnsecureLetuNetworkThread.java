package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class UnsecureLetuNetworkThread extends LetuNetworkThread {

	/**
	 * Creates an instance of UnsecureLetuNetworkThread
	 * given a url string and an application context
	 * 
	 * @author Devon Johnson
	 * @param urlString
	 * @param context
	 */
	public UnsecureLetuNetworkThread(String urlString, Context context) {
		super(urlString, context);
	} // end constructor
	
	/**
	 * Retrieves webpage data unsecurely from the
	 * specified URL. Outputs data through the
	 * listener, either on method onWebpageDataObtained
	 * or onExceptionRetrievingNetworkData
	 * 
	 * @author Devon Johnson
	 */
	@Override
	protected void execute() {
		
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
//			BufferedReader reader = new BufferedReader(isr);
//			
//			String data;
//			StringBuilder builder = new StringBuilder();
//			
//			while ((data = reader.readLine()) != null)
//				builder.append(data);
			
			if (listener != null)
				listener.onWebPageReaderObtained(new BufferedReader(isr));
			
		} catch (Exception ex) {
			if (listener != null)
				listener.onExceptionRetrievingNetworkData(ex);
		} // end try-catch
 
	} // end execute

} // end UnsecureLetuNetworkThread class
