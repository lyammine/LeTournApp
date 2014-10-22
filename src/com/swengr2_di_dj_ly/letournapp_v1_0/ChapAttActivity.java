package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.io.IOException;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChapAttActivity extends MenuActivity {
	
	protected TextView myTextView;
	protected static final String CHAP_ATT_URL = 
			"https://ssl.letu.edu/applications/" +
			"chapelattendance/attendance.html";
			
	protected String username;
	protected String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chap_att);
		
		initActivityObjects();
		
	} // end onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chap_att, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onWebPageReaderObtained(BufferedReader reader) {
		
		String data = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			while ((data = reader.readLine()) != null)
				sb.append(data);
		} catch (IOException e) {
			sb = new StringBuilder("Error retrieving data");
		} // end try-catch
		
		parseData();
		setTextViewText("Data retrieved successfully");
		
	} // end onWebPageDataObtained

	@Override
	public void onExceptionRetrievingNetworkData(Exception ex) {
		
		setTextViewText(ex.toString());
		
	} // end onExceptionRetrievingNetworkData
	
	/**
	 * Inflates the UI objects for use in
	 * data manipulation. Also initializes
	 * the Networking thread.
	 * 
	 * @author Devon Johnson
	 */
	@Override
	protected void initActivityObjects() {
		
		url = CHAP_ATT_URL;
		username = "xxx";
		password = "xxx";
		
		myTextView = (TextView) findViewById(R.id.textView1);
		networkChannel = new SecureLetuNetworkThread(url,
				getApplicationContext(), username, password);
		startNetworkThread();
		
	} // end initActivityObjects
	
	/**
	 * Parses through the data from the network.
	 * Should read in the list of chapels and
	 * credits and display them in a table
	 * 
	 * @author Devon Johnson
	 */
	@Override
	protected void parseData() {
		
		
		
	} // end parseData
	
	private void setTextViewText(String data) {
		
		final String theData = data;
		final TextView textView = myTextView;
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(theData);
			} // end run
		});
		
	} // end setTextViewText
	
} // end ChapAttActivity class
