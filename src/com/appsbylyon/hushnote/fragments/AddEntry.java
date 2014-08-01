package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Activities.NotebookView;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Dialog for creating a new Entry
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 *
 */
public class AddEntry extends DialogFragment implements OnClickListener
{
	public interface AddEntryListener 
	{
		public void addNewEntry(Entry entry);
		public int getScreenWidth();
		public Entry getEntryToEdit(int position);
		public void entryEdited(Entry entry, int position);
	}
	
	private static final double DIALOG_SCREEN_WIDTH_RATIO = 0.8;
	
	private Font font = Font.getInstance();
	
	private TextView header;
	private TextView titlePrompt;
	private TextView textPrompt;
	
	private EditText titleEntry;
	private EditText textEntry;
	
	private Button cancelButton;
	private Button confirmButton;
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	private AddEntryListener activity;
	
	private Entry entryToEdit;
	
	private int mode;
	private int position;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.add_entry_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (AddEntryListener) this.getActivity();
				
		Bundle bundle = this.getArguments();
		
		background = (LinearLayout) view.findViewById(R.id.add_entry_sub_layout);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) view.findViewById(R.id.add_entry_header);
		header.setTypeface(font.getCurrentFont());
		header.setTextSize(font.getFontSize()+5);
		header.setTextColor(theme.getTextColor());
		
		titlePrompt = (TextView) view.findViewById(R.id.add_entry_title_prompt);
		titlePrompt.setTypeface(font.getCurrentFont());
		titlePrompt.setTextSize(font.getFontSize());
		titlePrompt.setTextColor(theme.getTextColor());
		
		textPrompt = (TextView) view.findViewById(R.id.add_entry_text_prompt);
		textPrompt.setTypeface(font.getCurrentFont());
		textPrompt.setTextSize(font.getFontSize());
		textPrompt.setTextColor(theme.getTextColor());
		
		titleEntry = (EditText) view.findViewById(R.id.add_entry_title_text);
		titleEntry.setTypeface(font.getCurrentFont());
		titleEntry.setTextSize(font.getFontSize());
		titleEntry.setTextColor(theme.getTextColor());
		
		textEntry = (EditText) view.findViewById(R.id.add_entry_text_text);
		textEntry.setTypeface(font.getCurrentFont());
		textEntry.setTextSize(font.getFontSize());
		textEntry.setTextColor(theme.getTextColor());
		
		cancelButton = (Button) view.findViewById(R.id.add_entry_cancel_button);
		cancelButton.setTypeface(font.getCurrentFont());
		cancelButton.setTextSize(font.getFontSize());
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		cancelButton.setOnClickListener(this);
		
		confirmButton = (Button) view.findViewById(R.id.add_entry_confirm_button);
		confirmButton.setTypeface(font.getCurrentFont());
		confirmButton.setTextSize(font.getFontSize());
		confirmButton.setBackgroundResource(theme.getGeneralButton());
		confirmButton.setOnClickListener(this);
		
		background.setMinimumWidth(((int)((double)activity.getScreenWidth()*DIALOG_SCREEN_WIDTH_RATIO)));
		titleEntry.setMinimumWidth(((int)((double)activity.getScreenWidth()*DIALOG_SCREEN_WIDTH_RATIO))-10);
		textEntry.setMinimumWidth(((int)((double)activity.getScreenWidth()*DIALOG_SCREEN_WIDTH_RATIO))-10);
		
		mode = bundle.getInt(getString(R.string.add_entry_dialog_mode));
		if (mode == NotebookView.MODE_EDIT_ENTRY) 
		{
			position = bundle.getInt(getString(R.string.add_entry_edit_position));
			entryToEdit = activity.getEntryToEdit(position);
			header.setText(getString(R.string.add_entry_edit_entry_title));
			titleEntry.setText(entryToEdit.getTitle());
			textEntry.setText(entryToEdit.getText());
			confirmButton.setText(getString(R.string.add_entry_edit_entry_button_text));
		}
		
		checkToEnableConfirmButton();
		
		titleEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				checkToEnableConfirmButton();
			}
		});
		
		textEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				checkToEnableConfirmButton();
			}
		});
			
		
		return view;
	}

	@Override
	public void onClick(View view) 
	{
		if (view.equals(cancelButton)) 
		{
			this.dismiss();
		}
		if (view.equals(confirmButton)) 
		{
			switch(mode) 
			{
			case NotebookView.MODE_ADD_ENTRY:
				String newTitle = titleEntry.getText().toString().trim();
				String newText = textEntry.getText().toString().trim();
				Entry newEntry = new Entry(newTitle, newText);
				activity.addNewEntry(newEntry);
				break;
			case NotebookView.MODE_EDIT_ENTRY:
				entryToEdit.setTitle(titleEntry.getText().toString(), false);
				entryToEdit.setText(textEntry.getText().toString(), true);
				activity.entryEdited(entryToEdit, position);
				break;
			}
			this.dismiss();
		}
		
	}

	private void checkToEnableConfirmButton() 
	{
		if ((titleEntry.getText().toString().trim().length() > 0) &&
				(textEntry.getText().toString().trim().length() > 0)) 
		{
			confirmButton.setEnabled(true);
		}
		else 
		{
			confirmButton.setEnabled(false);
		}
	}
	
}// end of AddEntry class
