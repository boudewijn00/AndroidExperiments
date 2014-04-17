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

import com.example.canvas.views.WeekView;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canvas.models.AccountToken;
import com.example.canvas.models.CalendarEvents;

public class AccountActivity extends Activity {
	
	protected AccountManager am;
    protected Intent intent;
    private WeekView weekView;
    private String blocks;
    
    String TAG = "TGtracker";
    Activity activity;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
    	am = AccountManager.get(getApplicationContext());       
        activity = this;
        
        setContentView(R.layout.activity_account);
		weekView = (WeekView)findViewById(R.id.chart);
        
    }
    
    private class TokenTask extends AsyncTask<String,Void,String> {

		@Override
	    protected String doInBackground(String... url) {
	      
			String currentEvents = "";
			String previousEvents = "";
			String nextEvents = "";
			
			AccountToken accountToken = new AccountToken();
    		String authToken = accountToken.updateToken(am,true,activity);

    		CalendarEvents calendarEvents = new CalendarEvents();

			// get the begin and end dates of this week
			List weekBeginEndDates = calendarEvents.getWeekBeginEndDates();
			
			for (int i = 0; i < weekBeginEndDates.size(); i++) {
				
				List current = (List) weekBeginEndDates.get(i);
				
				// strip the begin date
				String beginDate = current.get(0).toString();
				
				// strip the end date
				String endDate = current.get(1).toString();	
				
				// get the events from google calendar
				String events = calendarEvents.getEvents(authToken,beginDate,endDate);
				
				if(i == 0){
					currentEvents = events;
				} else if(i == 1){
					previousEvents = events;
				} else if(i == 2){
					nextEvents = events;
				}	
				
			}
			
			blocks = calendarEvents.getBlocks(currentEvents,previousEvents,nextEvents);
			
			return null;
	      
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	    	Log.d("Events","post execute");
	    	weekView.setData(blocks);
	    	weekView.invalidate();
	    }
	    
    }
    
    public void onStart() {
		  super.onStart();
		  TokenTask task = new TokenTask();
		  task.execute();
    }


    

	
	
}
