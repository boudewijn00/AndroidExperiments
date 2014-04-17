package com.example.canvas;

import java.io.*;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class StorageActivity extends Activity {

	private MediaPlayer mPlayer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage);
		
		writeFileToInternalStorage("test");
		readFileFromInternalStorage("audio.m4a");
		
        File[] list = getFilesDir().listFiles();
        
        for (int i=0; i < list.length; i++){
            Log.d("Events", "FileName:" + list[i].getName());
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
