package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.InputMismatchException;

import org.apache.http.HttpException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ChapAttActivity extends MenuActivity {
	
	protected TableLayout chapelTable;
	protected TextView myTextView;
	protected static final String CHAP_ATT_URL = 
			"https://ssl.letu.edu/applications/" +
			"chapelattendance/attendance.html";
			
	protected BufferedReader networkReader;
	
	protected int creditsRecorded;
	protected int creditsRequired;
	
	private String username;
	private String password;
	
	public static final String SAVED_USERNAME_KEY = "Username";
	public static final String SAVED_PASSWORD_KEY = "Password";
	
	protected ArrayList<ChapelOpportunity> chapels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chap_att);
		
		if (savedInstanceState != null) {
			username = savedInstanceState.getString(SAVED_USERNAME_KEY);
			password = savedInstanceState.getString(SAVED_PASSWORD_KEY);
		} // end if
		
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
	protected void onSaveInstanceState(Bundle outState) {
		if (username != null && password != null) {
			outState.putString(SAVED_USERNAME_KEY, username);
			outState.putString(SAVED_PASSWORD_KEY, password);
		}
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onWebPageReaderObtained(BufferedReader reader) {
		
		networkReader = reader;
		parseData();
		
	} // end onWebPageDataObtained

	@Override
	public void onExceptionRetrievingNetworkData(Exception ex) {
		
		if (ex instanceof HttpException)
			setTextViewText("Error: Credentials not accepted");
		else
			setTextViewText("Error: website did not respond");
		
	} // end onExceptionRetrievingNetworkData
	
	/**
	 * Called when Refresh button is pressed.
	 * Fixes issue where user would have to exit
	 * back to the main screen and come back to
	 * ChapAtt in order to re-enter credentials
	 * in the event of credential failure or
	 * lack of credentials.
	 * 
	 * @param view Required for onClick method. Unused
	 * @author Devon Johnson
	 */
	public void refreshPage(View view) {
		initActivityObjects();
	} // end refresh
	
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
		
		chapels = new ArrayList<ChapelOpportunity>();
		
		myTextView = (TextView) findViewById(R.id.textView1);
		chapelTable = (TableLayout) findViewById(R.id.myTableLayout);
		chapelTable.removeAllViews();
		
		if (username == null || password == null) {
			
			// Get username and password from user
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Credentials");
			alert.setMessage("Enter username and password");
			final EditText nameText = new EditText(this);
			nameText.setHint("Username");
			final EditText passText = new EditText(this);
			passText.setHint("Password");
			passText.setInputType(InputType.TYPE_CLASS_TEXT | 
					InputType.TYPE_TEXT_VARIATION_PASSWORD);
			TableLayout tbl = new TableLayout(this);
			tbl.addView(nameText);
			tbl.addView(passText);
			alert.setView(tbl);
			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					username = nameText.getText().toString();
					password = passText.getText().toString();
					initNetwork();
				} // end onClick
			});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setTextViewText("Error: credentials not provided");
				} // end run
			});
			alert.show();
			
		} else {
			initNetwork();
		} // end if-else
		
	} // end initActivityObjects
	
	/**
	 * Parses through the data from the network.
	 * Reads in the list of chapels and
	 * credits and display them in a table
	 * 
	 * @author Devon Johnson
	 */
	@Override
	protected void parseData() {
		
		String data = null;
		
		try {
			
			while ((data = networkReader.readLine()) != null) {
				if (data.contains("<p>We"))
					break;
			} // end while
			if (data == null)
				throw new InputMismatchException();
			
			int temp1 = data.indexOf("<b>");
			int temp2 = data.indexOf("</b>");
			int temp3 = data.lastIndexOf("<b>");
			int temp4 = data.lastIndexOf("</b>");
			
			if (temp1 == -1 || temp2 == -1 ||
					temp3 == -1 || temp4 == -1 ||
					temp1 == temp3 || temp2 == temp4)
				throw new InputMismatchException();
			
			creditsRecorded = Integer.parseInt(data.substring(temp1 + 3, temp2));
			creditsRequired = Integer.parseInt(data.substring(temp3 + 3, temp4));
			
			while ((data = networkReader.readLine()) != null) {
			
				if (data.contains("</table>"))
					break;
				
				String tempDate = null;
				String tempTime = null;
				String tempDesc = null;
				int tempCredits = 0;
				int tempPossCredits = 0;
				
				while ((data = networkReader.readLine()) != null) {
					temp1 = data.indexOf("<td>");
					temp2 = data.indexOf("</td>");
					if (temp1 != -1 && temp2 != -1)
						break;
				} // end while
				if (data == null)
					break;
				
				tempDate = data.substring(temp2 - 6, temp2);
				data = networkReader.readLine();
				if (data == null)
					break;
				
				temp1 = data.indexOf("</td>");
				if (temp1 == -1)
					break;
				
				tempTime = data.substring(temp1 - 5, temp1);
				data = networkReader.readLine();
				if (data == null)
					break;
				
				temp1 = data.indexOf("<td>");
				temp2 = data.indexOf("</td>");
				if (temp1 == -1 || temp2 == -1)
					break;
				
				tempDesc = data.substring(temp1 + 4, temp2);
				data = networkReader.readLine();
				if (!data.contains("<td"))
					break;
				
				while ((data = networkReader.readLine()) != null) {
					if (data.contains("</td>"))
						break;
					temp1 = data.indexOf("alt=");
					if (temp1 == -1)
						throw new InputMismatchException();
					tempPossCredits++;
					if (data.substring(temp1 + 5, temp1 + 11).equals("credit"))
						tempCredits++;
				} // end while
				if (data == null)
					break;
				
				ChapelOpportunity chap = new ChapelOpportunity();
				chap.setDate(tempDate);
				chap.setTime(tempTime);
				chap.setDescription(tempDesc);
				chap.setCredits(tempCredits);
				chap.setPossibleCredits(tempPossCredits);
				
				chapels.add(chap);
			
			} // end while
			
			setTextViewText("You have " + creditsRecorded + " credits\n" +
							"You need " + creditsRequired + " credits");
			chapelTable.setColumnStretchable(1, true);
			
			for (ChapelOpportunity chap : chapels) {
				
				TableRow row = new TableRow(this);
				if (chap.getCredits() > 0)
					row.setBackgroundColor(getResources().getColor(R.color.LeTournApp_color_yellow));
				else
					row.setBackgroundColor(Color.WHITE);
				
				TextView tv1 = new TextView(this);
	        	tv1.setText(chap.getDate() + "\n" + chap.getTime());
	        	tv1.setBackgroundResource(R.drawable.table_row_border);
	        	tv1.setGravity(Gravity.CENTER);
	        	tv1.setPadding(15, 20, 15, 20);
	        	tv1.setTextSize(16);
	        	
	        	TextView tv2 = new TextView(this);
	        	tv2.setText(chap.getDescription());
	        	tv2.setBackgroundResource(R.drawable.table_row_border);
	        	tv2.setGravity(Gravity.LEFT);
	        	tv2.setPadding(15, 20, 15, 20);
	        	tv2.setTextSize(16);
	        	tv2.setLines(2);
	        	
	        	row.addView(tv1);
	        	row.addView(tv2);
	        	row.setOnClickListener(new ChapelClickListener(chap, this));
	        	
	        	final TableRow tr = row;
	        	
	        	runOnUiThread(new Runnable() {
	        		public void run() {
	        			chapelTable.addView(tr);
	        		}
	        	});
				
			} // end for
				
		} catch (Exception ex) {
			setTextViewText("Error parsing webpage");
		} // end try-catch
		
	} // end parseData
	
	/**
	 * Initializes the network thread with
	 * the given username and password. If
	 * username or password not initialized,
	 * does nothing.
	 * 
	 * @author Devon Johnson
	 */
	private void initNetwork() {
		if (username == null || password == null)
			return;
		networkChannel = new SecureLetuNetworkThread(url,
				getApplicationContext(), username, password);
		startNetworkThread();
	} // end initNetwork
	
	private void setTextViewText(String text) {
		
		final String theText = text;
		final TextView textView = myTextView;
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(theText);
			} // end run
		});
		
	} // end setTextViewText
	
	private static class ChapelOpportunity {
		
		private String date;
		private String time;
		private String description;
		private int credits;
		private int possibleCredits;
		
		public ChapelOpportunity() {
		}
		
		public void setDate(String date) {
			this.date = date;
		}
		
		public void setTime(String time) {
			this.time = time;
		}
		
		public void setDescription(String description) {
			String temp;
			int index = description.indexOf("&amp;");
			if (index == -1)
				temp = description;
			else
				temp = description.substring(0, index + 1) + 
						description.substring(index + 5, description.length());
			this.description = temp;
		}
		
		public void setCredits(int credits) {
			this.credits = credits;
		}
		
		public void setPossibleCredits(int possibleCredits) {
			this.possibleCredits = possibleCredits;
		}
		
		public String getDate() {
			return date;
		}
		
		public String getTime() {
			return time;
		}
		
		public String getDescription() {
			return description;
		}
		
		public int getCredits() {
			return credits;
		}
		
		public int getPossibleCredits() {
			return possibleCredits;
		}
		
	} // end ChapelOpportunity class

	private static class ChapelClickListener implements OnClickListener {
		private ChapelOpportunity chap;
		private Context context;
		
		public ChapelClickListener(ChapelOpportunity chap, Context context) {
			this.chap = chap;
			this.context = context;
		} // end constructor
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder alert = new AlertDialog.Builder(
					context);
			TextView tv = new TextView(context);
			tv.setText(chap.getDescription() + "\n\n" +
					chap.getDate() + "\n" + chap.getTime()
					+ "\n\n" + "Credits: " + chap.getCredits()
					+ " out of a possible " + chap.getPossibleCredits());
			tv.setTextSize(18);
			tv.setPadding(15, 15, 15, 15);
			alert.setView(tv);
			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
			});
			alert.show();
			
		} // end run
		
	} // end ChapelClickListener
	
} // end ChapAttActivity class
