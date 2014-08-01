package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;
import android.widget.LinearLayout;


/**
 * Class for password Entry
 * 
 * Modified: 7/12/2014
 * 
 * @author Adam Lyon
 *
 */
public class PasswordEntry extends DialogFragment implements OnClickListener 
{
	public interface PasswordEntryListener 
	{
		public void passwordEntered(int choice, int caller, String password);
	}
	public static final int CHOICE_CONFIRM = 1;
	public static final int CHOICE_CANCEL = 2;
	
	private PasswordEntryListener activity;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	private TextView title;
	private TextView message;
	
	private EditText entry;
	
	private Button confirm;
	private Button cancel;
	
	private int choice;
	private int caller;
	
	private String messageText = "";
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.enter_password_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (PasswordEntryListener) this.getActivity();
		
		caller = this.getArguments().getInt(getString(R.string.enter_password_caller));
		messageText = this.getArguments().getString(getString(R.string.enter_password_message_text));
		
		background = (LinearLayout) view.findViewById(R.id.enter_password_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		title = (TextView) view.findViewById(R.id.enter_password_title);
		title.setTypeface(font.getCurrentFont());
		title.setTextSize(font.getFontSize()+5);
		title.setTextColor(theme.getTextColor());
		
		message = (TextView) view.findViewById(R.id.enter_password_text);
		message.setTypeface(font.getCurrentFont());
		message.setTextSize(font.getFontSize());
		message.setTextColor(theme.getTextColor());
		message.setText(messageText);
		
		entry = (EditText) view.findViewById(R.id.enter_password_entry);
		entry.setTypeface(font.getCurrentFont());
		entry.setTextSize(font.getFontSize());
		entry.setTextColor(theme.getTextColor());
		
		confirm = (Button) view.findViewById(R.id.enter_password_confirm_button);
		confirm.setTypeface(font.getCurrentFont());
		confirm.setTextSize(font.getFontSize());
		confirm.setBackgroundResource(theme.getGeneralButton());
		confirm.setOnClickListener(this);
		
		cancel = (Button) view.findViewById(R.id.enter_password_cancel_button);
		cancel.setTypeface(font.getCurrentFont());
		cancel.setTextSize(font.getFontSize());
		cancel.setBackgroundResource(theme.getGeneralButton());
		cancel.setOnClickListener(this);
		
		
		return view;
	}

	@Override
	public void onClick(View view) 
	{
		if (view.equals(confirm)) 
		{
			choice = CHOICE_CONFIRM;
		}
		if (view.equals(cancel)) 
		{
			choice = CHOICE_CANCEL;
		}
		activity.passwordEntered(choice, caller, entry.getText().toString());
		this.dismiss();
	}
	
}// End of PasswordEntry class
