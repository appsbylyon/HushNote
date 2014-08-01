package com.appsbylyon.hushnote.fragments;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.DialogFragment;
import android.content.res.Resources;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.NoteBook;
import com.appsbylyon.hushnote.Objects.NotebookSummary;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class to create a new notebook
 * 
 * Modified: 7/11/2014
 * 
 * @author Adam Lyon
 *
 */
public class CreateNotebook extends DialogFragment implements OnClickListener, OnCheckedChangeListener 
{
	public interface CreateNotebookListener 
	{
		public int newNotebookGetScreenWidth();
		public int newNotebookGetScreenHeight();
		public void newNoteBookCreated(NoteBook notebook, NotebookSummary notebookSummary);
		public NotebookSummary getSummaryToEdit();
		public void summaryEdited(NotebookSummary summary, int editPosition, boolean isSecured, String password);
	}
	
	private static final int MAX_TITLE_LENGTH = 16;
	private static final int MAX_DESC_LENGTH = 48;
	private static final int MAX_PASSWORD_LENGTH = 16;
	
	public static final int MODE_CREATE = 1;
	public static final int MODE_EDIT = 2;
	
	private Theme theme = Theme.getInstance();
	
	private ScrollView background;
	
	private TextView title;
	private TextView titlePrompt;
	private TextView descPrompt;
	private TextView passPrompt;
	private TextView repassPrompt;
	private TextView passwordReadback;
	
	private EditText titleEntry;
	private EditText descEntry;
	private EditText passEntry;
	private EditText repassEntry;
	
	private CheckBox secureCheck;
	
	private Button okButton;
	private Button cancelButton;
	
	private static final double DIALOG_SCREEN_WIDTH_RATIO = 0.8;
	
	private int screenWidth;
	private int mode;
	private int editPosition;
	
	private String editPassword;
	
	private Font font = Font.getInstance();
	
	private NotebookSummary summaryToEdit;
	
	CreateNotebookListener activity;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.new_notebook_fragment, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Bundle bundle = this.getArguments();
		mode = bundle.getInt(getString(R.string.create_notebook_mode));
		
		activity = (CreateNotebookListener) getActivity();
		screenWidth = activity.newNotebookGetScreenWidth();
		
		LinearLayout subLayout = (LinearLayout) view.findViewById(R.id.create_notebook_sub_layout);
		subLayout.setMinimumWidth((int)((double)screenWidth*DIALOG_SCREEN_WIDTH_RATIO));
		
