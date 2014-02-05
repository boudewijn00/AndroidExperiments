package com.example.canvas;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;


public class PostActivity extends Activity {
	
	private static String mFileName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.mp4";
		
		File file = new File(mFileName);
		
		if (file.isFile()) {
			
			Log.d("Events","its a file");
			
			new Thread(new Runnable() {
                public void run() {                 
                     uploadFile(mFileName);                        
                }
              }).start();
			
		} else {
			Log.d("Events","its not a file");
		}
		
			
	}
	
	public void uploadFile(String mFileName){
		  
        HttpURLConnection conn = null;
        DataOutputStream dos = null; 
        
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        
        String upLoadServerUri = "http://theridiid.net/index/upload";
        
        File sourceFile = new File(mFileName); 
		
        try {
        
        // open a URL connection to the Servlet
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        URL url = new URL(upLoadServerUri);
         
        // Open a HTTP  connection to  the URL
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true); // Allow Inputs
        conn.setDoOutput(true); // Allow Outputs
        conn.setUseCaches(false); // Don't use a Cached Copy
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        conn.setRequestProperty("uploaded_file", mFileName);
        
        dos = new DataOutputStream(conn.getOutputStream());
        
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                  + mFileName + "\"" + lineEnd);
         
        dos.writeBytes(lineEnd);

        // create a buffer of  maximum size
        bytesAvailable = fileInputStream.available();

        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // read file and write it into form...
        bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
           
        while (bytesRead > 0) {
             
          dos.write(buffer, 0, bufferSize);
          bytesAvailable = fileInputStream.available();
          bufferSize = Math.min(bytesAvailable, maxBufferSize);
          bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
           
         }

        // send multipart form data necesssary after file data...
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        // Responses from the server (code and message)
        Integer serverResponseCode = conn.getResponseCode();
        String serverResponseMessage = conn.getResponseMessage();  
         
        //close the streams //
        fileInputStream.close();
        dos.flush();
        dos.close();
        
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        }
        
	}
	
}
