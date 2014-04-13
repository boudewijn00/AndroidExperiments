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
import android.widget.Toast;

public class SensorsActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;  
	private boolean color = false;
	private View view;
	private long lastUpdate;

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
	    
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			getValues(event);
	    }

	}

	private void getValues(SensorEvent event) {
	    
		float[] values = event.values;
	    
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
	    		sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	    		SensorManager.SENSOR_DELAY_NORMAL);
  	}

	@Override
	protected void onPause() {
		// unregister listener
	    super.onPause();
	    sensorManager.unregisterListener(this);
	}
} 

