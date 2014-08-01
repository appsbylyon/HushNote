package com.appsbylyon.hushnote.Activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsbylyon.hushnote.R;
import com.appsbylyon.hushnote.Custom.FileArrayAdapter;
import com.appsbylyon.hushnote.Custom.Option;
import com.appsbylyon.hushnote.Font.Font;
import com.appsbylyon.hushnote.Theme.Theme;

/**
 * Activity for file choosing
 * 
 * Modified: 7/17/2014
 * 
 * @author Adam Lyon
 * Based heavily off of code found here:
 * http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/
 *
 */
public class FileChooser extends Activity implements OnClickListener
{
	private ActionBar actionBar;
	
	private TextView header;
	private TextView locationLabel;
	private ListView fileList;
	private Button cancelButton;
	
    private Font font = Font.getInstance();
    
    private Theme theme = Theme.getInstance();
    
    private LinearLayout background;
	
    private File currentDir;
    private FileArrayAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filechoose_layout);
        
        actionBar = this.getActionBar();
        actionBar.setBackgroundDrawable(this.getResources().getDrawable(theme.getSmallBorder()));
        
        background = (LinearLayout) findViewById(R.id.file_chooser_layout);
        background.setBackgroundResource(theme.getLargeBorder());
        
        header = (TextView) findViewById(R.id.file_chooser_header);
        header.setTypeface(font.getCurrentFont());
        header.setTextSize(font.getFontSize()+5);
        header.setTextColor(theme.getTextColor());
        
        locationLabel = (TextView) findViewById(R.id.file_chooser_location_label);
        locationLabel.setTypeface(font.getCurrentFont());
        locationLabel.setTextSize(font.getFontSize());
        locationLabel.setTextColor(theme.getTextColor());
        
        cancelButton = (Button) findViewById(R.id.file_chooser_cancel_button);
        cancelButton.setTypeface(font.getCurrentFont());
        cancelButton.setTextSize(font.getFontSize());
        cancelButton.setBackgroundResource(theme.getGeneralButton());
        cancelButton.setOnClickListener(this);
        
        fileList = (ListView) findViewById(R.id.file_chooser_listview);
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }
    
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
         locationLabel.setText(f.getPath());
         List<Option>dir = new ArrayList<Option>();
         List<Option>fls = new ArrayList<Option>();
         try
         {
             for(File ff: dirs)
             {
                if(ff.isDirectory())
                    dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                else
                {
                    fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                }
             }
         }catch(Exception e)
         {
             
         }
         Collections.sort(dir);
         Collections.sort(fls);
         dir.addAll(fls);
         if(!f.getName().equalsIgnoreCase("sdcard"))
             dir.add(0,new Option("..","Parent Directory",f.getParent()));
         adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
         fileList.setAdapter(adapter);
         fileList.setOnItemClickListener(new OnItemClickListener() 
         {
        	 @Override
        	 public void onItemClick(AdapterView<?> l, View v, int position, long id) 
        	 {
        	    //public void onItemClick(ListView l, View v, int position, long id) {
        	        // TODO Auto-generated method stub
        	        //super.onItemClick(l, v, position, id);
        	        Option o = adapter.getItem(position);
        	        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
        	                currentDir = new File(o.getPath());
        	                fill(currentDir);
        	        }
        	        else
        	        {
        	            onFileClick(o);
        	        }
        	    }
         });
    }
    
    private void onFileClick(Option o)
    {
    	String name = o.getName();
    	if (name.length() > 4) 
    	{
    		if (o.getName().subSequence(name.length()-4, name.length()).toString().equalsIgnoreCase(".nbk")) 
    		{
    			Intent returnIntent = new Intent();
    	    	returnIntent.putExtra(getString(R.string.file_choose_result_value), o.getPath());
    	    	setResult(RESULT_OK,returnIntent);
    	    	finish();
    		}
    		else 
    		{
    			Toast.makeText(this, "That Is Not A Valid Notebook File!", Toast.LENGTH_SHORT).show();
    		}
    	}
    	
    }

	@Override
	public void onClick(View view) 
	{
		setResult(RESULT_CANCELED);
		finish();
	}
}// End of FileChooser class

