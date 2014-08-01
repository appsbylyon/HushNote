package com.appsbylyon.hushnote.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

public class MonkeyDialog extends DialogFragment implements OnClickListener
{
	public interface MonkeyDialogListener 
	{
		public int getScreenWidth();
	}
	
	private static final double LOGO_WIDTH_TO_SCREEN_RATIO = 0.6;
	
	private MonkeyDialogListener activity;
	
	private Font font = Font.getInstance();
	
	private Theme theme =  Theme.getInstance();
	
	private LinearLayout background;
	
	private TextView header;
	private TextView createdBy;
	private TextView mistiBiz;
	private TextView mistiName;
	private TextView visitWeb;
	private TextView website;
	
	private ImageView mistiLogo;
	
	private Button doneButton;
	
	private int screenWidth;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_i_love_you_monkey, container);
		getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (MonkeyDialogListener) this.getActivity();
		
		screenWidth = activity.getScreenWidth();
		
		background = (LinearLayout) view.findViewById(R.id.monkey_dialog_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) view.findViewById(R.id.moneky_header);
		header.setTypeface(font.getFontAtPosition(font.getMonkeyPos()));
		header.setTextSize(font.getFontSize()+5);
		header.setTextColor(theme.getTextColor());
		
		createdBy = (TextView) view.findViewById(R.id.monkey_created_by);
		createdBy.setTypeface(font.getCurrentFont());
		createdBy.setTextSize(font.getFontSize());
		createdBy.setTextColor(theme.getTextColor());
		
		mistiBiz = (TextView) view.findViewById(R.id.moneky_misti_biz_name);
		mistiBiz.setTypeface(font.getCurrentFont());
		mistiBiz.setTextSize(font.getFontSize());
		mistiBiz.setTextColor(theme.getTextColor());
		
		mistiName = (TextView) view.findViewById(R.id.monkey_misti_name);
		mistiName.setTypeface(font.getCurrentFont());
		mistiName.setTextSize(font.getFontSize());
		mistiName.setTextColor(theme.getTextColor());
		
		visitWeb = (TextView) view.findViewById(R.id.monkey_visit_website);
		visitWeb.setTypeface(font.getCurrentFont());
		visitWeb.setTextSize(font.getFontSize());
		visitWeb.setTextColor(theme.getTextColor());
		
		website = (TextView) view.findViewById(R.id.monkey_website);
		website.setTypeface(font.getCurrentFont());
		website.setTextSize(font.getFontSize());
		website.setTextColor(theme.getTextColor());
		website.setClickable(true);
		website.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='http://www.mistifonts.com'> www.mistifonts.com </a>";
		website.setText(Html.fromHtml(text));
		
		mistiLogo = (ImageView) view.findViewById(R.id.monkey_misti_logo);
		mistiLogo.setMaxWidth((int)((double)screenWidth * MonkeyDialog.LOGO_WIDTH_TO_SCREEN_RATIO));
		
		doneButton = (Button) view.findViewById(R.id.monkey_done_button);
		doneButton.setTypeface(font.getCurrentFont());
		doneButton.setTextSize(font.getFontSize());
		doneButton.setBackgroundResource(theme.getGeneralButton());
		doneButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View arg0) 
	{
		this.dismiss();
		
	}
}
