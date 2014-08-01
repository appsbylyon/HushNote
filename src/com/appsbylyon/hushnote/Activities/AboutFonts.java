package com.appsbylyon.hushnote.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;
import com.appsbylyon.hushnote.fragments.HelpDialog;
import com.appsbylyon.hushnote.fragments.HelpDialog.HelpDialogListener;
import com.appsbylyon.hushnote.fragments.MonkeyDialog;
import com.appsbylyon.hushnote.fragments.MonkeyDialog.MonkeyDialogListener;

/**
 * Class for the about fonts screen
 * 
 * Modified: 7/25/2014
 * 
 * @author Adam Lyon
 *
 */
public class AboutFonts extends Activity implements OnClickListener, MonkeyDialogListener, HelpDialogListener
{
	private Theme theme = Theme.getInstance();
	
	private Font font = Font.getInstance();
	
	private LinearLayout background;
	
	private TextView header;
	
	private Button monkeyButton;
	private Button otherButton;
	
	private int screenWidth;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_about_fonts);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		
		background = (LinearLayout) findViewById(R.id.about_fonts_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) findViewById(R.id.about_fonts_header);
		font.formatTextViewHeader(header);
		header.setTextColor(theme.getTextColor());
		
		monkeyButton = (Button) findViewById(R.id.about_fonts_love_monkey);
		monkeyButton.setTypeface(font.getCurrentFont());
		monkeyButton.setTextSize(font.getFontSize());
		monkeyButton.setBackgroundResource(theme.getGeneralButton());
		monkeyButton.setOnClickListener(this);
		
		otherButton = (Button) findViewById(R.id.about_fonts_other);
		otherButton.setTypeface(font.getCurrentFont());
		otherButton.setTextSize(font.getFontSize());
		otherButton.setBackgroundResource(theme.getGeneralButton());
		otherButton.setOnClickListener(this);
		
		ViewTreeObserver vto = monkeyButton.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout() 
			{
				int viewWidth = AboutFonts.this.monkeyButton.getWidth();
				otherButton.setWidth(viewWidth);
				
			}
		}); 
	}

	@Override
	public void onClick(View view) 
	{
		int id = view.getId();
		FragmentManager fm = this.getFragmentManager();
		
		
		switch (id) 
		{
		
		case R.id.about_fonts_love_monkey:
			MonkeyDialog monkey = new MonkeyDialog();
			monkey.show(fm, "monkey_dialog_fragment");
			break;
		case R.id.about_fonts_other:
			HelpDialog help = new HelpDialog();
			Bundle bundle = new Bundle();
			bundle.putInt(getString(R.string.help_dialog_caller), Help.CALLER_OTHER_FONTS);
			help.setArguments(bundle);
			help.show(fm, "help_dialog_fragment");
		}
		
	}

	@Override
	public int getScreenWidth() 
	{
		return screenWidth;
	}

	@Override
	public int getScreenHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}// End of AboutFonts class
