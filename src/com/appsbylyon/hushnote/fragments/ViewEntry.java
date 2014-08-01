package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for view a notebook entry
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 *
 */
public class ViewEntry extends DialogFragment implements OnClickListener
{
	public interface ViewEntryListener 
	{
		public Entry getEntry(int entryNum);
		public int getScreenWidthViewEntry();
		
	}
	
	private ViewEntryListener activity;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	private TextView title;
	private TextView dateCreated;
	private TextView dateModified;
	private TextView text;
	
	private Button doneButton;
	
	private Entry selectedEntry;
	
	private int selectedEntryNum;
	private int screenWidth;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.view_entry_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (ViewEntryListener) this.getActivity();
		
		Bundle bundle = this.getArguments();
		
		selectedEntryNum = bundle.getInt(getString(R.string.view_entry_selected_entry));
		selectedEntry = activity.getEntry(selectedEntryNum);
		
		background = (LinearLayout) view.findViewById(R.id.view_entry_fragment_layout);
		background.setBackgroundResource(theme.getLargeBorder());
		
		title = (TextView) view.findViewById(R.id.view_entry_title);
		title.setTypeface(font.getCurrentFont());
		title.setTextSize(font.getFontSize()+5);
		title.setTextColor(theme.getTextColor());
		title.setText(selectedEntry.getTitle());
		
		dateCreated = (TextView) view.findViewById(R.id.view_entry_date_created);
		dateCreated.setTypeface(font.getCurrentFont());
		dateCreated.setTextSize(font.getFontSize()-5);
		dateCreated.setTextColor(theme.getTextColor());
		dateCreated.setText("Created: "+selectedEntry.getStringDateCreated());
		
		dateModified = (TextView) view.findViewById(R.id.view_entry_date_modified);
		dateModified.setTypeface(font.getCurrentFont());
		dateModified.setTextSize(font.getFontSize()-5);
		dateModified.setTextColor(theme.getTextColor());
		dateModified.setText("Modified: "+selectedEntry.getStringDateModified());
		
		text = (TextView) view.findViewById(R.id.view_entry_text);
		text.setTypeface(font.getCurrentFont());
		text.setTextSize(font.getFontSize());
		text.setTextColor(theme.getTextColor());
		text.setMovementMethod(new ScrollingMovementMethod());
		text.setText(selectedEntry.getText());
		
		doneButton = (Button) view.findViewById(R.id.view_entry_ok_button);
		doneButton.setTypeface(font.getCurrentFont());
		doneButton.setTextSize(font.getFontSize());
		doneButton.setBackgroundResource(theme.getGeneralButton());
		doneButton.setOnClickListener(this);
		
		screenWidth = activity.getScreenWidthViewEntry();
		text.setMinimumWidth(screenWidth);
		
		return view;
	}

	@Override
	public void onClick(View view) 
	{
		this.dismiss();	
	}

}
