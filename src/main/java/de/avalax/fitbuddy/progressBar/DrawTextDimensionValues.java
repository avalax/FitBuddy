package de.avalax.fitbuddy.progressBar;

import android.graphics.Paint;

public class DrawTextDimensionValues{
	private Paint paint;
	private String text;
    private int width;
	private int height;
	private double x;
	private double y;

	public DrawTextDimensionValues(Paint paint, String text,int width,int height){
		this.paint = paint;
		this.text = text;
		this.width = width;
		this.height = height;
	}

	public long getX(){
		return Math.round(x);
	}

	public long getY(){
		return Math.round(y);
	}

	public DrawTextDimensionValues invoke(){
        double fontWidth = paint.measureText(text, 0, text.length());
		x = (width / 2) - (fontWidth /2);
		y = height / 2;
		return this;
	}
}
