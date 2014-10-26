package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleHoursActivity extends Activity {
	
	public static final int TOTAL_DAYS = 7;
	
	private TextView[] ascTable;
	private TextView[] servTable;
	private TextView[] sagaTable;
	private TextView dayView;
	
	private HoursData hoursData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_hours);
		initActivityObjects();
	} // end onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_hours, menu);
		return true;
	} // end onCreateOptionsMenu

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
	} // end onOptionsItemSelected

	/**
	 * Inflates the UI objects for use
	 * in changing the text of labels
	 * 
	 * @author Devon Johnson
	 */
	private void initActivityObjects() {
		
		hoursData = new HoursData();
		
		ascTable = new TextView[hoursData.ASC_HOURS.length];
		servTable = new TextView[hoursData.SERV_HOURS.length];
		sagaTable = new TextView[hoursData.SAGA_HOURS.length];
		
		ascTable[0] = (TextView)findViewById(R.id.Editable_ASC_Main_Hours);
		ascTable[1] = (TextView)findViewById(R.id.Editable_ASC_Hive_Hours);
		ascTable[2] = (TextView)findViewById(R.id.Editable_ASC_Coffee_Hours);
		ascTable[3] = (TextView)findViewById(R.id.Editable_ASC_Mail_Hours);
		
		servTable[0] = (TextView)findViewById(R.id.Editable_Services_Library_Hours);
		servTable[1] = (TextView)findViewById(R.id.Editable_Services_Registrar_Hours);
		servTable[2] = (TextView)findViewById(R.id.Editable_Services_FinancialAid_Hours);
		servTable[3] = (TextView)findViewById(R.id.Editable_Services_Cashier_Hours);
		
		sagaTable[0] = (TextView)findViewById(R.id.Editable_SAGA_Breakfast_Hours);
		sagaTable[1] = (TextView)findViewById(R.id.Editable_SAGA_Lunch_Hours);
		sagaTable[2] = (TextView)findViewById(R.id.Editable_SAGA_Dinner_Hours);
		
		dayView = (TextView)findViewById(R.id.DayText);
		
		Calendar cal = Calendar.getInstance();
		int day = (cal.get(Calendar.DAY_OF_WEEK) - 2) % TOTAL_DAYS;
		
		dayView.setText("Displaying " + hoursData.DAYS_OF_WEEK[day] +
				" hours");
		
		updateLabelsToDay(day);
		
	} // end initActivityObjects
	
	/**
	 * Changes all of the hours labels in the layout
	 * to reflect the hours of a certain day.
	 * 
	 * @param day 0-6 to reflect the day to display
	 * @return true if operation is a success
	 * @author Devon Johnson
	 */
	private boolean updateLabelsToDay(int day) {
		if (day < 0 || day >= TOTAL_DAYS || hoursData == null)
			return false;
		for (int i = 0; i < ascTable.length; i++)
			ascTable[i].setText(hoursData.ASC_HOURS[i][day]);
		for (int i = 0; i < servTable.length; i++)
			servTable[i].setText(hoursData.SERV_HOURS[i][day]);
		for (int i = 0; i < sagaTable.length; i++)
			sagaTable[i].setText(hoursData.SAGA_HOURS[i][day]);
		return true;
	} // end updateLabelsToDay
	
	public void changeDay(View view) {
		Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_LONG).show();
	} // end changeDay
	
	private class HoursData {
		
		public final String[] ASC_MAIN_HOURS;
		public final String[] ASC_HIVE_HOURS;
		public final String[] ASC_COFFEE_HOURS;
		public final String[] ASC_MAIL_HOURS;

		public final String[] SERV_LIBRARY_HOURS;
		public final String[] SERV_REGISTRAR_HOURS;
		public final String[] SERV_FIN_AID_HOURS;
		public final String[] SERV_CASHIER_HOURS;
		
		public final String[] SAGA_BREAKFAST_HOURS;
		public final String[] SAGA_LUNCH_HOURS;
		public final String[] SAGA_DINNER_HOURS;
		
		public final String[][] ASC_HOURS;
		public final String[][] SERV_HOURS;
		public final String[][] SAGA_HOURS;
		
		public final String[] DAYS_OF_WEEK;
		
		public HoursData() {
			
			ASC_MAIN_HOURS = new String[] {
				getString(R.string.Hours_ASC_Main_Day1),
				getString(R.string.Hours_ASC_Main_Day2),
				getString(R.string.Hours_ASC_Main_Day3),
				getString(R.string.Hours_ASC_Main_Day4),
				getString(R.string.Hours_ASC_Main_Day5),
				getString(R.string.Hours_ASC_Main_Day6),
				getString(R.string.Hours_ASC_Main_Day7)
			};
			ASC_HIVE_HOURS = new String[] {
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day1),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day2),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day3),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day4),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day5),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day6),
				getString(R.string.Hours_ASC_Hive_and_Coffee_Day7)
			};
			ASC_COFFEE_HOURS = Arrays.copyOf(
					ASC_HIVE_HOURS, ASC_HIVE_HOURS.length);
			ASC_MAIL_HOURS = new String[] {
				getString(R.string.Hours_ASC_Mail_Day1),
				getString(R.string.Hours_ASC_Mail_Day2),
				getString(R.string.Hours_ASC_Mail_Day3),
				getString(R.string.Hours_ASC_Mail_Day4),
				getString(R.string.Hours_ASC_Mail_Day5),
				getString(R.string.Hours_ASC_Mail_Day6),
				getString(R.string.Hours_ASC_Mail_Day7)
			};
			
			SERV_LIBRARY_HOURS = new String[] {
				getString(R.string.Hours_Services_Library_Day1),
				getString(R.string.Hours_Services_Library_Day2),
				getString(R.string.Hours_Services_Library_Day3),
				getString(R.string.Hours_Services_Library_Day4),
				getString(R.string.Hours_Services_Library_Day5),
				getString(R.string.Hours_Services_Library_Day6),
				getString(R.string.Hours_Services_Library_Day7)
			};
			SERV_REGISTRAR_HOURS = new String[] {
				getString(R.string.Hours_Services_Registrar_Day1),
				getString(R.string.Hours_Services_Registrar_Day2),
				getString(R.string.Hours_Services_Registrar_Day3),
				getString(R.string.Hours_Services_Registrar_Day4),
				getString(R.string.Hours_Services_Registrar_Day5),
				getString(R.string.Hours_Services_Registrar_Day6),
				getString(R.string.Hours_Services_Registrar_Day7)
			};
			SERV_FIN_AID_HOURS = new String[] {
				getString(R.string.Hours_Services_FinancialAid_Day1),
				getString(R.string.Hours_Services_FinancialAid_Day2),
				getString(R.string.Hours_Services_FinancialAid_Day3),
				getString(R.string.Hours_Services_FinancialAid_Day4),
				getString(R.string.Hours_Services_FinancialAid_Day5),
				getString(R.string.Hours_Services_FinancialAid_Day6),
				getString(R.string.Hours_Services_FinancialAid_Day7)
			};
			SERV_CASHIER_HOURS = new String[] {
				getString(R.string.Hours_Services_Cashier_Day1),
				getString(R.string.Hours_Services_Cashier_Day2),
				getString(R.string.Hours_Services_Cashier_Day3),
				getString(R.string.Hours_Services_Cashier_Day4),
				getString(R.string.Hours_Services_Cashier_Day5),
				getString(R.string.Hours_Services_Cashier_Day6),
				getString(R.string.Hours_Services_Cashier_Day7)
			};
			
			SAGA_BREAKFAST_HOURS = new String[] {
				getString(R.string.Hours_SAGA_Breakfast_Day1),
				getString(R.string.Hours_SAGA_Breakfast_Day2),
				getString(R.string.Hours_SAGA_Breakfast_Day3),
				getString(R.string.Hours_SAGA_Breakfast_Day4),
				getString(R.string.Hours_SAGA_Breakfast_Day5),
				getString(R.string.Hours_SAGA_Breakfast_Day6),
				getString(R.string.Hours_SAGA_Breakfast_Day7)
			};
			SAGA_LUNCH_HOURS = new String[] {
				getString(R.string.Hours_SAGA_Lunch_Day1),
				getString(R.string.Hours_SAGA_Lunch_Day2),
				getString(R.string.Hours_SAGA_Lunch_Day3),
				getString(R.string.Hours_SAGA_Lunch_Day4),
				getString(R.string.Hours_SAGA_Lunch_Day5),
				getString(R.string.Hours_SAGA_Lunch_Day6),
				getString(R.string.Hours_SAGA_Lunch_Day7)
			};
			SAGA_DINNER_HOURS = new String[] {
				getString(R.string.Hours_SAGA_Dinner_Day1),
				getString(R.string.Hours_SAGA_Dinner_Day2),
				getString(R.string.Hours_SAGA_Dinner_Day3),
				getString(R.string.Hours_SAGA_Dinner_Day4),
				getString(R.string.Hours_SAGA_Dinner_Day5),
				getString(R.string.Hours_SAGA_Dinner_Day6),
				getString(R.string.Hours_SAGA_Dinner_Day7)
			};
			
			ASC_HOURS = new String[][] {
				ASC_MAIN_HOURS,
				ASC_HIVE_HOURS,
				ASC_COFFEE_HOURS,
				ASC_MAIL_HOURS
			};
			SERV_HOURS = new String[][] {
				SERV_LIBRARY_HOURS,
				SERV_REGISTRAR_HOURS,
				SERV_FIN_AID_HOURS,
				SERV_CASHIER_HOURS
			};
			SAGA_HOURS = new String[][] {
				SAGA_BREAKFAST_HOURS,
				SAGA_LUNCH_HOURS,
				SAGA_DINNER_HOURS
			};
			
			DAYS_OF_WEEK = new String[] {
				getString(R.string.Day1Title),
				getString(R.string.Day2Title),
				getString(R.string.Day3Title),
				getString(R.string.Day4Title),
				getString(R.string.Day5Title),
				getString(R.string.Day6Title),
				getString(R.string.Day7Title)
			};
			
		} // end constructor
		
	} // end HoursData inner class
	
} // end ScheduleHoursActivity class

