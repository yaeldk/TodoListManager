package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.GregorianCalendar;

final class Item
{
	private String text;
	private GregorianCalendar date;
	private int id;
	private String ParseId;

	public Item (String text, GregorianCalendar date)
	{
		this.text = text;
		this.date = date;
	}
	
	public Item(String text, long dateInMilliSeconds) 
	{
		this.text = text;
		this.date = new GregorianCalendar();
		date.setTimeInMillis(dateInMilliSeconds);
	}
	
	public Item(int id, String text, long dateInMilliSeconds) 
	{
		this.id = id;
		this.text = text;
		this.date = new GregorianCalendar();
		date.setTimeInMillis(dateInMilliSeconds);
	}
	
	public void setParseId (String id)
	{
		this.ParseId = id;
	}
	
	public String getParseId ()
	{
		return this.ParseId;
	}

	public int getId()
	{
		return this.id;
	}
	
	public void setId( int id )
	{
		this.id = id;
	}
	public String getText ()
	{
		return this.text;
	}
	
	public String getDateString ()
	{
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		return day + "/" + month + "/" + year;
	}
	
	public GregorianCalendar getDate ()
	{
		return this.date;
	}
}
