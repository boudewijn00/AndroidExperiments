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

public class ListViewActivity extends Activity {

	ListView list;
	TextView ver;
	TextView name;
	TextView api;
  	Button Btngetdata;
  	Activity activity;
  	
  	private JSONArray items;
  	private JSONArray start;
  	private JSONArray end;
  	
  	protected AccountManager am;
  	
  	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	  
	  	//JSON Node Names	
	    
	  	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	  		
	  		super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_listview);
	        
	        oslist = new ArrayList<HashMap<String, String>>();
	        Btngetdata = (Button)findViewById(R.id.getdata);
	        Btngetdata.setOnClickListener(new View.OnClickListener() {
	      
	        	@Override
	        	public void onClick(View view) {
	        		new JSONParse().execute();
	        	}
	        });
	        
	    }
	  	
	    private class JSONParse extends AsyncTask<String, String, String> {
	    	
	    	private ProgressDialog pDialog;
	      
	    	@Override
	        protected void onPreExecute() {
	            
	    		super.onPreExecute();
	    		
	    		//ver = (TextView)findViewById(R.id.vers);
	    		name = (TextView)findViewById(R.id.name);
	    		//api = (TextView)findViewById(R.id.api);
	            
	    		pDialog = new ProgressDialog(ListViewActivity.this);
	            pDialog.setMessage("Getting Data ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	            
	    	}
	    	
	    	@Override
	        protected String doInBackground(String... args) {
	    		
	    		//URL to get JSON Array
	    	  	/*String url = "http://hellodata.nl/get.php";
	    		
	    		JSONParser jParser = new JSONParser();
	        
	    		// Getting JSON from URL
	    		JSONObject json = jParser.getJSONFromUrl(url);
	    		return json;*/
	    		
	    		am = AccountManager.get(getApplicationContext());
	    		
	    		AccountToken accountToken = new AccountToken();
	    		
	    		String authToken = accountToken.updateToken(am,true,activity);
	    		
	    		String beginDate = "2014-04-07T00:00:00+0200";
	    		String endDate = "2014-04-13T00:00:00+0200";
	    		
	    		String events = getEvents(authToken,beginDate,endDate);
	    		
	    		return events;
	        
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
				//Log.d("Events",sServerCode);
				
			    //successful query
			    if (serverCode == 200) {
			        InputStream is;
					try {
						
						is = con.getInputStream();
						//Log.d("Events","input stream available");
						
						BufferedReader r = new BufferedReader(new InputStreamReader(is));
						StringBuilder total = new StringBuilder();
						String line;
						while ((line = r.readLine()) != null) {
						    total.append(line);
						}
						
						events = total.toString();
						
						//Log.d("Events",total.toString());
						
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
