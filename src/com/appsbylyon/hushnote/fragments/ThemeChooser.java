package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

public class ThemeChooser extends DialogFragment implements OnClickListener, OnItemSelectedListener
{
	public interface ThemeChooserListener 
	{
		public void themeChosen();
	}
	
	private ThemeChooserListener activity;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private int originalTheme = theme.getCurrTheme();
	
	private LinearLayout background;
	
	private TextView header;
	
	private Spinner themeSpinner;
	
	private Button cancelButton;
	private Button confirmButton;
	
	private boolean isThemeLayoutTrigger = true;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_theme_chooser, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (ThemeChooserListener) this.getActivity();
		
		background = (LinearLayout) view.findViewById(R.id.theme_chooser_background);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) view.findViewById(R.id.theme_chooser_header);
		header.setTextColor(theme.getTextColor());
		header.setTypeface(font.getCurrentFont());
		header.setTextSize(font.getFontSize()+5);
		
		cancelButton = (Button) view.findViewById(R.id.theme_chooser_cancel_button);
		cancelButton.setTypeface(font.getCurrentFont());
		cancelButton.setTextSize(font.getFontSize());
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		cancelButton.setOnClickListener(this);
		
		confirmButton = (Button) view.findViewById(R.id.theme_chooser_confirm_button);
		confirmButton.setTypeface(font.getCurrentFont());
		confirmButton.setTextSize(font.getFontSize());
		confirmButton.setBackgroundResource(theme.getGeneralButton());
		confirmButton.setOnClickListener(this);
		
		themeSpinner = (Spinner) view.findViewById(R.id.theme_chooser_spinner);
		
		
		
		themeSpinner.setOnItemSelectedListener(this);
		
		/**
		ArrayList<String> colors = new ArrayList<String>();
		colors.add(theme.getThemeName(Theme.BLUE_THEME));
		colors.add(theme.getThemeName(Theme.GREEN_THEME));
		colors.add(theme.getThemeName(Theme.METAL_THEME));
		colors.add(theme.getThemeName(Theme.PINK_THEME));
		*/
		ArrayAdapter<String> themeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, theme.getColorList())
			{
		    	public View getView(int position, View convertView, ViewGroup parent) 
				{
					View v = super.getView(position, convertView, parent);
					
					((TextView) v).setTextColor(theme.getTextColor());
					return v;
				}
				
				public View getDropDownView(int position, View convertView, ViewGroup parent) 
				{
					View v = super.getDropDownView(position, convertView, parent);
					
					((TextView) v).setBackgroundResource(theme.getSmallBorder());
					((TextView) v).setTextColor(theme.getTextColor());
					((TextView) v).setTypeface(font.getCurrentFont());
					((TextView) v).setTextSize(font.getFontSize());
					 return v;
				}
			};
			themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			themeSpinner.setAdapter(themeAdapter);
		    
			themeSpinner.setSelection(theme.getCurrTheme());
			
		return view; 
	}

	@Override
	public void onClick(View view) 
	{
		int id = view.getId();
		switch (id) 
		{
		case R.id.theme_chooser_cancel_button:
			theme.setTheme(originalTheme);
			this.dismiss();
			break;
		case R.id.theme_chooser_confirm_button:
			activity.themeChosen();
			this.dismiss();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!isThemeLayoutTrigger) 
    	{
    		theme.setTheme(themeSpinner.getSelectedItemPosition());
    		background.setBackgroundResource(theme.getLargeBorder());
    		cancelButton.setBackgroundResource(theme.getGeneralButton());
    		confirmButton.setBackgroundResource(theme.getGeneralButton());
    		header.setTextColor(theme.getTextColor());
    	}
    	else 
    	{
    		isThemeLayoutTrigger = false;
    	}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
}
