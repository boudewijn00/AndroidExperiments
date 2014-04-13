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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canvas.JSONParser;
import com.example.canvas.AccountToken;
import com.example.canvas.CalendarEvents;

public class ListViewActivity extends Activity {

	ListView list;
  	Activity activity;
  	
  	private JSONArray items;
  	private JSONArray start;
  	private JSONArray end;
  	
  	protected AccountManager am;
  	
  	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	  	    
	  	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	  		
	  		super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_listview);
	        
	        oslist = new ArrayList<HashMap<String, String>>();
	        	      
	        new JSONParse().execute();
	        
	    }
	  	
	    private class JSONParse extends AsyncTask<String, String, String> {
	    	
	    	private ProgressDialog pDialog;
	      
	    	@Override
	        protected void onPreExecute() {
	            
	    		super.onPreExecute();
	            
	    		pDialog = new ProgressDialog(ListViewActivity.this);
	            pDialog.setMessage("Getting Data ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	            
	    	}
	    	
	    	@Override
	        protected String doInBackground(String... args) {
	    			    		
	    		am = AccountManager.get(getApplicationContext());
	    		
	    		AccountToken accountToken = new AccountToken();
	    		
	    		String authToken = accountToken.updateToken(am,true,activity);
	    		
	    		String beginDate = "2014-04-07T00:00:00+0200";
	    		String endDate = "2014-04-13T00:00:00+0200";
	    		
	    		CalendarEvents calendarEvents = new CalendarEvents();
	    		
	    		String events = calendarEvents.getEvents(authToken,beginDate,endDate);
	    		
	    		return events;
	        
	    	}
	    		    	
	    	
	    	
	    	protected void onPostExecute(String events) {
	    		
	    		pDialog.dismiss();	    		
	    		
	    		try {
	    			
	    			JSONObject json = new JSONObject(events); 
	    			
	    			// Getting JSON Array from URL
	    			items = json.getJSONArray("items");
	    			
	    			String itemsLength = String.valueOf(items.length());
	    			
	    			for(int i = 0; i < items.length(); i++){
	    				
	    				JSONObject c = items.getJSONObject(i);
	    				// Storing  JSON item in a Variable
	    				
	    				String summary = c.getString("summary");
	    				
	    				SimpleDateFormat dateInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
	    				SimpleDateFormat dateOutput = new SimpleDateFormat("yyyy-MM-dd");
	    				
	    				JSONObject start = c.getJSONObject("start");
	    				String startDateTime = start.getString("dateTime");
	    				String startDate = dateOutput.format(dateInput.parse(startDateTime));
	    				
	    				JSONObject end = c.getJSONObject("end");
	    				String endDateTime = start.getString("dateTime");
	    				String endDate = dateOutput.format(dateInput.parse(endDateTime));
	    				
	    				// Adding value HashMap key => value
	    				HashMap<String, String> map = new HashMap<String, String>();

	    				map.put("summary", summary);
						map.put("start", startDate);
	    				map.put("end", endDate);
	    				
	    				oslist.add(map);
	    				list=(ListView)findViewById(R.id.list);
	    				ListAdapter adapter = new SimpleAdapter(ListViewActivity.this, oslist,
	                
						R.layout.listitem_row,
	                
						new String[] { 
	    						"summary", 
	    						"start",
	    						"end"
	    						}, new int[] { 
	    						R.id.summary, 
	    						R.id.start,
	    						R.id.end
	    						});
	            
	    				list.setAdapter(adapter);
	    				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	                    
	    				@Override
	                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    					Toast.makeText(ListViewActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
	                    }
	    				
	                });
	            }
	        } catch (JSONException e) {
	        	e.printStackTrace();
	        } catch (java.text.ParseException e) {
				e.printStackTrace();
			}
       }
    }
	
}
