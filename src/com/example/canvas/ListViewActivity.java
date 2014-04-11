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
import java.util.ArrayList;
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

public class ListViewActivity extends Activity {

	ListView list;
	TextView ver;
	TextView name;
	TextView api;
  	Button Btngetdata;
  	Activity activity;
  	
  	private JSONArray items;
  	protected AccountManager am;
  	
  	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	  
	  	//JSON Node Names
	  	private static final String TAG_OS = "android";
	  	private static final String TAG_VER = "ver";
	  	private static final String TAG_NAME = "name";
	  	private static final String TAG_API = "api";
	  	private static final String TAG_AGE = "age";	
	    
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
	    		
	    		String authToken = updateToken(am,true);
	    		
	    		String beginDate = "2014-04-07T00:00:00+0200";
	    		String endDate = "2014-04-13T00:00:00+0200";
	    		
	    		String events = getEvents(authToken,beginDate,endDate);
	    		
	    		return events;
	        
	    	}
	    	
	    	private String updateToken(AccountManager am, boolean invalidateToken) {
				
				String response = "";
				String authToken = "null";
				
				String scopes = "oauth2: https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar";
				
				Account[] accounts = am.getAccountsByType("com.google");
				AccountManagerFuture<Bundle> accountManagerFuture;
				accountManagerFuture = am.getAuthToken(accounts[0], scopes, null, activity, null, null);
					
				String sAccountManagerFuture = String.valueOf(accountManagerFuture);
					
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
				
			    if(invalidateToken) {
	                am.invalidateAuthToken("com.google", authToken);
	                authToken = updateToken(am,false);
	            }
				
				return authToken;
				
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
	    				//String name = c.getString(TAG_NAME);
	    				//String age = c.getString(TAG_AGE);
	    				
	    				// Adding value HashMap key => value
	    				HashMap<String, String> map = new HashMap<String, String>();
	    				//map.put(TAG_VER, ver);
	    				map.put(TAG_NAME, summary);
	    				//map.put(TAG_AGE, age);
	    				//map.put("summary",summary);
	    				
	    				oslist.add(map);
	    				list=(ListView)findViewById(R.id.list);
	    				ListAdapter adapter = new SimpleAdapter(ListViewActivity.this, oslist,
	                
						R.layout.listitem_row,
	                
						new String[] { 
	    						//TAG_VER,
	    						TAG_NAME, 
	    						//TAG_AGE
	    						//summary
	    						}, new int[] { 
	    						//R.id.vers,
	    						R.id.name, 
	    						//R.id.age
	    						//R.id.summary
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
	        }
	       }
	    }
	
}
