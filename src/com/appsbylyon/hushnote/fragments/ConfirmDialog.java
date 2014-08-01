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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for a general confirmation dialog.
 * 
 * Modified: 7/12/2014
 * 
 * @author Adam Lyon
 *
 */
public class ConfirmDialog extends DialogFragment implements OnClickListener
{
	public interface ConfirmDialogListener
	{
		public void choiceSelected(int choice, int caller);
	}
	public static final int CHOICE_CONFIRM = 1;
	public static final int CHOICE_CANCEL = 2;
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	private TextView dialogTitle;
	private TextView dialogMessage;
	
	private Button cancelButton;
	private Button confirmButton;
	
	private Font font = Font.getInstance();
	
	private ConfirmDialogListener activity;
	
	private String dialogTitleText = "";
	private String dialogMessageText = "";
	
	private int caller = 0;
	private int choice = 0;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.confirm_dialog, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (ConfirmDialogListener) this.getActivity();
		
		dialogTitleText = this.getArguments().getString(getString(R.string.confirm_dialog_title));
		dialogMessageText = this.getArguments().getString(getString(R.string.confirm_dialog_message));
		caller = this.getArguments().getInt(getString(R.string.confirm_dialog_caller));
		
		background = (LinearLayout) view.findViewById(R.id.confirm_dialog_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		dialogTitle = (TextView) view.findViewById(R.id.confirm_dialog_title);
		dialogTitle.setText(dialogTitleText);
		dialogTitle.setTypeface(font.getCurrentFont());
		dialogTitle.setTextSize(font.getFontSize()+5);
		dialogTitle.setTextColor(theme.getTextColor());
		
		dialogMessage = (TextView) view.findViewById(R.id.confirm_dialog_text);
		dialogMessage.setText(dialogMessageText);
		dialogMessage.setTypeface(font.getCurrentFont());
		dialogMessage.setTextSize(font.getFontSize());
		dialogMessage.setTextColor(theme.getTextColor());
		
		cancelButton = (Button) view.findViewById(R.id.confirm_dialog_cancel_button);
		cancelButton.setTypeface(font.getCurrentFont());
		cancelButton.setTextSize(font.getFontSize());
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		cancelButton.setOnClickListener(this);
		
		confirmButton = (Button) view.findViewById(R.id.confirm_dialog_confirm_button);
		confirmButton.setTypeface(font.getCurrentFont());
		confirmButton.setTextSize(font.getFontSize());
		confirmButton.setBackgroundResource(theme.getGeneralButton());
		confirmButton.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View view) 
	{
		if (view.equals(cancelButton)) 
		{
			choice = CHOICE_CANCEL;
		}
		if (view.equals(confirmButton)) 
		{
			choice = CHOICE_CONFIRM;
		}
		activity.choiceSelected(choice, caller);
		this.dismiss();
	}
	
}// End of ConfirmDialog class
