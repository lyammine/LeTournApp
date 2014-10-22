package com.swengr2_di_dj_ly.letournapp_v1_0;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

import org.apache.http.HttpException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class SecureLetuNetworkThread extends LetuNetworkThread {
	
	protected String username;
	protected String password;
	
	private static final String CERT_STRING = "CN=COMODO SSL CA, " +
			"O=COMODO CA Limited, L=Salford, ST=Greater Manchester, " +
			"C=GB";
	private static final X500Principal LETU_PRINC = new X500Principal(CERT_STRING);
	
	protected static final TrustManager[] LETU_TRUST =
		new TrustManager[] {
			new X509TrustManager() {	
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					Log.d(TAG, "getAcceptedIssuers called");
					return new X509Certificate[] { };
				} // end getAcceptedIssuers
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					
					if (chain.length != 1)
						throw new CertificateException("Should " +
								"have only one certificate");
					
					if (!chain[0].getIssuerX500Principal().equals(LETU_PRINC))
						throw new CertificateException("Issuer " +
								"is not the expected CA");
					
				} // end checkServerTrusted
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					throw new CertificateException("This class should not have clients");
				} // end checkClientTrusted
				
			} // end new X509TrustManager
			
		};
	
	public static final String TAG = "NetworkDebug";
	
	/**
	 * Creates an instance of SecureLetuNetworkThread given
	 * a url string, context, username and password
	 * 
	 * @author Devon Johnson
	 * @param urlString requested URL
	 * @param context the application's context
	 * @param username Username credential
	 * @param password Password credential
	 */
	public SecureLetuNetworkThread(String urlString, Context context,
			String username, String password) {
		super(urlString, context);
		this.username = username;
		this.password = password;
	} // end constructor
	
	/**
	 * Securely retrieves webpage data from the specified
	 * URL. Outputs results by sending data to listener
	 * method onWebpageDataObtained or onExceptionRetrievingNetworkData
	 * 
	 * @author Devon Johnson
	 */
	@SuppressLint("TrulyRandom") @Override
	protected void execute() {
		
		try {
			String userPass = username + ":" + password;
			String encoding = Base64.encodeToString(
					userPass.getBytes(), Base64.NO_WRAP);
			
			SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, LETU_TRUST, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
			
			URL url = new URL(urlString);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//			con.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
			con.setRequestProperty("Authorization", "Basic " + encoding);
			con.connect();
			
			if (con.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED)
				throw new HttpException("Credentials not accepted");
			
			if (listener != null)
				listener.onWebPageReaderObtained(new BufferedReader(
						new InputStreamReader(con.getInputStream())));
			
		} catch (Exception ex) {
			if (listener != null)
				listener.onExceptionRetrievingNetworkData(ex);
		} // end try-catch

	} // end execute

} // end SecureLetuNetworkThread class
