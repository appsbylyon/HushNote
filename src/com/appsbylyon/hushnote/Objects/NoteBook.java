package com.appsbylyon.hushnote.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import com.appsbylyon.hushnote.Custom.SerTime;

/**
 * Class representing a Notebook
 * 
 * Modified: 7/16/2014
 * 
 * @author Adam Lyon
 *
 */
public class NoteBook implements Serializable
{
	private static final long serialVersionUID = -3479641618152896927L;
	
	private static final String TIME_FORMAT = "%m/%d/%Y %H:%M";
	
	private SerTime time = new SerTime();
	
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	
	private long dateCreated = 0;
	private long dateModified = 0;
	
	private String title = "";
	private String description = "";
	private String password = "";
	
	private boolean isEncrypted = false;
	
	public NoteBook() 
	{
		setDateCreated();
		setDateModified();
	}
	
	public NoteBook(boolean isNew) {};
	
	public NoteBook(String title, String description) 
	{
		setDateCreated();
		setDateModified();
		setTitle(title);
		setDescription(description);
	}
	
	public NoteBook(String title, String description, boolean isEncrypted, String password) 
	{
		setDateCreated();
		setDateModified();
		setTitle(title);
		setDescription(description);
		setEncrypted(isEncrypted);
		setPassword(password);
	}

	public boolean hasEntries() 
	{
		if (entries.size() > 0) 
		{
			return true;
		}
		return false;
	}
	
	public long getDateCreated() 
	{
		return dateCreated;
	}

	private void setDateCreated() 
	{
		this.dateCreated = System.currentTimeMillis();
	}

	public long getDateModified() 
	{
		return dateModified;
	}

	private void setDateModified() 
	{
		this.dateModified = System.currentTimeMillis();
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
    
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String getStringDateCreated() 
	{
		time.set(this.dateCreated);
		return time.format(TIME_FORMAT);
	}
	
	public String getStringDateModified() 
	{
		time.set(this.dateModified);
		return time.format(TIME_FORMAT);
	}
	
	public void addEntry(Entry newEntry) 
	{
		this.entries.add(newEntry);
	}
	
	public Entry getEntry(int location) 
	{
		return (Entry) entries.get(location);
	}
	
	public ArrayList<Entry> getAllEntries() 
	{
		return entries;
	}
	
	public void removeEntry(int location) 
	{
		this.entries.remove(location);
	}
	
	public void removeEntry(Entry entry) 
	{
		this.entries.remove(entry);
	}
	
	public boolean checkPassword(String password) 
	{
		if (password.compareTo(this.password) == 0) 
		{
			return true;
		}
		return false;
	}
	
	public void setDateCreated(Long newDate) 
	{
		this.dateCreated = newDate;
	}
	
        public String getPassword() 
        {
            return password;
        }
        
	public void setDateModified(long newDate) 
	{
		this.dateModified = newDate;
	}
	
	public void replaceEntry(Entry newEntry, int position) 
	{
		entries.set(position, newEntry);
	}
	
        public void printNotebook() 
        {
            System.out.println();
            System.out.println("Printing Notebook");
            System.out.println();
            System.out.println("Date Created: "+this.getStringDateCreated());
            System.out.println("Date Modified: "+this.getStringDateModified());
            System.out.println("Title: "+getTitle());
            System.out.println("Description: "+getDescription());
            System.out.println("Password: "+this.password);
            System.out.println();
            System.out.println("Entries");
            for (Entry tempEntry : entries) 
            {
                System.out.println();
                System.out.println("Date Created: "+tempEntry.getDateCreated());
                System.out.println("Date Modified: "+tempEntry.getDateModified());
                System.out.println("Title: "+tempEntry.getTitle());
                System.out.println("Text: "+tempEntry.getText());
            }
        }
}//End of NoteBook Class
