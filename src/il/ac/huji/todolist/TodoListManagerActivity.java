package il.ac.huji.todolist;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


final class Item
{
	private String text;
	private GregorianCalendar date;

	public Item (String text, GregorianCalendar date)
	{
		this.text = text;
		this.date = date;
	}
	
	public String getText ()
	{
		return this.text;
	}
	
	public String getDateString ()
	{
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		return day + "/" + month + "/" + year;
	}
	
	public GregorianCalendar getDate ()
	{
		return this.date;
	}
}

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

public class TodoListManagerActivity extends Activity 
{
	
	protected ArrayList<Item> items = new ArrayList<Item>();
	protected RedBlueAdapter adapter;
	protected int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        adapter = new RedBlueAdapter( 
        		getApplicationContext(),
        		R.id.lstTodoItems,
        		items);
        ListView list = (ListView)findViewById(R.id.lstTodoItems);
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        list.setOnItemLongClickListener(new OnItemLongClickListener() 
        {
        	@Override
            public boolean onItemLongClick ( 
            		AdapterView<?> adapterView,
            		View view,
                    int index, long arg3) 
            {
        		selectedIndex = index;
        		String t = ((RedBlueAdapter) adapterView.getAdapter()).getItemText(index);
        		builder.setTitle( t );
        		final CharSequence dItems[] = { getString(R.string.delete), t };
        		builder.setItems( dItems, 
        				new DialogInterface.OnClickListener() 
        				{
							public void onClick(DialogInterface dialog, int dItem) 
							{
								// User clicked an item
								if (dItem == 0)
								{
									items.remove(selectedIndex);
									adapter.notifyDataSetChanged();
								}
								else 
								{
									if (dItem == 1)
									{
										//call
									}
								}
								
							}
        				});
        		
        		/*
        		builder.setPositiveButton(
        				R.string.ok, 
        				new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int id) 
        					{
        						// User clicked OK button
        						items.remove(selectedIndex);
        						adapter.notifyDataSetChanged();
        					}
        				});
        				
        		*/		
        		AlertDialog dialog = builder.create();
        		dialog.show();
            	return true;
            }
        });
        
        list.setAdapter(adapter);
        
    }
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) 
    {
        if (requestCode == 1) 
        {
            if (resultCode == RESULT_OK) 
            {
            	if (intent != null)
    	    	{
    	    		Bundle extras = intent.getExtras();
    		    	if (extras != null) 
    		    	{
    		    	    String newItemText = extras.getString("Text"); 
    			    	int year = Integer.parseInt(extras.getString("Year"));
    			    	int month = Integer.parseInt(extras.getString("Month"));
    			    	int day = Integer.parseInt(extras.getString("Day"));
    			    	GregorianCalendar date = new GregorianCalendar( year, month, day );
    			    	
    			    	adapter.add(new Item (newItemText,date));
    			    }
    	    	}
                
            }
        }
    }
    /*
    protected void onResume()
    {
    	super.onResume();
    	try
    	{
    		Intent intent = getIntent();
	    	if (intent != null)
	    	{
	    		Bundle extras = intent.getExtras();
		    	if (extras != null) 
		    	{
		    	    String newItemText = extras.getString("Text"); 
			    	int year = Integer.parseInt(extras.getString("Year"));
			    	int month = Integer.parseInt(extras.getString("Month"));
			    	int day = Integer.parseInt(extras.getString("Day"));
			    	GregorianCalendar date = new GregorianCalendar( year, month, day );
			    	
			    	adapter.add(new Item (newItemText,date));
			    }
	    	}
    		
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list_manager, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected ( MenuItem item )
    {
    	switch ( item.getItemId() )
    	{
    	case R.id.action_add_item :
    		
    		Intent intent = new Intent(this, AddNewTodoItemActivity.class); 
    		startActivityForResult(intent, 1); 
    		
    		
    		/*
    		EditText editText = (EditText) findViewById(R.id.edtNewItem);
        	String textEntered = editText.getText().toString();
        	if (textEntered.length() > 0)
        	{
        		adapter.add(textEntered);
        		editText.setText("");
        	}
    		*/
    		break;
    	}
    	return true;
    }
    
}
