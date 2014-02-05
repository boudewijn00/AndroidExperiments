package com.example.canvas;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AccountActivity extends Activity {
	
	protected AccountManager am;
    protected Intent intent;
    String TAG = "TGtracker";
    Activity activity;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = AccountManager.get(getApplicationContext());       
        activity = this;
    }
    
    private class TokenTask extends AsyncTask<String,Void,String> {

		@Override
	    protected String doInBackground(String... url) {
	      
			String response = "";
			String authToken = "null";
			
			String currentEvents = "";
			String previousEvents = "";
			String nextEvents = "";
			
			authToken = updateToken(am,true);
			
			Log.d("Events", "got token, yipee: "+authToken);
			
			//String events = getEvents(authToken);
			//String post = postEvents(events);
			List weekBeginEndDates = getWeekBeginEndDates();
			
			for (int i = 0; i < weekBeginEndDates.size(); i++) {
				
				List current = (List) weekBeginEndDates.get(i);
				
				String beginDate = current.get(0).toString();
				String endDate = current.get(1).toString();
				
				Log.d("Events","begin date: "+beginDate);
				Log.d("Events","end date: "+endDate);
				
				String events = getEvents(authToken,beginDate,endDate);
				
				String week = "";
				
				if(i == 0){
					currentEvents = events;
				} else if(i == 1){
					previousEvents = events;
				} else if(i == 2){
					nextEvents = events;
				}	
				
			}
			
			postEvents(currentEvents,previousEvents,nextEvents);
			
			Log.d("Events","after iterator");
			
			return response;
	      
	    }
		
		private String updateToken(AccountManager am, boolean invalidateToken) {
			
			String response = "";
			String authToken = "null";
			
			String scopes = "oauth2: https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar";
			
			Account[] accounts = am.getAccountsByType("com.google");
			AccountManagerFuture<Bundle> accountManagerFuture;
			accountManagerFuture = am.getAuthToken(accounts[0], scopes, null, activity, null, null);
				
			String sAccountManagerFuture = String.valueOf(accountManagerFuture);
			Log.d("Events",sAccountManagerFuture);
				
			Bundle authTokenBundle = null;
			try {
				authTokenBundle = accountManagerFuture.getResult();
			} catch (OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
		    Log.d("Events", "newToken preinvalidate: "+authToken);
			
		    if(invalidateToken) {
                am.invalidateAuthToken("com.google", authToken);
                authToken = updateToken(am,false);
            }
			
			return authToken;
			
		}

	    @Override
	    protected void onPostExecute(String result) {
	    	
	    }
	    
	    private String getEvents(String authToken, String beginDate, String endDate){
	    	
	    	String response = "";
	    	String events = "";
	    	String calendarId = "m7ltv01i3hl6lja9bh3ublkd64@group.calendar.google.com";
	    	    	
	    	URL uri = null;
			try {
				final String encodedBeginDate = URLEncoder.encode(beginDate, "UTF-8");
				final String encodedEndDate = URLEncoder.encode(endDate, "UTF-8");
				uri = new URL("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events?timeMax="+encodedEndDate+"&timeMin="+encodedBeginDate+"&orderBy=startTime&singleEvents=true");
				Log.d("Events",uri.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    HttpURLConnection con = null;
			try {
				con = (HttpURLConnection) uri.openConnection();
				con.addRequestProperty("client_id", "1094621519767-lr864ii0dqi3ogqbvm3tstcptuu0hn9u.apps.googleusercontent.com");
				con.setRequestProperty("Authorization", "OAuth " + authToken);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    int serverCode = 0;
			try {
				serverCode = con.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String sServerCode = String.valueOf(serverCode);
			Log.d("Events",sServerCode);
			
		    //successful query
		    if (serverCode == 200) {
		        InputStream is;
				try {
					
					is = con.getInputStream();
					Log.d("Events","input stream available");
					
					BufferedReader r = new BufferedReader(new InputStreamReader(is));
					StringBuilder total = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
					    total.append(line);
					}
					
					events = total.toString();
					
					Log.d("Events",total.toString());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    //bad token, invalidate and get a new one
		    } else if (serverCode == 401) {
		        return response;
		    //unknown error, do something else
		    } else {
		        return response;
		    }
	    	
	    	return events;
	    	
	    }
	    
	    private String postEvents(String currentEvents, String previousEvents, String nextEvents){

	    	//Log.d("Events",previousEvents);
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
	    	
	    	// specify the URL you want to post to
	    	HttpPost httppost = new HttpPost("http://thenewfamily.nl/calendar/days");
	    	try {
	    	
	    		// create a list to store HTTP variables and their values
	    	List nameValuePairs = new ArrayList();
	    	
	    	// add an HTTP variable and value pair
	    	nameValuePairs.add(new BasicNameValuePair("current", currentEvents));
	    	nameValuePairs.add(new BasicNameValuePair("previous", previousEvents));
	    	nameValuePairs.add(new BasicNameValuePair("next", nextEvents));
	    	
	    	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	
	    	// send the variable and value, in other words post, to the URL
	    	HttpResponse response = httpclient.execute(httppost);
	    	
	    	InputStream content = response.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			
			while ((line = buffer.readLine()) != null)
		    {
		        sb.append(line);
		    }
			
			String output = sb.toString();
	    	
			Log.d("Events",output);
			
	    	} catch (ClientProtocolException e) {
	    	// process execption
	    	} catch (IOException e) {
	    	// process execption
	    	}
	    	
	    	return "";
	    	
	    }
	    
	    private List getWeekBeginEndDates(){
	    	
	    	List weekBeginEndDates = new ArrayList();
	    	
	    	Calendar calendar = Calendar.getInstance();
	    	int week = calendar.get(Calendar.WEEK_OF_YEAR);
	    	
	    	// get current begin end date
	    	List currentBeginEndDates = getWeekBeginEndDate(week);
	    	
	    	List currentWeekDates = new ArrayList();
	    	currentWeekDates.add(currentBeginEndDates.get(0).toString());
	    	currentWeekDates.add(currentBeginEndDates.get(1).toString());
	    	
	    	// add current week dates to list
	    	weekBeginEndDates.add(currentWeekDates);
	    	
	    	// get previous begin end date
	    	List previousBeginEndDates = getWeekBeginEndDate(week-1);
	    	
	    	List previousWeekDates = new ArrayList();
	    	previousWeekDates.add(previousBeginEndDates.get(0).toString());
	    	previousWeekDates.add(previousBeginEndDates.get(1).toString());
	    	
	    	// add previous week dates to list
	    	weekBeginEndDates.add(previousWeekDates);
	    	
	    	// get next begin end date
	    	List nextBeginEndDates = getWeekBeginEndDate(week+1);
	    	
	    	List nextWeekDates = new ArrayList();
	    	nextWeekDates.add(nextBeginEndDates.get(0).toString());
	    	nextWeekDates.add(nextBeginEndDates.get(1).toString());
	    	  
	    	// add next week dates to list
	    	weekBeginEndDates.add(nextWeekDates);
	    	
	    	return weekBeginEndDates; 
	    	
	    }
	    
	    private List getWeekBeginEndDate(int week){
	    	
	    	String sWeek = String.valueOf(week);
	    	Log.d("Events",sWeek);
	    	
	    	int year = 2014;
	    	List weekBeginEndDate = new ArrayList();
	    	
	    	Calendar calendar = Calendar.getInstance();
	        calendar.clear();
	        calendar.set(Calendar.WEEK_OF_YEAR, week);
	        calendar.set(Calendar.YEAR, year);
	    	
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
	        String beginDate = formatter.format(calendar.getTime());
	        
	        calendar.add(Calendar.DATE, 6);
	        String endDate = formatter.format(calendar.getTime());
	    	
	    	weekBeginEndDate.add(beginDate);
	    	weekBeginEndDate.add(endDate);
	    	
	    	return weekBeginEndDate;
	    	
	    	
	    }
	    
    }
    
    public void onStart() {
		  super.onStart();
		  TokenTask task = new TokenTask();
		  task.execute();
    }


    

	
	
}
