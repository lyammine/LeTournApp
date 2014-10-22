package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.io.IOException;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AnnouncementsActivity extends MenuActivity {

	private TextView myTextView;
	protected static final String ANNOUNC_URL = 
			"http://letustartpage.blogspot.com/feeds/" +
			"posts/default?alt=rss";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announcements);
		
		initActivityObjects();
		
	} // end onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.announcements, menu);
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
		
		setTextViewText(sb.toString());
		
	} // end onWebPageDataObtained

	@Override
	public void onExceptionRetrievingNetworkData(Exception ex) {
		setTextViewText(ex.toString());
	} // end onExceptionRetrievingNetworkData

	@Override
	protected void initActivityObjects() {
		
		url = ANNOUNC_URL;
		myTextView = (TextView) findViewById(R.id.textView1);
		networkChannel = new UnsecureLetuNetworkThread(url, getApplicationContext());
		startNetworkThread();
		
	} // end initActivityObjects

	@Override
	protected void parseData() {
		
	}
	
	private void setTextViewText(String data) {
		
		final TextView textView = myTextView;
		final String theData = data;
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(theData);
			} // end run
		});
		
	} // end setTextViewText
	
} // end AnnouncementsActivity class

