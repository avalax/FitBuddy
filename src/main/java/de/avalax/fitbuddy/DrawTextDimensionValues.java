package de.avalax.fitbuddy;

import android.graphics.Paint;

public class DrawTextDimensionValues{
	private Paint mTextPaint;
	private String mTextValue;
	private float mFontTextValueWidth;
	private int mWindowWidth;
	private int mWindowHeight;
	private int mX;
	private int mY;

	public DrawTextDimensionValues(Paint textPaint, String textValue,int windowWidth,int windowHeight){
		mTextPaint = textPaint;
		mTextValue = textValue;
		mWindowWidth = windowWidth;
		mWindowHeight = windowHeight;
	}

	public int getX(){
		return mX;
	}

	public int getY(){
		return mY;
	}

	public DrawTextDimensionValues invoke(){
		mFontTextValueWidth = mTextPaint.measureText(mTextValue, 0, mTextValue.length());
		mX = (int)((mWindowWidth / 2) - (mFontTextValueWidth /2));
		mY = mWindowHeight / 2;
		return this;
	}
}
