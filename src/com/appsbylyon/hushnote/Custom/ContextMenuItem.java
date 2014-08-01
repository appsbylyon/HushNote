package com.appsbylyon.hushnote.Custom;

/**
 * Class for the items in the custom context menu
 * 
 * Modified: 7/24/2014
 * 
 * @author Adam Lyon
 *
 */
public class ContextMenuItem 
{
	private int iconResourceId;
	
	private String itemText;
	
	private boolean hasIcon = false;
	
	public ContextMenuItem(String itemText) 
	{
		this.setItemText(itemText);
		this.setHasIcon(false);
	}
	
	public ContextMenuItem(String itemText, int iconResourceId) 
	{
		this.setItemText(itemText);
		this.setIconResourceId(iconResourceId);
		this.setHasIcon(true);
	}

	public int getIconResourceId() 
	{
		return iconResourceId;
	}

	public void setIconResourceId(int iconResourceId) 
	{
		this.iconResourceId = iconResourceId;
	}

	public String getItemText() 
	{
		return itemText;
	}

	public void setItemText(String itemText) 
	{
		this.itemText = itemText;
	}

	public boolean hasIcon() 
	{
		return hasIcon;
	}

	public void setHasIcon(boolean hasIcon) 
	{
		this.hasIcon = hasIcon;
	}
}// End of ContextMenuItem class