package com.appsbylyon.hushnote.Objects;

import java.io.Serializable;

import com.appsbylyon.hushnote.Custom.SerTime;

/**
 * Object representing a notebook entry.
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 *
 */
public class Entry implements Serializable
{
	private static final long serialVersionUID = 135464L;
	private static final String TIME_FORMAT = "%m/%d/%Y %H:%M";
	
	private String dateCreated = "";
	private String dateModified = "";
	
	private String title = "";
	private String text = "";
	
	private SerTime time = new SerTime();
		
	public Entry()
	{
		setDateCreated();
		setDateModified();
	}
	
	public Entry(boolean isNew) {}
	
	public Entry (String title) 
	{
		setDateCreated();
		this.setTitle(title, true);
	}
	
	public Entry (String title, String text) 
	{
		setDateCreated();
		setDateModified();
		this.setTitle(title, false);
		this.setText(text, true);
	}

	private void setDateCreated() 
	{
		this.dateCreated = Long.toString(System.currentTimeMillis());
	}
	
	public void setDateCreated(String newDate) 
	{
		this.dateCreated = newDate;
	}
	
	public void setDateModified(String newDate) 
	{
		this.dateModified = newDate;
	}
	
	public String getDateCreated() 
	{
		return dateCreated;
	}

	public String getDateModified() 
	{
		return dateModified;
	}

	private void setDateModified() 
	{
		this.dateModified = Long.toString(System.currentTimeMillis());
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title, boolean updateDate) 
	{
		if (updateDate){setDateModified();}
		this.title = title;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text, boolean updateDate) 
	{
		if (updateDate) {setDateModified();}
		this.text = text;
	}
	
	public String getStringDateCreated() 
	{
		time.set(Long.parseLong(this.dateCreated));
		return time.format(TIME_FORMAT);
	}
	
	public String getStringDateModified() 
	{
		time.set(Long.parseLong(this.dateModified));
		return time.format(TIME_FORMAT);
	}
}// End of Entry class