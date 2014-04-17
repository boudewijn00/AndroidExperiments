package com.example.canvas.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class CalendarEvents {

	public String getEvents(String authToken, String beginDate, String endDate){
    	
    	String response = "";
    	String events = "";
    	String calendarId = "m7ltv01i3hl6lja9bh3ublkd64@group.calendar.google.com";
    	    	
    	URL uri = null;
		try {
			final String encodedBeginDate = URLEncoder.encode(beginDate, "UTF-8");
			final String encodedEndDate = URLEncoder.encode(endDate, "UTF-8");
			uri = new URL("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events?timeMax="+encodedEndDate+"&timeMin="+encodedBeginDate+"&orderBy=startTime&singleEvents=true");
			//Log.d("Events",uri.toString());
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
		
	    //successful query
	    if (serverCode == 200) {
	        InputStream is;
			try {
				
				is = con.getInputStream();
				
				BufferedReader r = new BufferedReader(new InputStreamReader(is));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
				    total.append(line);
				}
				
				events = total.toString();
				
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
	
	/**
     * send the current, previous and next events to the new family sever, which makes blocks out of the events
     * @param currentEvents
     * @param previousEvents
     * @param nextEvents
     * @return
     */
    public String getBlocks(String currentEvents, String previousEvents, String nextEvents){
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	
    	String blocks = "";
    	
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
			
			// grab the output, and put in a string
			blocks = sb.toString();
		
    	} catch (ClientProtocolException e) {
    	// process execption
    	} catch (IOException e) {
    	// process execption
    	}
    	
		return blocks;
    	
    }
    
    public List getWeekBeginEndDates(){
    	
    	List weekBeginEndDates = new ArrayList();
    	
    	Calendar calendar = Calendar.getInstance();
    	int week = calendar.get(Calendar.WEEK_OF_YEAR);
    	
    	// get current begin end date
    	List currentBeginEndDates = this.getWeekBeginEndDate(week);
    	
    	List currentWeekDates = new ArrayList();
    	currentWeekDates.add(currentBeginEndDates.get(0).toString());
    	currentWeekDates.add(currentBeginEndDates.get(1).toString());
    	
    	// add current week dates to list
    	weekBeginEndDates.add(currentWeekDates);
    	
    	// get previous begin end date
    	List previousBeginEndDates = this.getWeekBeginEndDate(week-1);
    	
    	List previousWeekDates = new ArrayList();
    	previousWeekDates.add(previousBeginEndDates.get(0).toString());
    	previousWeekDates.add(previousBeginEndDates.get(1).toString());
    	
    	// add previous week dates to list
    	weekBeginEndDates.add(previousWeekDates);
    	
    	// get next begin end date
    	List nextBeginEndDates = this.getWeekBeginEndDate(week+1);
    	
    	List nextWeekDates = new ArrayList();
    	nextWeekDates.add(nextBeginEndDates.get(0).toString());
    	nextWeekDates.add(nextBeginEndDates.get(1).toString());
    	  
    	// add next week dates to list
    	weekBeginEndDates.add(nextWeekDates);
    	
    	return weekBeginEndDates; 
    	
    }
    
    public List getWeekBeginEndDate(int week){
    	
    	String sWeek = String.valueOf(week);
    	
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
