package il.ac.huji.todolist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoListDatabase 
{
	  
	// Database fields
	  private SQLiteDatabase database;
	  private TodoListSQLiteHelper dbHelper;
	  private String[] allColumns = { TodoListSQLiteHelper.COLUMN_ID,
	      TodoListSQLiteHelper.COLUMN_TITLE, TodoListSQLiteHelper.COLUMN_DUE };

	  public TodoListDatabase(Context context) 
	  {
	    dbHelper = new TodoListSQLiteHelper(context);
	  }

	  public void open() throws SQLException 
	  {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() 
	  {
	    dbHelper.close();
	  }

	  public Item createItem ( String title, long due ) 
	  {
	    ContentValues values = new ContentValues();
	    values.put(TodoListSQLiteHelper.COLUMN_TITLE, title);
	    values.put(TodoListSQLiteHelper.COLUMN_DUE, due);
	    long insertId = database.insert(TodoListSQLiteHelper.TABLE_TODO, null, values);
	    Cursor cursor = database.query(TodoListSQLiteHelper.TABLE_TODO,
	        allColumns, TodoListSQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Item newItem = cursorToItem(cursor);
	    cursor.close();
	    
		
	    return newItem;
	  }

	  public void deleteItem(Item item) 
	  {
	    long id = item.getId();
	    //System.out.println("Item deleted with id: " + id);
	    database.delete(TodoListSQLiteHelper.TABLE_TODO, TodoListSQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }
	  
	  public Cursor getCursor ()
	  {
		  return database.query(TodoListSQLiteHelper.TABLE_TODO,
				  allColumns, null, null, null, null, null);
	  }
	  
	  // UNUSED since v4
	  public ArrayList<Item> getAllItems() 
	  {
		  ArrayList<Item> items = new ArrayList<Item>();

		  Cursor cursor = database.query(TodoListSQLiteHelper.TABLE_TODO,
				  allColumns, null, null, null, null, null);

		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) 
		  {
			  Item item = cursorToItem(cursor);
			  items.add(item);
			  cursor.moveToNext();
		  }
		  // make sure to close the cursor
		  cursor.close();
		  return items;
	  }

	  public Item cursorToItem(Cursor cursor) {
	    Item item = new Item((int) cursor.getLong(0), cursor.getString(1), cursor.getLong(2));
	    return item;
	  }
}
