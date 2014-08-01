package com.appsbylyon.hushnote.Custom;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Objects.NotebookSummary;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Class for the custom listview in the Library view.
 * 
 * Modified: 7/10/2014
 * 
 * @author Adam Lyon
 *
 */
public class LibraryViewAdapter extends ArrayAdapter<NotebookSummary> 
{
	private final Context context;
	
	private ArrayList<NotebookSummary> values;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	public LibraryViewAdapter(Context context, ArrayList<NotebookSummary> values) 
	{
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.library_view_list_layout, parent, false);
		RelativeLayout background = (RelativeLayout) rowView.findViewById(R.id.library_view_list);
		TextView title = (TextView) rowView.findViewById(R.id.library_view_notebook_title);
		TextView desc = (TextView) rowView.findViewById(R.id.library_view_notebook_desc);
		ImageView lock = (ImageView) rowView.findViewById(R.id.library_view_lock_image);
		
		
		background.setBackgroundResource(theme.getSmallBorder());
		
		NotebookSummary currSummary = (NotebookSummary) values.get(position);
		title.setText(currSummary.getTitle());
		title.setTypeface(font.getCurrentFont());
		title.setTextSize((float) font.getFontSize());
		title.setTextColor(theme.getTextColor());
		
		desc.setText(currSummary.getDescription());
		desc.setTypeface(font.getCurrentFont());
		desc.setTextSize((float) font.getFontSize()-4);
		desc.setTextColor(theme.getTextColor());
		
		if (currSummary.isEncrypted()) 
		{
			lock.setImageResource(theme.getLocked());
		}
		else 
		{
			lock.setImageResource(theme.getUnlocked());
		}
		
		
		return rowView;
	}
}// end of LibraryViewAdapter class
