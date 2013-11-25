package com.example.canvas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.canvas.CustomView;

public class MainActivity extends Activity {

	private CustomView myView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myView = (CustomView)findViewById(R.id.chart);
		
		Intent i = new Intent(Intent.ACTION_SEND); i.setData(Uri.parse("mailto:"));
		String[] to = { "someone1@example.com" , "someone2@example.com" }; String[] cc = { "someone3@example.com" , "someone4@example.com" }; i.putExtra(Intent.EXTRA_EMAIL, to);
		i.putExtra(Intent.EXTRA_CC, cc);
		i.putExtra(Intent.EXTRA_SUBJECT, "Subject here..."); i.putExtra(Intent.EXTRA_TEXT, "Message here..."); i.setType("message/rfc822");
		startActivity(Intent.createChooser(i, "Email"));
		
	}

}
