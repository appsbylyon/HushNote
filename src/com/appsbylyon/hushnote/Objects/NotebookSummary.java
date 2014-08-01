package com.appsbylyon.hushnote.Objects;

import java.io.Serializable;

/**
 * Class to hold the notebook summary information
 * 
 * Modified: 7/9/2014
 * 
 * @author Adam Lyon
 *
 */
public class NotebookSummary implements Serializable 
{
	private static final long serialVersionUID = 23422231L;
	
	private String title = "";
	private String description = "";
	private boolean isEncrypted = false;
	
	public NotebookSummary(String title, String description, boolean isEncrypted) 
	{
		this.setTitle(title);
		this.setDescription(description);
		this.setEncrypted(isEncrypted);
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public boolean isEncrypted() 
	{
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) 
	{
		this.isEncrypted = isEncrypted;
	}
}// End of NotebookSummary class