		background = (ScrollView) view.findViewById(R.id.create_notebook_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		title = (TextView) view.findViewById(R.id.create_notebook_header);
		title.setTypeface(font.getCurrentFont());
		title.setTextSize(font.getFontSize()+5);
		title.setTextColor(theme.getTextColor());
		
		
		titlePrompt = (TextView) view.findViewById(R.id.create_notebook_title_prompt);
		titlePrompt.setTypeface(font.getCurrentFont());
		titlePrompt.setTextSize(font.getFontSize());
		titlePrompt.setTextColor(theme.getTextColor());
		
		descPrompt = (TextView) view.findViewById(R.id.create_notebook_desc_prompt);
		descPrompt.setTypeface(font.getCurrentFont());
		descPrompt.setTextSize(font.getFontSize());
		descPrompt.setTextColor(theme.getTextColor());
		
		passPrompt = (TextView) view.findViewById(R.id.create_notebook_password_prompt);
		passPrompt.setTypeface(font.getCurrentFont());
		passPrompt.setTextSize(font.getFontSize());
		passPrompt.setVisibility(View.INVISIBLE);
		passPrompt.setTextColor(theme.getTextColor());
		
		repassPrompt = (TextView) view.findViewById(R.id.create_notebook_repassword_prompt);
		repassPrompt.setTypeface(font.getCurrentFont());
		repassPrompt.setTextSize(font.getFontSize());
		repassPrompt.setVisibility(View.INVISIBLE);
		repassPrompt.setTextColor(theme.getTextColor());
		
		passwordReadback = (TextView) view.findViewById(R.id.create_notebook_password_readback);
		passwordReadback.setTypeface(font.getCurrentFont());
		passwordReadback.setTextSize(font.getFontSize()-5);
		passwordReadback.setVisibility(View.INVISIBLE);
		passwordReadback.setTextColor(theme.getTextColor());
		
		titleEntry = (EditText) view.findViewById(R.id.create_notebook_title_text);
		titleEntry.setTypeface(font.getCurrentFont());
		titleEntry.setTextSize(font.getFontSize());
		titleEntry.setTextColor(theme.getTextColor());

		descEntry = (EditText) view.findViewById(R.id.create_notebook_desc_text);
		descEntry.setTypeface(font.getCurrentFont());
		descEntry.setTextSize(font.getFontSize());
		descEntry.setTextColor(theme.getTextColor());
		
		passEntry = (EditText) view.findViewById(R.id.create_notebook_password_text);
		passEntry.setTypeface(font.getCurrentFont());
		passEntry.setTextSize(font.getFontSize());
		passEntry.setEnabled(false);
		passEntry.setVisibility(View.INVISIBLE);
		passEntry.setTextColor(theme.getTextColor());
		
		repassEntry = (EditText) view.findViewById(R.id.create_notebook_repassword_text);
		repassEntry.setTypeface(font.getCurrentFont());
		repassEntry.setTextSize(font.getFontSize());
		repassEntry.setEnabled(false);
		repassEntry.setVisibility(View.INVISIBLE);
		repassEntry.setTextColor(theme.getTextColor());
		
		secureCheck = (CheckBox) view.findViewById(R.id.create_notebook_secure_checkbox);
		secureCheck.setTypeface(font.getCurrentFont());
		secureCheck.setTextSize(font.getFontSize());
		secureCheck.setChecked(false);
		secureCheck.setOnCheckedChangeListener(this);
		int checkId = Resources.getSystem().getIdentifier("btn_check_holo_dark", "drawable", "android");
		secureCheck.setButtonDrawable(checkId);
		secureCheck.setTextColor(theme.getTextColor());
		
		okButton = (Button) view.findViewById(R.id.create_notebook_ok_button);
		okButton.setTypeface(font.getCurrentFont());
		okButton.setTextSize(font.getFontSize());
		okButton.setEnabled(false);
		okButton.setBackgroundResource(theme.getGeneralButton());
		okButton.setOnClickListener(this);
		
		cancelButton = (Button) view.findViewById(R.id.create_notebook_cancel_button);
		cancelButton.setTypeface(font.getCurrentFont());
		cancelButton.setTextSize(font.getFontSize());
		cancelButton.setBackgroundResource(theme.getGeneralButton());
		cancelButton.setOnClickListener(this);
		
		titleEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				String entry = s.toString();
				if (entry.length() > MAX_TITLE_LENGTH) 
				{
					titleEntry.setText(entry.substring(0, MAX_TITLE_LENGTH));
					titleEntry.setSelection(MAX_TITLE_LENGTH);
					Toast.makeText(getActivity(), "Title Length Cannot Be More Than "+MAX_TITLE_LENGTH, Toast.LENGTH_SHORT).show();
				}
				checkToEnableOkButton();
			}
		});
		
		descEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				String entry = s.toString();
				if (entry.length() > MAX_DESC_LENGTH) 
				{
					descEntry.setText(entry.substring(0, MAX_DESC_LENGTH));
					descEntry.setSelection(MAX_DESC_LENGTH);
					Toast.makeText(getActivity(), "Description Length Cannot Be More Than "+MAX_DESC_LENGTH, Toast.LENGTH_SHORT).show();
				}
				checkToEnableOkButton();
			}
		});
		
		passEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				String space = "\\s";
		        Pattern spacePattern = Pattern.compile(space);
		        Matcher spaceMatcher = spacePattern.matcher(s.toString());
		        if (spaceMatcher.find()) 
		        {
		        	passEntry.setText(s.toString().subSequence(0, (s.toString().length()-1)));
		        	passEntry.setSelection(passEntry.getText().toString().length());
		        	Toast.makeText(getActivity(), "Password Cannot Contain Spaces", Toast.LENGTH_SHORT).show();
		        }
		        if (s.toString().length() > MAX_PASSWORD_LENGTH) 
		        {
		        	passEntry.setText(s.toString().substring(0, MAX_PASSWORD_LENGTH));
		        	passEntry.setSelection(MAX_PASSWORD_LENGTH);
		        	Toast.makeText(getActivity(), "Password Cannot Be Longer Than "+MAX_PASSWORD_LENGTH, Toast.LENGTH_SHORT).show();
		        }
				checkToEnableOkButton();
			}
		});
		
		repassEntry.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				String space = "\\s";
		        Pattern spacePattern = Pattern.compile(space);
		        Matcher spaceMatcher = spacePattern.matcher(s.toString());
		        if (spaceMatcher.find()) 
		        {
		        	repassEntry.setText(s.toString().subSequence(0, (s.toString().length()-1)));
		        	repassEntry.setSelection(repassEntry.getText().toString().length());
		        	Toast.makeText(getActivity(), "Password Cannot Contain Spaces", Toast.LENGTH_SHORT).show();
		        }
		        if (s.toString().length() > MAX_PASSWORD_LENGTH) 
		        {
		        	repassEntry.setText(s.toString().substring(0, MAX_PASSWORD_LENGTH));
		        	repassEntry.setSelection(MAX_PASSWORD_LENGTH);
		        	Toast.makeText(getActivity(), "Password Cannot Be Longer Than "+MAX_PASSWORD_LENGTH, Toast.LENGTH_SHORT).show();
			    }
				checkToEnableOkButton();
			}
		});
		
		if (mode == MODE_EDIT) 
		{
			editPassword = bundle.getString(getString(R.string.create_notebook_edit_password));
			editPosition = bundle.getInt(getString(R.string.create_notebook_edit_position));
			summaryToEdit = activity.getSummaryToEdit();
			titleEntry.setText(summaryToEdit.getTitle());
			descEntry.setText(summaryToEdit.getDescription());
			if (summaryToEdit.isEncrypted()) 
			{
				secureCheck.setChecked(true);
				passEntry.setText(editPassword);
				repassEntry.setText(editPassword);
			}
			checkToEnableOkButton();
		}
		
		return view;
	}
	
	private boolean comparePasswords(String pass, String repass) 
	{
		if ((pass.length() > 0) || (repass.length() > 0)) 
		{
			if (pass.compareTo(repass) == 0) 
			{
				passwordReadback.setVisibility(View.INVISIBLE);
				return true;
			} 
			else 
			{
				passwordReadback.setVisibility(View.VISIBLE);
				return false;
			}
		}
		else 
		{
			passwordReadback.setVisibility(View.INVISIBLE);
			return false;
		}
	}
	
	private void checkToEnableOkButton() 
	{
		String passText = passEntry.getText().toString();
		String repassText = repassEntry.getText().toString();
		String titleText = titleEntry.getText().toString();
		String descText = descEntry.getText().toString();
		boolean secureBook = secureCheck.isChecked();
		boolean enableOkButton = false;
		
		if ((titleText.length() > 0) && (descText.length() > 0)) 
		{
			if ((secureBook)) 
			{
				if(comparePasswords(passText, repassText)) 
				{
					enableOkButton = true;
				}
			}
			else 
			{
				enableOkButton = true;
			}
		}
		else 
		{
			if (secureBook) 
			{
				comparePasswords(passText, repassText);
			}
		}
		okButton.setEnabled(enableOkButton);
	}

	@Override
	public void onClick(View view) 
	{
		if (view == cancelButton) 
		{
			this.dismiss();
		}
		if (view == okButton) 
		{
			if (mode == MODE_CREATE) 
			{
				NotebookSummary summary = new NotebookSummary(titleEntry.getText().toString().trim(),
						descEntry.getText().toString().trim(), secureCheck.isChecked());
				NoteBook notebook;
				if (secureCheck.isChecked()) 
				{
					notebook = new NoteBook(titleEntry.getText().toString().trim(),
							descEntry.getText().toString().trim(),
							secureCheck.isChecked(),
							passEntry.getText().toString());
				}
				else 
				{
					notebook = new NoteBook(titleEntry.getText().toString().trim(),
							descEntry.getText().toString().trim());
				}
				activity.newNoteBookCreated(notebook, summary);
			}
			else if (mode == MODE_EDIT) 
			{
				summaryToEdit.setTitle(titleEntry.getText().toString().trim());
				summaryToEdit.setDescription(descEntry.getText().toString().trim());
				summaryToEdit.setEncrypted(secureCheck.isChecked());
				String newPassword = passEntry.getText().toString();
				activity.summaryEdited(summaryToEdit, editPosition, secureCheck.isChecked(), newPassword);
			}
			this.dismiss();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) 
	{
		if (isChecked) 
		{
			passEntry.setEnabled(true);
			repassEntry.setEnabled(true);
			passPrompt.setVisibility(View.VISIBLE);
			repassPrompt.setVisibility(View.VISIBLE);
			passEntry.setVisibility(View.VISIBLE);
			repassEntry.setVisibility(View.VISIBLE);
			checkToEnableOkButton();
			
		} 
		else 
		{
			passEntry.setEnabled(false);
			repassEntry.setEnabled(false);
			passPrompt.setVisibility(View.INVISIBLE);
			repassPrompt.setVisibility(View.INVISIBLE);
			passEntry.setVisibility(View.INVISIBLE);
			repassEntry.setVisibility(View.INVISIBLE);
			if (passwordReadback.getVisibility() == View.VISIBLE) {passwordReadback.setVisibility(View.INVISIBLE);}
			checkToEnableOkButton();
		}
			
		
	}
}// End of CreateNotebook class
