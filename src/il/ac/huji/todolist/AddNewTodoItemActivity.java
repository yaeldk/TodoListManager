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
		
		
		Intent intent = new Intent(this, TodoListManagerActivity.class); 
		intent.putExtra("Text", textEntered); 
		intent.putExtra("Year", String.valueOf(year));
		intent.putExtra("Month", String.valueOf(month));
		intent.putExtra("Day", String.valueOf(day));
		
		setResult(RESULT_OK, intent); 
		finish(); 

	}

}
