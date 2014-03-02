package com.example.canvas;

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

public class WeekView extends View {

	private Paint circlePaint;
	private Paint textPaint;
	private JSONArray blocksArray;
	private Float scale;
	private Typeface customFont;
	
	public WeekView(Context context, AttributeSet attrs){
		
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
			
			blocksArray = jsonObject.getJSONArray("blocks");
			
			String sLength = String.valueOf(blocksArray.length());
			Log.d("Events",sLength);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
		if(blocksArray != null){
			
			textPaint.setTextSize((int) (20 * scale));
			textPaint.setColor(Color.WHITE);
			
			circlePaint.setColor(Color.WHITE);
			circlePaint.setStrokeWidth(3);
	        canvas.drawRect(0, 0, 277, 286, circlePaint);
	        
	        double widthPerMinute = 0.38;
			
			for (int i=0; i < blocksArray.length(); i++)
			{
		    	
				try {
					
					int yPos1 = (i * 40) + 3;
					int yPos2 = ((i+1) * 40) + 3;
					int yPos3 = (i * 40) + 30;
					int color = 0;
					String person = "male";

				    JSONArray dayArray = blocksArray.getJSONArray(i);
				    
				    int begin = 3;
				    int end = 0;
				    
					for (int j=0; j < dayArray.length(); j++)
					{
						JSONObject block = dayArray.getJSONObject(j);
						int minutes = Integer.parseInt(block.getString("minutes"));
						person = block.getString("person").toString();
									
						if(person.equalsIgnoreCase("male")){
							circlePaint.setColor(Color.argb(100, 123, 183, 233));
						} else if(person.equalsIgnoreCase("female")){
							circlePaint.setColor(Color.argb(100, 171, 123, 233));
						}
						
						Log.d("Events",person);
						
						end = (int) (minutes * widthPerMinute);
						
					    canvas.drawRect(begin, yPos1, end, yPos2, circlePaint);
					    canvas.drawText(person, 9, yPos3, textPaint);
						
					    begin = end;
					    
					}
					
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			        
			   
			}
			
			// 7 * 40 = 280 + (2 * 3 = 6) = 286
			
			//textPaint.setTypeface(customFont);
			/*textPaint.setTextSize((int) (20 * scale));
			textPaint.setColor(Color.WHITE);
			
			circlePaint.setColor(Color.WHITE);
			circlePaint.setStrokeWidth(3);
	        canvas.drawRect(0, 0, 277, 286, circlePaint);
	        
	        // Sunday
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 123, 183, 233));
	        canvas.drawRect(3, 243, 274, 283, circlePaint);
	        canvas.drawText("SUN", 9, 270, textPaint);
	        
	        // Saturday
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 171, 123, 233));
	        canvas.drawRect(3, 203, 274, 243, circlePaint);
	        canvas.drawText("SAT", 9, 230, textPaint);
	        
	        // Friday
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 123, 183, 233));
	        canvas.drawRect(3, 163, 274, 203, circlePaint);
	        canvas.drawText("FRI", 9, 190, textPaint);
	        
	        // Thursday
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 171, 123, 233));
	        canvas.drawRect(3, 123, 274, 163, circlePaint);
	        canvas.drawText("THU", 9, 150, textPaint);
	        
	        // Wednesday
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 123, 183, 233));
	        canvas.drawRect(3, 83, 274, 123, circlePaint);
	        canvas.drawText("WED", 9, 110, textPaint);
	        
	        // Tuesday (second part)
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 171, 123, 233));
	        canvas.drawRect(100, 43, 274, 83, circlePaint);
	        
	        // Tuesday (first part)
	        circlePaint.setStrokeWidth(0);
	        circlePaint.setColor(Color.argb(100, 123, 183, 233));
	        canvas.drawRect(3, 43, 100, 83, circlePaint);
	        canvas.drawText("TUE", 9, 70, textPaint);
	        
	        // Monday
	        circlePaint.setColor(Color.argb(100, 123, 183, 233));
	        canvas.drawRect(3, 3, 274, 43, circlePaint);
	        canvas.drawText("MON", 9, 30, textPaint);*/
	        
	        //textPaint.setColor(Color.BLACK);
	        //canvas.drawText("WEEK 52 (24-12 - 01-01)", 0, 20, textPaint);
			
		}
		
		
		
		
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float xPosition = event.getX();
        float yPosition = event.getY();
        
        String sPosition = String.valueOf(xPosition);;
        //Log.d("Events",sPosition);
        
        return true;
        
	}

	
}