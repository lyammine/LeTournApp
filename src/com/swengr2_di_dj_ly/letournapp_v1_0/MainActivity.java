package com.swengr2_di_dj_ly.letournapp_v1_0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * I haven't done very much commenting and authorship
 * in this class because it is only for debugging purposes
 * 
 * @author Devon Johnson
 *
 */
public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityObjects();
        
    } // end onCreate
    
    private void initActivityObjects() {
    	
    } // end initActivityObjects

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void chapAttButtonPressed(View view) {
    	openNewWindow(ChapAttActivity.class);
    } // end chapAttButtonPressed
    
    public void announcButtonPressed(View view) {
    	openNewWindow(AnnouncementsActivity.class);
    } // end announcButtonPressed
    
    public void hoursButtonPressed(View view) {
    	openNewWindow(ScheduleHoursActivity.class);
    }
    
    public void loginSettingsButtonPressed(View view){
    	openNewWindow(LoginSettings.class);
    }
    
    private void openNewWindow(Class<?> cls) {
    	Intent intent = new Intent(this, cls);
    	startActivity(intent);
    } // end openNewWindow
    
} // end MainActivity class
