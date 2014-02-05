package com.example.canvas;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.canvas.CustomView;
import com.example.canvas.R;

public class WeekActivity extends Activity {

	private CustomView myView;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);
		
		myView = (CustomView)findViewById(R.id.chart);
		
		
	}
	
	private class DownloadWebPageTask extends AsyncTask<String,Void,String> {

		@Override
	    protected String doInBackground(String... url) {
	      
			String response = "";
	        
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
	    	myView.setData(result);
	    	myView.invalidate();
	    	final TextView textViewToChange = (TextView) findViewById(R.id.weekDates);
			textViewToChange.setText("test");
	    }
	    
	  }

	  public void onStart() {
		  super.onStart();
		  DownloadWebPageTask task = new DownloadWebPageTask();
		  task.execute();

	  }

	
		
	
	
	
	
	

}
