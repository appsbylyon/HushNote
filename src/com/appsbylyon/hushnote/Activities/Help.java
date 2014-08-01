package com.appsbylyon.hushnote.Activities;

import android.app.ActionBar;
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

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;
import com.appsbylyon.hushnote.fragments.HelpDialog;
import com.appsbylyon.hushnote.fragments.HelpDialog.HelpDialogListener;

/**
 * Activity for the help screen
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 *
 */
public class Help extends Activity implements OnClickListener, HelpDialogListener
{
	public static final int CALLER_GENERAL_USE = 1;
	public static final int CALLER_IMPORT_EXPORT = 2;
	public static final int CALLER_RESTRICTIONS = 3;
	public static final int CALLER_OTHER_FONTS = 4;
	
	private ActionBar actionBar;
	
	private Button generalButton;
	private Button importExportButton;
	private Button restrictionButton;
	
	private LinearLayout background;
	
	private Theme theme = Theme.getInstance();
	
	private Font font = Font.getInstance();
	
	private int screenWidth;
	private int screenHeight;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_help);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		
		actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
		
		background = (LinearLayout) findViewById(R.id.activity_help_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		generalButton = (Button) findViewById(R.id.activity_help_general_use_button);
		generalButton.setTypeface(font.getCurrentFont());
		generalButton.setTextSize(font.getFontSize());
		generalButton.setBackgroundResource(theme.getGeneralButton());
		generalButton.setOnClickListener(this);
		
		importExportButton = (Button) findViewById(R.id.activity_help_import_export_button);
		importExportButton.setTypeface(font.getCurrentFont());
		importExportButton.setTextSize(font.getFontSize());
		importExportButton.setBackgroundResource(theme.getGeneralButton());
		importExportButton.setOnClickListener(this);
		
		restrictionButton = (Button) findViewById(R.id.activity_help_restriction_button);
		restrictionButton.setTypeface(font.getCurrentFont());
		restrictionButton.setTextSize(font.getFontSize());
		restrictionButton.setBackgroundResource(theme.getGeneralButton());
		restrictionButton.setOnClickListener(this);
		
		ViewTreeObserver vto = importExportButton.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
			public void onGlobalLayout() 
			{
				int width = Help.this.importExportButton.getWidth();
				generalButton.setWidth(width);
				restrictionButton.setWidth(width);
			}
		});
		
	}


	@Override
	public void onClick(View view) 
	{
		int id = view.getId();
		int caller = 0;
		FragmentManager fm = this.getFragmentManager();
		Bundle bundle = new Bundle();
		switch (id) 
		{
		case R.id.activity_help_general_use_button:
			caller = CALLER_GENERAL_USE;
			break;
		case R.id.activity_help_import_export_button:
			caller = CALLER_IMPORT_EXPORT;
			break;
		case R.id.activity_help_restriction_button:
			caller = CALLER_RESTRICTIONS;
			break;
		}
		HelpDialog helpDialog = new HelpDialog();
		bundle.putInt(getString(R.string.help_dialog_caller), caller);
		helpDialog.setArguments(bundle);
		helpDialog.show(fm, "help_dialog_fragment");
		
	}


	@Override
	public int getScreenWidth() 
	{
		return this.screenWidth;
	}


	@Override
	public int getScreenHeight() 
	{
		return this.screenHeight;
	}
} // End of Help class