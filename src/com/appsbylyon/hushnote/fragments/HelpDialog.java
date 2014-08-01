package com.appsbylyon.hushnote.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Activities.Help;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for the help dialog
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 *
 */
public class HelpDialog extends DialogFragment implements OnClickListener
{
	public interface HelpDialogListener 
	{
		public int getScreenWidth();
		public int getScreenHeight();
	}
	
	private static final double DIALOG_WIDTH_SCREEN_RATIO = 0.7;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private HelpDialogListener activity;
	
	private TextView header;
	private TextView text;
	
	private Button doneButton;
	
	private LinearLayout background;
	
	private int caller;
	
	private int dialogWidth = 0;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.help_dialog_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Bundle bundle = this.getArguments();
		caller = bundle.getInt(getString(R.string.help_dialog_caller));
		
		activity = (HelpDialogListener) this.getActivity();
		
		dialogWidth = (int) ((double) activity.getScreenWidth() * DIALOG_WIDTH_SCREEN_RATIO);
		
		background = (LinearLayout) view.findViewById(R.id.help_dialog_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
				
		header = (TextView) view.findViewById(R.id.help_dialog_header);
		header.setTypeface(font.getCurrentFont());
		header.setTextSize(font.getFontSize()+5);
		
		text = (TextView) view.findViewById(R.id.help_dialog_text);
		text.setTypeface(font.getCurrentFont());
		text.setTextSize(font.getFontSize());
		text.setMovementMethod(new ScrollingMovementMethod());
		text.setText("");
		
		ViewTreeObserver vto = text.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
			public void onGlobalLayout() 
			{
				text.setWidth(HelpDialog.this.dialogWidth);
			}
		});
		
		doneButton = (Button) view.findViewById(R.id.help_dialog_done_button);
		doneButton.setTypeface(font.getCurrentFont());
		doneButton.setTextSize(font.getFontSize());
		doneButton.setBackgroundResource(theme.getGeneralButton());
		doneButton.setOnClickListener(this);
		
		int resourceToLoad = 0;
		switch (caller) 
		{
		case Help.CALLER_GENERAL_USE:
			header.setText(getString(R.string.activity_help_general_use_button_text));
			resourceToLoad = R.raw.general_use;
			break;
		case Help.CALLER_IMPORT_EXPORT:
			header.setText(getString(R.string.activity_help_import_export_button_text));
			resourceToLoad = R.raw.import_export;
			break;
		case Help.CALLER_RESTRICTIONS:
			header.setText(getString(R.string.activity_help_restriction_button_text));
			resourceToLoad = R.raw.restrictions;
			break;
		case Help.CALLER_OTHER_FONTS:
			header.setText(getString(R.string.about_fonts_other_text));
			resourceToLoad = R.raw.other_fonts;
		}
		
		try 
        {
        	InputStreamReader input = new InputStreamReader(getResources().openRawResource(resourceToLoad));
        	BufferedReader br = new BufferedReader(input);
        	String line;
        	while ((line = br.readLine()) != null) 
        	{
        		text.append(line);
        		text.append("\n");
        	}
        	br.close();
        }
        catch (IOException IOE) 
        {
        	// No error handling needed
        }
		return view;
	}

	@Override
	public void onClick(View view) 
	{
		this.dismiss();
	}
}// End of HelpDialog class