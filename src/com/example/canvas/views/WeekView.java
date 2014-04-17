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
import android.widget.Toast;

public class WeekView extends View {

	private Paint circlePaint;
	private Paint textPaint;
	private JSONArray blocksArray;
	private Float scale;
	private Typeface customFont;
	private Context context;
	
	public WeekView(Context context, AttributeSet attrs){
		
		super(context, attrs);
		circlePaint = new Paint();
		textPaint = new Paint();
		
		customFont = Typeface.createFromAsset(getContext().getAssets(), "MovLette.ttf");
		
		Resources resources = context.getResources();
		scale = resources.getDisplayMetrics().density;
		
		String sScale = String.valueOf(scale);
		//Log.d("Events","scale: "+sScale);
		
		Toast toast = Toast.makeText(context,"test",Toast.LENGTH_LONG);
		toast.show();
		
	}
	
	public void setData(String data){
		
		//Log.d("Events", data);
		
		try {
			
			JSONObject jsonObject = new JSONObject(data); 
			
			blocksArray = jsonObject.getJSONArray("blocks");
			
			String sLength = String.valueOf(blocksArray.length());
			//Log.d("Events",sLength);
			
			
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
					String type = "care";

				    JSONArray dayArray = blocksArray.getJSONArray(i);
				    
				    int begin = 3;
				    int end = 0;
				    
					for (int j=0; j < dayArray.length(); j++)
					{
						JSONObject block = dayArray.getJSONObject(j);
						int minutes = Integer.parseInt(block.getString("minutes"));
						
						person = block.getString("person").toString();
						type = block.getString("type").toString();
						
						end = (int) (minutes * widthPerMinute) + begin;
						
						if(type.equalsIgnoreCase("care")){
							
							if(person.equalsIgnoreCase("male")){
								circlePaint.setColor(Color.argb(100, 123, 183, 233));
							} else if(person.equalsIgnoreCase("female")){
								circlePaint.setColor(Color.argb(100, 171, 123, 233));
							}
							
						} else {
							
							circlePaint.setColor(Color.GRAY);
							end += 1;
							
						}
						
						String sEnd = String.valueOf(end);
						String sBegin = String.valueOf(begin);
						String sMinutes = String.valueOf(minutes);
						
						//Log.d("Events","block: "+sBegin+" "+sEnd+" "+sMinutes);
						
					    canvas.drawRect(begin, yPos1, end, yPos2, circlePaint);
					    //canvas.drawText(person, 9, yPos3, textPaint);
						
					    begin = end;
					    
					}
					
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			        
			   
			}
			
	        textPaint.setColor(Color.BLACK);
	        canvas.drawText("WEEK 52 (24-12 - 01-01)", 0, 20, textPaint);
			
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
