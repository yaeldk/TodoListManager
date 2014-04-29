package il.ac.huji.todolist;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

public class LoadDatabase extends AsyncTask<Object, Item, Void> 
{
	protected RedBlueAdapter adapter;
	protected ArrayList<Item> items;
	private TodoListDatabase datasource;
	private Context context;
	private ListView listView;
		
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Object... contexts) 
	{
		context = (Context) contexts[0];
		listView = (ListView) contexts[1];
		
		items = new ArrayList<Item>();
		adapter = new RedBlueAdapter(context, R.id.lstTodoItems, items);
		listView.setAdapter(adapter);
		
				
		this.datasource = new TodoListDatabase( context );
		this.datasource.open();
		
		Cursor cursor = this.datasource.getCursor();
		cursor.moveToFirst();
		
		while ( !cursor.isAfterLast() ) 
		{
			Item item = datasource.cursorToItem(cursor);
			cursor.moveToNext();
			
			publishProgress(item);
			
			if (isCancelled())
			{
				break;
			}
			
		}
		// make sure to close the cursor
		cursor.close();
		return null;

	}
	
	@Override
	protected void onProgressUpdate(Item... items) 
	{
		super.onProgressUpdate(items);
		adapter.add(items[0]);
	}
	
	protected void onPostExecute() 
	{
		
	};
	
	public void addItem ( String newItemText, long dueDateLong)
	{
		Item item = datasource.createItem( newItemText, dueDateLong);
		adapter.add(item);
	}
	
	public void deleteItem (int selectedIndex)
	{
		Item item = items.get(selectedIndex);
		datasource.deleteItem(item);
		items.remove(selectedIndex);
		adapter.notifyDataSetChanged();
	}
	
	public String getItemText (int index)
	{
		return items.get(index).getText();
	}
	
	public void close ()
	{
		datasource.close();
	}
}