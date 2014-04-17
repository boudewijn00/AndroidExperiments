package com.example.canvas;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.canvas.R;
import com.example.canvas.views.CustomView;

public class ReceiveActivity extends Activity {

	private TextView textView;
	private String response;
	private JSONArray usersArray;
	private ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				final String item = (String) parent.getItemAtPosition(position);
				
				startActivity(new Intent("com.example.canvas.VoiceActivity"));
				
			}
			
		});
		
	}
	
	private class DownloadWebPageTask extends AsyncTask<String,Void,String> {

		@Override
	    protected String doInBackground(String... url) {
	        
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://www.hellodata.nl/get.php");
        
			try {
          
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				StringBuilder sb = new StringBuilder();
				
				String line = null;
				
				while ((line = buffer.readLine()) != null)
			    {
			        sb.append(line);
			    }
				
				response = sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return response;
	      
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	try {
				
				JSONObject jsonObject = new JSONObject(result); 
				usersArray = jsonObject.getJSONArray("users");				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	final ArrayList<String> list = new ArrayList<String>();
	    	
	    	for (int i=0; i < usersArray.length(); i++)
			{
		    	
				JSONObject person;
				try {
					person = usersArray.getJSONObject(i);
					String name = person.getString("name");
					list.add(name);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	    	
		    final ArrayAdapter adapter = new ArrayAdapter(ReceiveActivity.this,
		        android.R.layout.simple_list_item_1, list);
		    listview.setAdapter(adapter);
		    
		    
	    	
	    }
	    
	  }

	  public void onStart() {
		  super.onStart();
		  DownloadWebPageTask task = new DownloadWebPageTask();
		  task.execute();
	  }

}
