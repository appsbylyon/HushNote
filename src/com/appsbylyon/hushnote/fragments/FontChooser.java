package com.appsbylyon.hushnote.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for the Font Chooser Dialog
 * 
 * Modified: 7/10/2014
 * 
 * @author Adam Lyon
 *
 */
public class FontChooser extends DialogFragment implements OnClickListener
{
	
	public interface FontChosen 
	{
		public void fontChosen();
		public int getScreenWidth();
		public int getScreenHeight();
	}
	
	private Theme theme = Theme.getInstance();
	private LinearLayout background;
	
	private static final double DIALOG_SCREEN_WIDTH_RATIO = 0.8;
	private static final double DIALOG_SCREEN_HEIGHT_RATIO = 0.1;
	
	private Spinner fontNameSpinner;
	private Spinner fontSizeSpinner;
	
	private TextView dialogTitle;
	private TextView previewText;
	private TextView previewLabel;
	
	private Button cancelButton;
	private Button okButton;
	
	private Font font = Font.getInstance();
	
	private boolean isFontNameLayoutTrigger = true;
	private boolean isFontSizeLayoutTrigger = true;
	
	
	private int originalFont = font.getCurrentFontPosition();
	private int originalFontSize = font.getFontSize();
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.font_chooser_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		background = (LinearLayout) view.findViewById(R.id.font_chooser_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		fontNameSpinner = (Spinner) view.findViewById(R.id.font_chooser_spin_font_name);
		fontSizeSpinner = (Spinner) view.findViewById(R.id.font_chooser_spin_font_size);
		
		dialogTitle = (TextView) view.findViewById(R.id.font_chooser_title);
		previewText = (TextView) view.findViewById(R.id.font_chooser_tv_preview);
		previewLabel = (TextView) view.findViewById(R.id.font_chooser_tv_preview_label);
		previewLabel.setTextColor(theme.getTextColor());
		
		cancelButton = (Button) view.findViewById(R.id.font_chooser_cancel_button);
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		
		okButton = (Button) view.findViewById(R.id.font_chooser_ok_button);
		okButton.setBackgroundResource(theme.getGeneralButton());
		
		cancelButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		
		FontChosen activity = (FontChosen) getActivity();
		int screenWidth = activity.getScreenWidth();
		int screenHeight = activity.getScreenHeight();
		previewText.setWidth((int)((float)screenWidth*DIALOG_SCREEN_WIDTH_RATIO));
		previewText.setHeight((int)((float)screenHeight*DIALOG_SCREEN_HEIGHT_RATIO));
		
		fontNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{
				if (!isFontNameLayoutTrigger) 
            	{
            		font.setCurrentFont(fontNameSpinner.getSelectedItemPosition());
            		previewText.setTypeface(font.getCurrentFont());
            		dialogTitle.setTypeface(font.getCurrentFont());
            	}
            	else 
            	{
            		isFontNameLayoutTrigger = false;
            	}
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
        });
		
		fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{
				if (!isFontSizeLayoutTrigger) 
            	{
            		font.setFontSize(Integer.parseInt((String)fontSizeSpinner.getSelectedItem()));
            		dialogTitle.setTextSize(font.getFontSize());
            		previewText.setTextSize((float) font.getFontSize());
            	}
            	else 
            	{
            		isFontSizeLayoutTrigger = false;
            	}
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		previewText.setTypeface(font.getCurrentFont());
		previewText.setTextSize((float) font.getFontSize());
		previewText.setTextColor(theme.getTextColor());
		dialogTitle.setTextSize(font.getFontSize());
		dialogTitle.setTypeface(font.getCurrentFont());
		dialogTitle.setTextColor(theme.getTextColor());
		
		String[] fontNameArray = getResources().getStringArray(R.array.font_names);
		ArrayList<String> fontNameList = new ArrayList<String>(Arrays.asList(fontNameArray));
		
		ArrayAdapter<String> fontNameAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, fontNameList)
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
						((TextView) v).setTypeface(font.getFontAtPosition(position));
						((TextView) v).setTextSize(font.getFontSize());
						((TextView) v).setTextColor(theme.getTextColor());
						 return v;
					}
				};
		fontNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    fontNameSpinner.setAdapter(fontNameAdapter);
	    fontNameSpinner.setSelection(font.getCurrentFontPosition());
	 
	    
	    ArrayList<String> fontSizeList = new ArrayList<String>();
	    for (int i = Font.MIN_FONT_SIZE; i < (Font.MAX_FONT_SIZE+1); i++) 
	    {
	    	fontSizeList.add(Integer.toString(i));
	    }
	    ArrayAdapter<String> fontSizeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, fontSizeList)
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
						((TextView) v).setTypeface(font.getCurrentFont());
						((TextView) v).setTextSize((float)(position+Font.MIN_FONT_SIZE));
						((TextView) v).setTextColor(theme.getTextColor());
						 return v;
					}
	    		};
	    fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    fontSizeSpinner.setAdapter(fontSizeAdapter);
	    fontSizeSpinner.setSelection((font.getFontSize()-Font.MIN_FONT_SIZE));
	    
	    return view;
	}


	@Override
	public void onClick(View view) 
	{
		if (view == this.cancelButton) 
		{
			font.setCurrentFont(originalFont);
			font.setFontSize(originalFontSize);
			this.dismiss();
		}
		if (view == this.okButton) 
		{
			if(font.saveFont()) 
			{
				Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
				FontChosen activity = (FontChosen) getActivity();
				activity.fontChosen();
			}
			else 
			{
				Toast.makeText(getActivity(), "Changes NOT Saved", Toast.LENGTH_SHORT).show();
			}
			
			this.dismiss();
		}
	}
}// End of FontChooser class