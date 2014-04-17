package com.example.canvas;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;  
	private boolean color = false;
	private View view;
	private long lastUpdate;
	
	TextView text1 = null;
	TextView text2 = null;
	TextView text3 = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_sensors);

	    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    lastUpdate = System.currentTimeMillis();
	    
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	    
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			getValues(event);
	    }

	}

	private void getValues(SensorEvent event) {
		
		float[] values = event.values;
		
		//float x = values[0];
	    //float y = values[1];
	    //float z = values[2];
	    
		text1 = (TextView)findViewById(R.id.text1);
		text2 = (TextView)findViewById(R.id.text2);
		text3 = (TextView)findViewById(R.id.text3);
		
		text1.setText(Float.toString(values[0]));
		text2.setText(Float.toString(values[1]));
		text3.setText(Float.toString(values[2]));
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected void onResume() {
	    
		super.onResume();
	    // register this class as a listener for the orientation and
	    // accelerometer sensors
	    sensorManager.registerListener(this,
	    		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	    		SensorManager.SENSOR_DELAY_NORMAL);
  	}

	@Override
	protected void onPause() {
		// unregister listener
	    super.onPause();
	    sensorManager.unregisterListener(this);
	}
} 

