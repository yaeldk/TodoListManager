package il.ac.huji.todolist;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TodoListManagerActivity extends Activity {

	private static final String ITEM_CLASS  = "Item";
	private static final String ITEM_TITLE = "title";
	private static final String ITEM_DUE_DATE = "dueDate";
	private static final String ITEM_ID = "id";
	private static final String ITEM_USER = "user";
	
	protected ArrayList<Item> items;
	protected RedBlueAdapter adapter;
	protected int selectedIndex;
	private TodoListDatabase datasource;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		if (android.os.Build.VERSION.SDK_INT > 9) 
		{
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				if (intent != null) {
					Bundle extras = intent.getExtras();
					if (extras != null) {
						String newItemText = extras.getString("title");
						long dueDateLong = extras.getLong("dueDate");

						Item item = datasource.createItem(newItemText,
								dueDateLong);
						adapter.add(item);
						
						ParseObject itemObject = new ParseObject(ITEM_CLASS);
						itemObject.put(ITEM_TITLE, newItemText);
						itemObject.put(ITEM_DUE_DATE, dueDateLong);
						itemObject.put(ITEM_ID, item.getId());
						itemObject.put(ITEM_USER, ParseUser.getCurrentUser());
						itemObject.setACL(new ParseACL(ParseUser.getCurrentUser()));
						itemObject.saveInBackground();

						
					}
				}

			}
		}
	}

	protected void onResume() {
		
		//datasource.open();
		super.onResume();
	}
	
	protected void onPause() {
		//datasource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_item:

			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, 1);

			/*
			 * EditText editText = (EditText) findViewById(R.id.edtNewItem);
			 * String textEntered = editText.getText().toString(); if
			 * (textEntered.length() > 0) { adapter.add(textEntered);
			 * editText.setText(""); }
			 */
			break;
		}
		return true;
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		datasource.close();
	}
	
	public void onStart()
	{
		super.onStart();
		try
		{
			Parse.initialize(getApplicationContext(), "fQ9puXIBaq6RT4rSiLObJI1kDJLdt8eK3cLumQxj", 
				"zZgq4IEKYHNQj9O3hSMnCkaxOlfFirqXuyEihzaQ");
		}
		catch (Exception e)
		{
			Log.d("ini", "-----------");
		}
		
		ParseAnonymousUtils.logIn(new LogInCallback() {
			  @Override
			  public void done(ParseUser user, com.parse.ParseException e) {
			    if (e != null) {
			      Log.d("MyApp", "Anonymous login failed.");
			    } else {
			      Log.d("MyApp", "Anonymous user logged in.");
			    }
			  }
			});
	
		datasource = new TodoListDatabase(getApplicationContext());
		datasource.open();
		items = datasource.getAllItems();

		adapter = new RedBlueAdapter(getApplicationContext(),
				R.id.lstTodoItems, items);
		ListView list = (ListView) findViewById(R.id.lstTodoItems);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int index, long arg3) {
				selectedIndex = index;
				String t = ((RedBlueAdapter) adapterView.getAdapter())
						.getItemText(index);
				builder.setTitle(t);

				CharSequence dItems[] = null;
				dItems[0] = getString(R.string.delete);
				
				if (t.indexOf("Call ") == 0) 
				{
				
					dItems[1] = t ;
	
				}
				
				builder.setItems(dItems, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int dItem) {
						// User clicked a delete item
						if (dItem == 0) {
							Item item = items.get(selectedIndex);
							datasource.deleteItem(item);
																
							ParseQuery<ParseObject> query = ParseQuery.getQuery(ITEM_CLASS);
							query.whereEqualTo(ITEM_ID, item.getId());
							query.getFirstInBackground(new GetCallback<ParseObject>() {
							  public void done(ParseObject object, com.parse.ParseException e) {
							    if (object == null) 
							    {
							    } else {
							      object.deleteInBackground();
							    }
							  }
							});
							
							items.remove(selectedIndex);
							adapter.notifyDataSetChanged();
							
							
							
						} 
						else 
						{
							if (dItem == 1) {
								
							}
						}

					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();
				return true;
			}
		});

		list.setAdapter(adapter);

	}

}
