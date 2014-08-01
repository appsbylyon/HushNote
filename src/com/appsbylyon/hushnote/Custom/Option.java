package com.appsbylyon.hushnote.Custom;

/**
 * Custom Class to work with file chooser
 * 
 * Modified: 7/17/2014
 * 
 * @author H3R3T1C
 * This code came from the following and is entirely unmodified (except for formatting)
 * http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/
 *
 */
public class Option implements Comparable<Option>{
    private String name;
    private String data;
    private String path;
    
    public Option(String n,String d,String p)
    {
        name = n;
        data = d;
        path = p;
    }
    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getPath()
    {
        return path;
    }
    @Override
    public int compareTo(Option o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
        else 
            throw new IllegalArgumentException();
    }
}// End of Option class


