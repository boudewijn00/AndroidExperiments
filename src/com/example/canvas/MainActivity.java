package com.example.canvas;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends Activity {
	
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
        arrayList.add("Sensors");
        
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
		
	}
	
	
}
