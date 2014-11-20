package com.swengr2_di_dj_ly.letournapp_v1_0;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginSettings extends Activity {

	private EditText username;
	private EditText password;
	
	public static final String USER_KEY = "username";
	public static final String PASS_KEY = "password";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_settings);

		username = (EditText) findViewById(R.id.username_textField);
		password = (EditText) findViewById(R.id.password_textField);
		String user = LetuSharedPreferences.loadLetuPreference(this,  USER_KEY,  null);
		String pass = LetuSharedPreferences.loadLetuPreference(this,  PASS_KEY, null);
		if(user != null) username.setText(encrypt(user));
		if(pass != null) password.setText(encrypt(pass));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_settings, menu);
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
	
	public void onSave(View view) {
	
		String user = username.getText().toString();
		String pass = password.getText().toString();
		
		// Encrypt
		user = encrypt(user);
		pass = encrypt(pass);
		
		LetuSharedPreferences.saveLetuPreference(this, USER_KEY, user);
		LetuSharedPreferences.saveLetuPreference(this, PASS_KEY, pass);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		TextView tv = new TextView(this);
		tv.setText("Success! Credentials Saved");
		if(user.length()==0 || pass.length()==0){
			tv.setText("Please enter both a username and password.");
		}
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
	
	}
	
	public void onClear(View view) {
		String defaultText = "";
		username.setText(defaultText);
		password.setText(defaultText);
		LetuSharedPreferences.saveLetuPreference(this, USER_KEY, defaultText);
		LetuSharedPreferences.saveLetuPreference(this, PASS_KEY, defaultText);
	}
	
	/**
	* This method is not secure. It is merely to hide credentials.
	* @param plaintext
	* 
	* @return the encrypted text
	*/
	public static String encrypt(String plaintext){
		String key = ",.nqwekfxzx";
		if(key.length() <= 0) return plaintext;
		String ciphertext = "";
		while(key.length() < plaintext.length()){
		key += key;
		}
		key = key.substring(0, plaintext.length());
		for(int i = 0; i < plaintext.length(); i++){
		ciphertext += (char)((int)plaintext.charAt(i)^(int)key.charAt(i));
		}
		return ciphertext;
	}
	
} // end LoginSettings class
