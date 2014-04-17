package com.example.canvas.views;

import java.lang.reflect.Array;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.view.InputEvent;

public class CustomView extends View {

	private Paint circlePaint;
	private Paint textPaint;
	private JSONArray usersArray;
	private Float scale;
	private Typeface customFont;
	
	public CustomView(Context context, AttributeSet attrs){
		
		super(context, attrs);
		circlePaint = new Paint();
		textPaint = new Paint();
		
		customFont = Typeface.createFromAsset(getContext().getAssets(), "MovLette.ttf");
		
		Resources resources = context.getResources();
		scale = resources.getDisplayMetrics().density;
		
		String sScale = String.valueOf(scale);
		Log.d("Events","scale: "+sScale);
		
	}
	
	public void setData(String data){
		
		Log.d("Events", data);
		
		try {
			
			JSONObject jsonObject = new JSONObject(data); 
			usersArray = jsonObject.getJSONArray("users");
			
			String sLength = String.valueOf(usersArray.length());
			Log.d("Events",sLength);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
		Log.d("Events","on draw executed");		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float xPosition = event.getX();
        float yPosition = event.getY();
        
        String sPosition = String.valueOf(xPosition);;
        Log.d("Events",sPosition);
        
        return true;
        
	}

	
}
