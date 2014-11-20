package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AnnouncementsActivity extends MenuActivity {

	private static AlertDialog.Builder alert = null;
	private TableLayout announcTable;
	protected static final String ANNOUNC_URL = "http://letustartpage.blogspot.com/feeds/"
			+ "posts/default?alt=rss";
	private String webData;
	
	
	private void addRowToTable(TableRow row) 
	{
		final TableRow theRow = row;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				announcTable.addView(theRow);
			}
		});
	} // end addRowToTable

	private String decodeUtf8(String original) 
	{	
		String utf8 = original;
		for (String[] utf8ReplaceKey : utf8ReplaceKeys)
			utf8 = utf8.replaceAll(utf8ReplaceKey[1], utf8ReplaceKey[0]);
		return utf8;
	} // end decodeUtf8
	
	@Override
	protected void initActivityObjects() {
		
		url = ANNOUNC_URL;
		announcTable = (TableLayout)findViewById(R.id.announcementsTable);
		networkChannel = new UnsecureLetuNetworkThread(url, getApplicationContext());
		startNetworkThread();
		
	} // end initActivityObjects
	
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
	public void onExceptionRetrievingNetworkData(Exception ex) {
//		setTextViewText(ex.toString());
	} // end onExceptionRetrievingNetworkData
	
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
		
//		setTextViewText(sb.toString());
		webData = decodeUtf8(sb.toString());
//		parseData();
		
//		webReader = reader;
		parseData();

//		setTextViewText(webData);
		
	} // end onWebPageDataObtained

	@Override
	protected void parseData() {
		if (webData == null)
			return;
		
		List<RssItem> rssItems = new ArrayList<RssItem>();
		RssItem currentItem = null;
		
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(webData));
			int eventType = parser.getEventType();
			boolean isCreatingItem = false;
			
			boolean isReadingDescription = false;
			StringBuilder descriptionStrBuilder = new StringBuilder();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				try {
					String tagName = parser.getName();
					switch (eventType) {
					
						case XmlPullParser.START_TAG:
							// Item tag signifies beginning of RSS Item
							if (tagName.equalsIgnoreCase("item")) {
	                            currentItem = new RssItem();
	                            isCreatingItem = true;
	                        }
	                        // Title tag signifies title of Announcement
	                        else if(tagName.equalsIgnoreCase("title") && isCreatingItem) {
	                            currentItem.setTitle(parser.nextText());
	                        }
	                        // Description tag signifies description of Announcement
	                        else if(tagName.equalsIgnoreCase("description") && isCreatingItem) {
	                        	descriptionStrBuilder = new StringBuilder();
	                        	isReadingDescription = true;
	                        }
	                        // Link tag signifies link of Announcement
	                        else if(tagName.equalsIgnoreCase("link") && isCreatingItem) {
	                            currentItem.setLink(parser.nextText());
	                        }
	                        break;
	                        
						case XmlPullParser.END_TAG:
							// End item tag signifies that this item is finished and can be added to the list
							if (tagName.equalsIgnoreCase("item")) {
	                    		rssItems.add(currentItem);
	                    		isCreatingItem = false;
	                    	}
							// If reading description, 
							else if (isReadingDescription) {
								if (tagName.equalsIgnoreCase("o:p") ||
										tagName.equalsIgnoreCase("br") ||
										tagName.equalsIgnoreCase("li")) {
									descriptionStrBuilder.append("\n");
									// Skip to next line o:p or br encountered
								}
								else if (tagName.equalsIgnoreCase("description")) {
									isReadingDescription = false;
									currentItem.setDescription(descriptionStrBuilder.toString());
								}
							} // end if isReadingDescription
	                    	break;
	                    	
						case XmlPullParser.TEXT:
							if (isReadingDescription) {
								descriptionStrBuilder.append(parser.getText());
							}
							break;
	                    	
	                    default:
	                    	break;
					} // end switch
					
				} finally {
					eventType = parser.next();
				}
			} // end while
			
			for (RssItem rssItem : rssItems) {
				
				TableRow row = new TableRow(this);
				TextView textView = new TextView(this);
	        	textView.setText(rssItem.getTitle());
	        	textView.setBackgroundResource(R.drawable.table_row_border);
	        	textView.setGravity(Gravity.LEFT);
	        	textView.setPadding(15, 20, 15, 20);
	        	textView.setTextSize(18);
	        	
	        	row.addView(textView);
	        	row.setOnClickListener(new AnnouncementListener(rssItem, this));
	        	addRowToTable(row);
			} // end for
			
		} catch (Exception e) { 
//			setTextViewText(e.toString());
		}
	} // end parseData
	
	public static final String[][] utf8ReplaceKeys = {
        {"\"", "&quot;"},
        {"&", "&amp;"},
        {"<", "&lt;"},
        {">", "&gt;"},
        {" ", "&nbsp;"}
    };
	
	public static class AnnouncementListener implements OnClickListener 
	{	
		private RssItem announcement;
		private Context context;
		private boolean annoucementOpen = false;
		
		public AnnouncementListener(RssItem announcement, Context context) 
		{
			this.announcement = announcement;
			this.context = context;
		}
		
		@Override
		public void onClick(View arg0) {

			if(annoucementOpen == false) {
				
				alert = new AlertDialog.Builder(context);
				annoucementOpen = true;
				ScrollView scrollView = new ScrollView(context);
				
				TextView textView = new TextView(context);
				textView.setText(announcement.getDescription());
				textView.setTextSize(18);
				textView.setPadding(15, 15, 15, 15);
				scrollView.addView(textView);
				alert.setView(scrollView);
				
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						annoucementOpen = false;
						dialog.dismiss();
					}
				}); // End setPositiveButton
				alert.show();
			} // End annoucementOpen check
		}
	}
	
	public static class RssItem 
	{	
		private String title;
		private String description;
		private String link;
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
		public String getLink() {
			return link;
		}
		
		public void setLink(String link) {
			this.link = link;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\nTitle: " + title);
			sb.append("\nDescription: " + description);
			sb.append("\nLink: " + link);
			return sb.toString();
		}
	} // end RssItem class
	
} // end AnnouncementsActivity class

