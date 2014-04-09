package com.example.canvas;

import java.io.*;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.media.MediaPlayer;

import com.example.canvas.CustomView;
import com.example.canvas.R;

public class MainActivity extends Activity {
	
	private MediaPlayer mPlayer = null;
	
	private ListView listView;
	private ArrayList< String>arrayList; // list of the strings that should appear in ListView
	private ArrayAdapter arrayAdapter; // a middle man to bind ListView and array list 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.listview);
		
		arrayList = new ArrayList();
        arrayList.add("Receive");
        arrayList.add("Account");
        arrayList.add("Post");
        arrayList.add("Voice");
        arrayList.add("ListView");
        
        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_single_choice, arrayList);
        listView.setAdapter(arrayAdapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				final String item = (String) parent.getItemAtPosition(position);
				Log.d("Events",item);
				
				startActivity(new Intent("com.example.canvas."+item+"Activity")); 
				
			}
			
		});
		
		//writeFileToInternalStorage("test");
		//readFileFromInternalStorage("audio.m4a");
		
        File[] list = getFilesDir().listFiles();
        
        for (int i=0; i < list.length; i++){
            //Log.d("Events", "FileName:" + list[i].getName());
        }
        
		
		
	}
	
	public void writeFileToInternalStorage(String fileName) {
		  
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));
		    writer.write("This is a test1." + eol);
	    	writer.write("This is a test2." + eol);
		} catch (Exception e) {
			  e.printStackTrace();
		} finally {
		    
			if (writer != null) {
				  try {
					  writer.close();
				  } catch (IOException e) {
					  e.printStackTrace();
				  }
			  }
			  
		  }
		
	}
	
	public void readFileFromInternalStorage(String fileName) {
		  
		String eol = System.getProperty("line.separator");
		BufferedReader input = null;
		
		try {
		    
			input = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
		    String line;
		    StringBuffer buffer = new StringBuffer();
		    
		    while ((line = input.readLine()) != null) {
		    	buffer.append(line + eol);
		    	Log.d("Events",line);
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		  
			if (input != null) {
		    
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		    }
		}
	}
}
