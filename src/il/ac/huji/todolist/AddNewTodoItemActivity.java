package il.ac.huji.todolist;

import java.util.GregorianCalendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_item);
    }
	
	public void addItem( View view )
	{
		EditText editText = (EditText) findViewById(R.id.edtNewItem);
    	String textEntered = editText.getText().toString();
    	if (textEntered.length() == 0)
    	{
    		return;
    	}
    	
		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		int year = datePicker.getYear();
		int month = datePicker.getMonth();
		int day = datePicker.getDayOfMonth();
		GregorianCalendar date = new GregorianCalendar( year, month, day );
    			
		
		Intent intent = new Intent(this, TodoListManagerActivity.class); 
		intent.putExtra("title", textEntered); 
		intent.putExtra("dueDate", date.getTimeInMillis());
		
		setResult(RESULT_OK, intent); 
		finish(); 

	}
	
	public void cancel ( View view )
	{
		Intent intent = new Intent(this, TodoListManagerActivity.class); 
				
		setResult(RESULT_CANCELED, intent); 
		finish(); 
	}

}
