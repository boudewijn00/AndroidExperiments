package com.example.canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class CalendarEvents {

	String getEvents(String authToken, String beginDate, String endDate){
    	
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
	
}
