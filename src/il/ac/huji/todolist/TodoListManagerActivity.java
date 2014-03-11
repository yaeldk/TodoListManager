package il.ac.huji.todolist;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


final class RedBlueAdapter extends ArrayAdapter<String>
{
	
	
	public RedBlueAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId );
		// TODO Auto-generated constructor stub
	}
	
	private ArrayList<String> items;
	
	/* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<String> items,
	* because it is the list of objects we want to display.
	*/
	public RedBlueAdapter(Context context, int textViewResourceId, ArrayList<String> items ) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	public String getItemText (int index)
	{
		return items.get(index);
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
		
		if ( position % 2 == 0 ) 
		{
			v = inflater.inflate( R.layout.red_item, null);
		}
		else
		{
			v = inflater.inflate(R.layout.blue_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current String.
		 */
		String i = items.get(position);

		if (i != null) 
		{

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView tv = (TextView) v.findViewById(R.id.lstTodoItem);
			
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (tv != null){
				tv.setText(i);
			}
		}

		// the view must be returned to our activity
		return v;

	}
}

public class TodoListManagerActivity extends Activity 
{
	
	protected ArrayList<String> items = new ArrayList<String>();
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
        		builder.setTitle( t + " " + getString(R.string.delete_title));
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
        		AlertDialog dialog = builder.create();
        		dialog.show();
            	return true;
            }
        });
        
        list.setAdapter(adapter);
        
    }


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
    		
    		EditText editText = (EditText) findViewById(R.id.edtNewItem);
        	String textEntered = editText.getText().toString();
        	if (textEntered.length() > 0)
        	{
        		adapter.add(textEntered);
        		editText.setText("");
        	}
    		
    		break;
    	}
    	return true;
    }
    
}
