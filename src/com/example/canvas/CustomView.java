package com.example.canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

	private Paint circlePaint;
	
	public CustomView(Context context, AttributeSet attrs){
		
		super(context, attrs);
		
		circlePaint = new Paint();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		circlePaint.setColor(Color.MAGENTA);
		
		int viewWidthHalf = this.getMeasuredWidth()/2;
		int viewHeightHalf = this.getMeasuredHeight()/2;
		
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
		
		//draw the text using the string attribute and chosen properties
		//canvas.drawText("HELP", viewWidthHalf, viewHeightHalf, circlePaint);
		canvas.drawRect(0, 0, 300, 30, circlePaint);
		
	}
	
}
