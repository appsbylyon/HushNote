package com.appsbylyon.hushnote.Font;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Class containing application wide font attributes.
 * 
 * Modified: 7/10/2014
 * 
 * @author Adam Lyon
 *
 */
public class Font 
{
	private static final Font instance = new Font();
	
	public static final int DEFAULT_FONT_SIZE = 16;
	public static final int DEFAULT_FONT = 0;
	public static final int MIN_FONT_SIZE = 10;
	public static final int MAX_FONT_SIZE = 36;
	
	private ArrayList<Typeface> fontList = new ArrayList<Typeface>();
	
	private Typeface currentFont;
	
	private int fontSize = DEFAULT_FONT_SIZE;
	
	private Font (){}
	
	private SharedPreferences sharedPrefs;
	
	private int monkeyPos;
	
	public static Font getInstance() 
	{
		return instance;
	}
	
	public void setSharedPrefs(SharedPreferences sharedPrefs, String fontNameKey, String fontSizeKey) 
	{
		this.sharedPrefs = sharedPrefs;
	}
	
	public boolean saveFont() 
	{
		Editor editor = sharedPrefs.edit();
		editor.putInt("font_id", fontList.indexOf(currentFont));
		editor.putInt("font_size", fontSize);
		return editor.commit();
	}
	
	public void addFont(Typeface newFont) 
	{
		this.fontList.add(newFont);
	}
	
	public void setCurrentFont(int location) 
	{
		currentFont = (Typeface) fontList.get(location);
	}
	
	public boolean setFontSize(int newSize) 
	{
		if ((newSize >= MIN_FONT_SIZE) && (newSize <= MAX_FONT_SIZE))
		{
			this.fontSize = newSize;
			return true;
		}
		return false;
	}
	
	public int getFontSize() 
	{
		return this.fontSize;
	}
	
	public void setDefaults() 
	{
		this.fontSize = DEFAULT_FONT_SIZE;
		this.currentFont = (Typeface) fontList.get(DEFAULT_FONT);
	}
	
	public Typeface getCurrentFont() 
	{
		return this.currentFont;
	}
	
	public int getCurrentFontPosition() 
	{
		return fontList.indexOf(currentFont);
	}
	
	public int getNumberOfFonts() 
	{
		return fontList.size();
	}
	
	public Typeface getFontAtPosition(int position) 
	{
		return (Typeface) fontList.get(position);
	}
	
	public void formatTextView(TextView textView) 
	{
		textView.setTypeface(this.getCurrentFont());
		textView.setTextSize(this.getFontSize());
	}
	
	public void formatTextViewHeader(TextView textView) 
	{
		textView.setTypeface(this.getCurrentFont());
		textView.setTextSize(this.getFontSize()+5);
	}
	
	public int getMonkeyPos() 
	{
		return monkeyPos;
	}
	
	public void setMonkeyPos(int pos) 
	{
		monkeyPos = pos;
	}
}// End of Font class
