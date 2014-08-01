package com.appsbylyon.hushnote.Theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.appsbylyon.hushnote.R;

/**
 * Theme Singleton
 * 
 * Modified: 7/25/2014
 * 
 * @author Adam Lyon
 *
 */
public class Theme 
{
	public static final int BLUE_THEME = 0;
	public static final int GREEN_THEME = 1;
	public static final int METAL_THEME = 2;
	public static final int PINK_THEME = 3;
	public static final int ORANGE_THEME = 4;
	public static final int LOVE_THEME = 5;
	public static final int SKY_THEME = 6;
	public static final int EARTH_THEME = 7;
	public static final int FIRE_THEME = 8;
	public static final int SEA_THEME = 9;
	
	private static final Theme instance = new Theme();
	
	private Map<Integer, String> nameMap;
	
	private int addButton;
	private int unlocked;
	private int locked;
	private int largeBorder;
	private int smallBorder;
	private int generalButton;
	private int folder;
	private int upArrow;
	private int check;
	private int x;
	private int textColor;
	private int edit;
	private int export;
	private int trash;
	private int currTheme;
	
	@SuppressLint("UseSparseArrays")
	private Theme()
	{
		setTheme(METAL_THEME);
		nameMap = new HashMap<Integer, String>();
		nameMap.put(BLUE_THEME, "Blue");
		nameMap.put(GREEN_THEME, "Green");
		nameMap.put(METAL_THEME, "Metal");
		nameMap.put(PINK_THEME, "Pink");
		nameMap.put(ORANGE_THEME, "Orange");
		nameMap.put(LOVE_THEME, "Love");
		nameMap.put(SKY_THEME, "Sky");
		nameMap.put(EARTH_THEME, "Earth");
		nameMap.put(FIRE_THEME, "Fire");
		nameMap.put(SEA_THEME, "Sea");
	}
	
	public static Theme getInstance() 
	{
		return instance;
	}
	
	public ArrayList<String> getColorList() 
	{
		ArrayList<String> colorList = new ArrayList<String>();
		for (int i = 0; i < nameMap.size(); i++) 
		{
			colorList.add(nameMap.get(i));
		}
		return colorList;
	}
	
	public String getThemeName(int themeValue) 
	{
		return nameMap.get(themeValue);
	}
	
