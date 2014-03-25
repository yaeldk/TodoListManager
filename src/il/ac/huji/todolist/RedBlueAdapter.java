package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

final class RedBlueAdapter extends ArrayAdapter<Item>
{
	
	private ArrayList<Item> items;
	
	/* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<String> items,
	* because it is the list of objects we want to display.
	*/
	public RedBlueAdapter(Context context, int textViewResourceId, ArrayList<Item> items ) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	public String getItemText (int index)
	{
		return items.get(index).getText();
	}
	
	public String getItemDateString (int index)
	{
		return items.get(index).getDateString();
	}
	
	public GregorianCalendar getItemDate (int index)
	{
		return items.get(index).getDate();
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v;

		// we have to inflate view.
		// to inflate it basically means to render, or show, the view.
		LayoutInflater inflater = 
				(LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		
		String i, d;
		GregorianCalendar date = this.getItemDate(position);
		if (date != null)
		{
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH);
			int day = date.get(Calendar.DAY_OF_MONTH);
					
			GregorianCalendar cal = (GregorianCalendar) (new GregorianCalendar()).getInstance();
			int curYear = cal.get(Calendar.YEAR);
			int curMonth = cal.get(Calendar.MONTH);
			int curDay = cal.get(Calendar.DAY_OF_MONTH);
			
			if ( ( year < curYear ) || ( year == curYear && month < curMonth ) || 
					( year == curYear && month == curMonth && day < curDay ) )
			{
				v = inflater.inflate( R.layout.red_item, null);
			}
			else
			{
				v = inflater.inflate(R.layout.blue_item, null);
			}
			d = this.getItemDateString(position);
			
		}
		else
		{
			v = inflater.inflate(R.layout.blue_item, null);
			d = Resources.getSystem().getString ( R.string.no_due_date );
		}
		
		i = this.getItemText(position);
				
		if (i != null && d != null) 
		{
			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView tvText = (TextView) v.findViewById(R.id.txtTodoTitle);
			TextView tvDate = (TextView) v.findViewById(R.id.txtTodoDueDate);
			
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (tvText != null)
			{
				tvText.setText(i);
			}
			if (tvDate != null)
			{
				tvDate.setText(d);
			}
		}

		// the view must be returned to our activity
		return v;

	}
}
