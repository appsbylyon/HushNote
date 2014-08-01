package com.appsbylyon.hushnote.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Theme.Theme;
/**
 * Class for the about activity
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 *
 */
public class About extends Activity 
{
	private static final double ICON_SCREEN_WIDTH_RATIO = 0.6;
	
	private ActionBar actionBar;
	
	private ImageView icon;
	
	private int screenWidth;
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_about);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		
		actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
		
		background = (LinearLayout) findViewById(R.id.activity_about_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		icon = (ImageView) findViewById(R.id.about_icon);
		
		ViewTreeObserver vto = icon.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
			public void onGlobalLayout()  
			{
				int maxWidth = ((int)((double)About.this.screenWidth*About.ICON_SCREEN_WIDTH_RATIO));
				int xPos = (About.this.screenWidth-maxWidth)/2;
				About.this.icon.setMaxWidth(maxWidth);
				About.this.icon.setX((float)xPos);
				
			}
		});
		
		
		
	}
}// end of About class
