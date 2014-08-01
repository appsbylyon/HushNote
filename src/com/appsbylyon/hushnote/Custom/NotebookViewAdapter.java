package com.appsbylyon.hushnote.Custom;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.Entry;
import com.appsbylyon.hushnote.Theme.Theme;
/**
 * Custom array adapter for the notebook view
 * 
 * Modified: 7/12/2014
 * 
 * @author Adam Lyon
 *
 */
public class NotebookViewAdapter extends ArrayAdapter<Entry> 
{
	private final Context context;
	
	private ArrayList<Entry> values;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	public NotebookViewAdapter(Context context, ArrayList<Entry> values) 
	{
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.notebook_entry_list_layout, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.notebook_listview_entry_title);
		TextView createdDated = (TextView) rowView.findViewById(R.id.notebook_listview_entry_created_date);
		LinearLayout background = (LinearLayout) rowView.findViewById(R.id.notebook_entry_background);
		
		background.setBackgroundResource(theme.getSmallBorder());
		
		Entry currEntry = (Entry) values.get(position);
		title.setText(currEntry.getTitle());
		title.setTypeface(font.getCurrentFont());
		title.setTextSize((float) font.getFontSize());
		title.setTextColor(theme.getTextColor());
		
		createdDated.setText(currEntry.getStringDateCreated());
		createdDated.setTypeface(font.getCurrentFont());
		createdDated.setTextSize((float) font.getFontSize()-4);
		createdDated.setTextColor(theme.getTextColor());
		
		return rowView;
	}
}// End of NotebookViewAdapter class