	public void setTheme(int theme) 
	{
		currTheme = theme;
		switch (theme) 
		{
		case BLUE_THEME:
			addButton = R.drawable.blue_add_button;
			unlocked = R.drawable.blue_unlocked;
			locked = R.drawable.blue_locked;
			largeBorder = R.drawable.blue_big_border9;
			smallBorder = R.drawable.blue_list_background;
			generalButton = R.drawable.blue_gen_button;
			folder = R.drawable.blue_folder;
			upArrow = R.drawable.blue_arrow;
			check = R.drawable.blue_check;
			x = R.drawable.blue_x;
			edit = R.drawable.blue_edit;
			export = R.drawable.blue_export;
			trash = R.drawable.blue_trash_can;
			textColor = Color.WHITE;
			break;
		case GREEN_THEME:
			addButton = R.drawable.green_add_button;
			unlocked = R.drawable.green_unlocked;
			locked = R.drawable.green_locked;
			largeBorder = R.drawable.green_big_border9;
			smallBorder = R.drawable.green_list_background;
			generalButton = R.drawable.green_gen_button;
			folder = R.drawable.green_folder;
			upArrow = R.drawable.green_arrow;
			check = R.drawable.green_check;
			x = R.drawable.green_x;
			edit = R.drawable.green_edit;
			export = R.drawable.green_export;
			trash = R.drawable.green_trash_can;
			textColor = Color.WHITE;
			break;
		case METAL_THEME:
			addButton = R.drawable.metal_add_button;
			unlocked = R.drawable.metal_unlocked;
			locked = R.drawable.metal_locked;
			largeBorder = R.drawable.metal_big_border9;
			smallBorder = R.drawable.metal_list_background;
			generalButton = R.drawable.metal_gen_button;
			folder = R.drawable.metal_folder;
			upArrow = R.drawable.metal_arrow;
			check = R.drawable.metal_check;
			x = R.drawable.metal_x;
			edit = R.drawable.metal_edit;
			export = R.drawable.metal_export;
			trash = R.drawable.metal_trash_can;
			textColor = Color.WHITE;
			break;
		case PINK_THEME:
			addButton = R.drawable.pink_add_button;
			unlocked = R.drawable.pink_unlocked;
			locked = R.drawable.pink_locked;
			largeBorder = R.drawable.pink_big_border9;
			smallBorder = R.drawable.pink_list_background;
			generalButton = R.drawable.pink_gen_button;
			folder = R.drawable.pink_folder;
			upArrow = R.drawable.pink_arrow;
			check = R.drawable.pink_check;
			x = R.drawable.pink_x;
			edit = R.drawable.pink_edit;
			export = R.drawable.pink_export;
			trash = R.drawable.pink_trash_can;
			textColor = Color.WHITE;
			break;
		case ORANGE_THEME:
			addButton = R.drawable.orange_add_button;
			unlocked = R.drawable.orange_unlocked;
			locked = R.drawable.orange_locked;
			largeBorder = R.drawable.orange_big_border9;
			smallBorder = R.drawable.orange_list_background;
			generalButton = R.drawable.orange_gen_button;
			folder = R.drawable.orange_folder;
			upArrow = R.drawable.orange_arrow;
			check = R.drawable.orange_check;
			x = R.drawable.orange_x;
			edit = R.drawable.orange_edit;
			export = R.drawable.orange_export;
			trash = R.drawable.orange_trash_can;
			textColor = Color.WHITE;
			break;
		case LOVE_THEME:
			addButton = R.drawable.love_add_button;
			unlocked = R.drawable.love_unlocked;
			locked = R.drawable.love_locked;
			largeBorder = R.drawable.love_big_border9;
			smallBorder = R.drawable.love_list_background;
			generalButton = R.drawable.love_gen_button;
			folder = R.drawable.love_folder;
			upArrow = R.drawable.love_arrow;
			check = R.drawable.love_check;
			x = R.drawable.love_x;
			edit = R.drawable.love_edit;
			export = R.drawable.love_export;
			trash = R.drawable.love_trash_can;
			textColor = Color.WHITE;
			break;
		case SKY_THEME:
			addButton = R.drawable.sky_add_button;
			unlocked = R.drawable.sky_unlocked;
			locked = R.drawable.sky_locked;
			largeBorder = R.drawable.sky_big_border9;
			smallBorder = R.drawable.sky_list_background;
			generalButton = R.drawable.sky_gen_button;
			folder = R.drawable.sky_folder;
			upArrow = R.drawable.sky_arrow;
			check = R.drawable.sky_check;
			x = R.drawable.sky_x;
			edit = R.drawable.sky_edit;
			export = R.drawable.sky_export;
			trash = R.drawable.sky_trash_can;
			textColor = Color.WHITE;
			break;
		case EARTH_THEME:
			addButton = R.drawable.earth_add_button;
			unlocked = R.drawable.earth_unlocked;
			locked = R.drawable.earth_locked;
			largeBorder = R.drawable.earth_big_border9;
			smallBorder = R.drawable.earth_list_background;
			generalButton = R.drawable.earth_gen_button;
			folder = R.drawable.earth_folder;
			upArrow = R.drawable.earth_arrow;
			check = R.drawable.earth_check;
			x = R.drawable.earth_x;
			edit = R.drawable.earth_edit;
			export = R.drawable.earth_export;
			trash = R.drawable.earth_trash_can;
			textColor = Color.WHITE;
			break;
		case FIRE_THEME:
			addButton = R.drawable.fire_add_button;
			unlocked = R.drawable.fire_unlocked;
			locked = R.drawable.fire_locked;
			largeBorder = R.drawable.fire_big_border9;
			smallBorder = R.drawable.fire_list_background;
			generalButton = R.drawable.fire_gen_button;
			folder = R.drawable.fire_folder;
			upArrow = R.drawable.fire_arrow;
			check = R.drawable.fire_check;
			x = R.drawable.fire_x;
			edit = R.drawable.fire_edit;
			export = R.drawable.fire_export;
			trash = R.drawable.fire_trash_can;
			textColor = Color.WHITE;
			break;
		case SEA_THEME:
			addButton = R.drawable.sea_add_button;
			unlocked = R.drawable.sea_unlocked;
			locked = R.drawable.sea_locked;
			largeBorder = R.drawable.sea_big_border9;
			smallBorder = R.drawable.sea_list_background;
			generalButton = R.drawable.sea_gen_button;
			folder = R.drawable.sea_folder;
			upArrow = R.drawable.sea_arrow;
			check = R.drawable.sea_check;
			x = R.drawable.sea_x;
			edit = R.drawable.sea_edit;
			export = R.drawable.sea_export;
			trash = R.drawable.sea_trash_can;
			textColor = Color.WHITE;
			break;
		}
	}
	
	public int getCurrTheme() 
	{
		return currTheme;
	}
	
	public int getAddButton() 
	{
		return this.addButton;
	}
	
	public int getUnlocked() 
	{
		return this.unlocked;
	}
	
	public int getLocked() 
	{
		return this.locked;
	}
	
	public int getLargeBorder() 
	{
		return this.largeBorder;
	}
	
	public int getSmallBorder() 
	{
		return this.smallBorder;
	}
	
	public int getGeneralButton() 
	{
		return this.generalButton;
	}
	
	public int getFolder() 
	{
		return this.folder;
	}
	
	public int getArrow() 
	{
		return this.upArrow;
	}
	
	public int getCheck() 
	{
		return this.check;
	}
	
	public int getX() 
	{
		return this.x;
	}
	
	public int getTextColor() 
	{
		return this.textColor;
	}
	
	public int getEdit() 
	{
		return this.edit;
	}
	
	public int getExport() 
	{
		return this.export;
	}
	
	public int getTrash() 
	{
		return this.trash;
	}
}// End of Theme class
