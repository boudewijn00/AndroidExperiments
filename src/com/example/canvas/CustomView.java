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
		
		if(usersArray != null){
			
			for (int i=0; i < usersArray.length(); i++)
			{
			    try {
			    	
			    	if(i == 0){
			    		circlePaint.setColor(Color.MAGENTA);
			    	} else {
			    		circlePaint.setColor(Color.GREEN);
			    	}
			    	
			        JSONObject oneObject = usersArray.getJSONObject(i);
			        // Pulling items from the array
			        String name = oneObject.getString("name");
			        Log.d("Events",name);
			        
			        int viewWidthHalf = this.getMeasuredWidth()/2;
					int viewHeightHalf = this.getMeasuredHeight()/2;
					
					int yMargin = i*60;
					Log.d("Events",String.valueOf(yMargin));
					
					int radius = 0;
					if(viewWidthHalf>viewHeightHalf)
						radius=viewHeightHalf-10;
					else
						radius=viewWidthHalf-10;

					//draw the circle using the properties defined
					//canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
					
					//circlePaint.setColor(Color.WHITE);
					//circlePaint.setTextAlign(Paint.Align.CENTER);
					//circlePaint.setTextSize(50);
					//circlePaint.setStyle(Paint.Style.FILL);
					
					//canvas.drawRect(277, yMargin, 0, 0, circlePaint);
					
					//circlePaint.setStyle(Paint.Style.STROKE);
				    //circlePaint.setColor(Color.BLACK);
					
					//draw the text using the string attribute and chosen properties
					//canvas.drawText("HELP", viewWidthHalf, viewHeightHalf, circlePaint);
					//canvas.drawRect(277, yMargin, 0, 0, circlePaint);
			        
			    } catch (JSONException e) {
			        // Oops
			    }
			}
			
			// 7 * 40 = 280 + (2 * 3 = 6) = 286
			
			//textPaint.setTypeface(customFont);
			textPaint.setTextSize((int) (20 * scale));
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
	        canvas.drawText("MON", 9, 30, textPaint);
	        
	        //textPaint.setColor(Color.BLACK);
	        //canvas.drawText("WEEK 52 (24-12 - 01-01)", 0, 20, textPaint);
			
		}
		
		
		
		
		
		
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
