package com.appsbylyon.hushnote.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class For Library Information
 * 
 * Modified: 7/24/2014
 * 
 * @author Adam Lyon
 *
 */
public class Library implements Serializable 
{
	private static final long serialVersionUID = 2983479823L;
	
	private ArrayList<NotebookSummary> summaries = new ArrayList<NotebookSummary>();
	
	public void replaceSummary(NotebookSummary summary, int position)  
	{
		summaries.set(position, summary);
	}
	
	public void addSummary(NotebookSummary summary) 
	{
		summaries.add(summary);
	}
	
	public void removeSummary(NotebookSummary summary) 
	{
		summaries.remove(summary);
	}
	
	public void removeSummaryAtPosition(int position) 
	{
		summaries.remove(position);
	}
	
	public int getPositionOfSummary(NotebookSummary summary) 
	{
		return summaries.indexOf(summary);
	}
	
	public NotebookSummary getSummaryAtPosition(int position) 
	{
		return (NotebookSummary) summaries.get(position);
	}
	
	public int getNumberOfSummaries() 
	{
		return summaries.size();
	}
	
	public ArrayList<NotebookSummary> getAllSummaries() 
	{
		return summaries;
	}
}// End of Library class