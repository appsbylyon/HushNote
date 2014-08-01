package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Dialog for exporting a notebook
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 *
 */
public class ExportNotebook extends DialogFragment implements OnClickListener
{
	public interface ExportNotebookListener 
	{
		public void exportOptionSelected(int exportOption);
	}
	
	public static final int EXPORT_AS_NOTEBOOK = 1;
	public static final int EXPORT_AS_TEXTFILE = 2;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	private LinearLayout background;
	
	private TextView header;
	
	private RadioButton exportAsNotebook;
	private RadioButton exportAsTextFile;
	
	private Button cancelButton;
	private Button confirmButton;
	
	private ExportNotebookListener activity;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_export_notebook, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (ExportNotebookListener) this.getActivity();
		
		background = (LinearLayout) view.findViewById(R.id.export_notebook_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) view.findViewById(R.id.export_header);
		header.setTypeface(font.getCurrentFont());
		header.setTextSize(font.getFontSize()+5);
		header.setTextColor(theme.getTextColor());
		
		int checkId = Resources.getSystem().getIdentifier("btn_radio_holo_dark", "drawable", "android");
		
		exportAsNotebook = (RadioButton) view.findViewById(R.id.export_as_notebook);
		exportAsNotebook.setTypeface(font.getCurrentFont());
		exportAsNotebook.setTextSize(font.getFontSize());
		exportAsNotebook.setButtonDrawable(checkId);
		exportAsNotebook.setTextColor(theme.getTextColor());
		exportAsNotebook.setChecked(true);
		
		exportAsTextFile = (RadioButton) view.findViewById(R.id.export_as_textfile);
		exportAsTextFile.setTypeface(font.getCurrentFont());
		exportAsTextFile.setTextSize(font.getFontSize());
		exportAsTextFile.setTextColor(theme.getTextColor());
		exportAsTextFile.setButtonDrawable(checkId);
		
		cancelButton = (Button) view.findViewById(R.id.export_cancel_button);
		cancelButton.setTypeface(font.getCurrentFont());
		cancelButton.setTextSize(font.getFontSize());
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		cancelButton.setOnClickListener(this);
		
		confirmButton = (Button) view.findViewById(R.id.export_confirm_button);
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
			this.dismiss();
		}
		if (view.equals(confirmButton))
		{
			int option = 0;
			if (exportAsNotebook.isChecked()) 
			{
				option = ExportNotebook.EXPORT_AS_NOTEBOOK;
			}
			if (exportAsTextFile.isChecked()) 
			{
				option = ExportNotebook.EXPORT_AS_TEXTFILE;
			}
			activity.exportOptionSelected(option);
			this.dismiss();
		}
			
	}
	
}// End of ExportNotebook Class