package com.swengr2_di_dj_ly.letournapp_v1_0;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class contains only two static
 * methods: load a preference and save
 * a preference. This class should never
 * be instantiated or extended
 * 
 * @author Devon Johnson
 *
 */
public final class LetuSharedPreferences {
	
	public static final String SHARED_PREFS_FILE = "LetuSharedPrefs";
	
	/**
	 * Saves a preference to the LETU Shared
	 * Preferences file given a key-value pair
	 * 
	 * @param context	The application context
	 * @param key		Key of key-value pair
	 * @param value		Value of key-value pair
	 * @author Devon Johnson
	 */
	public static void saveLetuPreference(Context context, 
			String key, String value) {
		
		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREFS_FILE, Context.MODE_PRIVATE);				// Open preferences file
		SharedPreferences.Editor editor = settings.edit();				// Open editor
		editor.putString(key, value);									// Save key-value pair
		editor.commit();												// Permanent save!
		
	} // end saveSharedPreferences
	
	/**
	 * Loads a preference from the LETU 
	 * Shared Preference file given a key
	 * 
	 * @param context	The application context
	 * @param key		Key of key-value pair
	 * @param defValue	What to return if this preferences doesn't exist
	 * @return			Value found from the key
	 * @author Devon Johnson
	 */
	public static String loadLetuPreference(Context context,
			String key, String defValue) {
		
		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREFS_FILE, Context.MODE_PRIVATE);				// Open preferences file
		return settings.getString(key, defValue);						// Return the found value
		
	} // end loadLetuPreference
	
} // end LetuSharedPreferences class
