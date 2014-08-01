package com.appsbylyon.hushnote.Custom;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Custom array adapter for a custom context menu
 * 
 * Modified: 7/23/2014
 * 
 * @author Adam Lyon
 *
 */
public class ContextMenuArrayAdapter extends ArrayAdapter<ContextMenuItem> 
{
private final Context context;
	
	private ArrayList<ContextMenuItem> values;
	
	private Font font = Font.getInstance();
	
	private Theme theme = Theme.getInstance();
	
	public ContextMenuArrayAdapter(Context context, ArrayList<ContextMenuItem> values) 
	{
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		ImageView image = null;
		TextView text = null;
		ContextMenuItem item =  (ContextMenuItem) values.get(position);
		
		if (item.hasIcon()) 
		{
			rowView = inflater.inflate(R.layout.context_row_view, parent, false);
			image = (ImageView) rowView.findViewById(R.id.context_view_image);
			image.setBackgroundResource(item.getIconResourceId());
			text = (TextView) rowView.findViewById(R.id.context_view_text);
		}
		else 
		{
			rowView = inflater.inflate(R.layout.context_row_view_no_icon, parent, false);
			text = (TextView) rowView.findViewById(R.id.context_view_text);
		}
		
		
		LinearLayout background = (LinearLayout) rowView.findViewById(R.id.context_row_view_background);
		background.setBackgroundResource(theme.getSmallBorder());
		
		text.setText(item.getItemText());
		text.setTypeface(font.getCurrentFont());
		text.setTextSize(font.getFontSize());
		text.setTextColor(theme.getTextColor());
		
		
		
		return rowView;
	}
	
}// end of ContextMenuArrayAdapter class
