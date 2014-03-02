package com.example.canvas;

import java.io.*;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//writeFileToInternalStorage("test");
		//readFileFromInternalStorage("audio.m4a");
		
        File[] list = getFilesDir().listFiles();
        
        for (int i=0; i < list.length; i++)
        {
            Log.d("Events", "FileName:" + list[i].getName());
        }
        
		Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent("com.example.canvas.WeekActivity"); 
				startActivity(i);
				
			}
		});
        
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent("com.example.canvas.VoiceActivity"); 
				startActivity(i);
				
			}
		});
        
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent("com.example.canvas.PostActivity"); 
				startActivity(i);
				
			}
		});
        
        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent("com.example.canvas.AccountActivity"); 
				startActivity(i);
				
			}
		});
		
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
