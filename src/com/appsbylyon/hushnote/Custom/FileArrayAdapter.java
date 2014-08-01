package com.appsbylyon.hushnote.Custom;

import java.util.List;

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
 * Custom Array adapter for file chooser
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 * Base off of code found here:
 * http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/
 *
 */
public class FileArrayAdapter extends ArrayAdapter<Option>{

    private Font font = Font.getInstance();
	
	private Context context;
    private int id;
    private List<Option>items;
    
    private Theme theme = Theme.getInstance();
    
    
    public FileArrayAdapter(Context context, int textViewResourceId,
            List<Option> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        id = textViewResourceId;
        items = objects;
    }
    
    public Option getItem(int i)
    {
    	return items.get(i);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	View view = convertView;
        if (view == null) 
        {
        	LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	view = vi.inflate(id, null);
        }
        final Option option = items.get(position);
        if (option != null) 
        {
        	LinearLayout background = (LinearLayout) view.findViewById(R.id.file_view_background);
        	background.setBackgroundResource(theme.getSmallBorder());
        	
        	TextView t1 = (TextView) view.findViewById(R.id.file_view_text1);
        	t1.setTypeface(font.getCurrentFont());
        	t1.setTextSize(font.getFontSize()+3);
        	t1.setTextColor(theme.getTextColor());
                       
        	TextView t2 = (TextView) view.findViewById(R.id.file_view_text2);
        	t2.setTypeface(font.getCurrentFont());
            t2.setTextSize(font.getFontSize());
            t2.setTextColor(theme.getTextColor());
            
            if(t1!=null) t1.setText(option.getName());
            if(t2!=null) t2.setText(option.getData());
            
            ImageView image = (ImageView) view.findViewById(R.id.file_view_image);
            if (option.getData().equalsIgnoreCase("parent directory")) 
            {
            	image.setImageDrawable(context.getResources().getDrawable(theme.getArrow()));
            }else if (option.getData().equalsIgnoreCase("folder")) 
            {
                image.setImageDrawable(context.getResources().getDrawable(theme.getFolder()));    	   
            }
            else if (option.getName().length() > 4) 
            {
            	if (option.getName().subSequence(option.getName().length()-4, option.getName().length()).toString().equalsIgnoreCase(".nbk")) 
                {
            		image.setImageDrawable(context.getResources().getDrawable(theme.getCheck()));
            		t2.setText("Importable Notebook File");
                }
            	else 
                {
                	image.setImageDrawable(context.getResources().getDrawable(theme.getX()));
                	t2.setText("Unimportable File");
                }
            }
            else 
            {
            	image.setImageDrawable(context.getResources().getDrawable(theme.getX()));
            }
            
                       
        }
        return view;
	}

}

