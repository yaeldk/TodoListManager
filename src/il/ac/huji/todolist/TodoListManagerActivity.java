package il.ac.huji.todolist;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

/*
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
*/
public class TodoListManagerActivity extends Activity {

	/*
	private static final String ITEM_CLASS  = "Item";
	private static final String ITEM_TITLE = "title";
	private static final String ITEM_DUE_DATE = "dueDate";
	private static final String ITEM_ID = "id";
	private static final String ITEM_USER = "user";
	*/
	
	protected int selectedIndex;
	private LoadDatabase loadDB;
	//protected RedBlueAdapter adapter;
	//protected ArrayList<Item> items;
	//private TodoListDatabase datasource;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		/*
		if (android.os.Build.VERSION.SDK_INT > 9) 
		{
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		*/
		
	}
	
	public void onStart()
	{
		super.onStart();
		
		// *** Parse integration removed *******************
		/*
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
		
		*/
	
		
		
		/*
		items = new ArrayList<Item>();
		adapter = new RedBlueAdapter(getApplicationContext(),
				R.id.lstTodoItems, items);
		*/
		
		ListView list = (ListView) findViewById(R.id.lstTodoItems);
		
				
		loadDB = new LoadDatabase();
		
		//adapter = loadDB.getAdapter();
		//items = loadDB.getItems();
		//datasource = loadDB.getDataSource();
		
		//list.setAdapter(adapter);
		
		loadDB.execute(getApplicationContext(), list);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int index, long arg3) {
				selectedIndex = index;
				String t = ((RedBlueAdapter) adapterView.getAdapter()).getItemText(index);
				builder.setTitle(t);

				
				CharSequence[] dItems = null;
				
				if (t.indexOf("Call ") == 0) 
				{
					dItems = new CharSequence[2];
					dItems[0] = getString(R.string.delete);
					dItems[1] = t ;
				}
				else
				{
					dItems = new CharSequence[1];
					dItems[0] = getString(R.string.delete);
				}
				
				builder.setItems(dItems, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int dItem) {
						// User clicked a delete item
						if (dItem == 0) 
						{
							loadDB.deleteItem( selectedIndex );
							//Item item = items.get(selectedIndex);
							//datasource.deleteItem(item);
							//items.remove(selectedIndex);
							//adapter.notifyDataSetChanged();
							
								
							// *** Parse integration removed *******************
							/*
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
							*/
						} 
						else 
						{
							if (dItem == 1) 
							{
								String text = "tel:" + loadDB.getItemText(selectedIndex).substring(5);
								Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(text));
								startActivity(dial);
							}
						}

					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();
				return true;
			}
		});

		
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
		//datasource.close();
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1) 
		{
			if (resultCode == RESULT_OK) 
			{
				if (intent != null) 
				{
					Bundle extras = intent.getExtras();
					if (extras != null) 
					{
						
						String newItemText = extras.getString("title");
						long dueDateLong = extras.getLong("dueDate");
						
						loadDB.addItem(newItemText, dueDateLong);
						//Item item = datasource.createItem( newItemText, dueDateLong);
						//adapter.add(item);
						
						// ** Parse integration removed *********
						/*
						ParseObject itemObject = new ParseObject(ITEM_CLASS);
						itemObject.put(ITEM_TITLE, newItemText);
						itemObject.put(ITEM_DUE_DATE, dueDateLong);
						itemObject.put(ITEM_ID, item.getId());
						itemObject.put(ITEM_USER, ParseUser.getCurrentUser());
						itemObject.setACL(new ParseACL(ParseUser.getCurrentUser()));
						itemObject.saveInBackground();
						*/
						
					}
				}

			}
		}
		
	}
	
}
