package com.appsbylyon.hushnote.fragments;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Custom.ContextMenuArrayAdapter;
import com.appsbylyon.hushnote.Custom.ContextMenuItem;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for the custom context menu
 * 
 * Modified: 7/23/2014
 * 
 * @author Adam Lyon
 *
 */
public class CustomContextMenu extends DialogFragment 
{
	public interface ContextMenuListener 
	{
		public void contextItemSelected(int contextOption);
	}
	
	private ContextMenuListener activity;
	
	private Theme theme = Theme.getInstance();
	
	private Font font = Font.getInstance();
	
	private LinearLayout background;
	
	private TextView header;
	
	private ListView itemList;
	
	private ArrayList<ContextMenuItem> menuItems = new ArrayList<ContextMenuItem>();
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.context_menu_fragment, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		activity = (ContextMenuListener) this.getActivity();
		
		background = (LinearLayout) view.findViewById(R.id.context_menu_fragment);
		background.setBackgroundResource(theme.getLargeBorder());
		
		header = (TextView) view.findViewById(R.id.context_menu_header);
		header.setTypeface(font.getCurrentFont());
		header.setTextSize(font.getFontSize()+5);
		header.setTextColor(theme.getTextColor());
		
		itemList = (ListView) view.findViewById(R.id.context_menu_list);
		
		ContextMenuArrayAdapter adapter = new ContextMenuArrayAdapter(getActivity(), menuItems);
		itemList.setAdapter(adapter);
		
		itemList.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				activity.contextItemSelected(position);
				CustomContextMenu.this.dismiss();
			}
			
		});
		
		return view;
	}
	
	public void setAdapter() 
	{
		ContextMenuArrayAdapter adapter = new ContextMenuArrayAdapter(getActivity(), menuItems);
		itemList.setAdapter(adapter);
	}
	
	public void addMenuItems(ContextMenuItem item) 
	{
		menuItems.add(item);
	}
}// end of ContextMenu class
