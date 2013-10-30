/*
 * Creator: Minhal Syed*/
package c301.AdventureBook;

import java.util.ArrayList;

import com.example.adventurebook.R;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EditOptionActivity extends Activity {

	ArrayAdapter<String> adapter;
	ListView pageListView;
	TextView gotoPageTV;
	
	// For now: we can just add Dummy Pages
	String[] pages = { "Page1", "Page2", "Page3", "Page4", "Page5",
			"Page6", "Page7" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_option);
		populateListView();
		registerForClicks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void populateListView() {
		// This function will put all the available pages in the listView
		pageListView = (ListView) findViewById(R.id.list_of_goto_pages);

		this.adapter = new ArrayAdapter<String>(this, R.layout.list_row, pages);
		pageListView.setAdapter(adapter);
	}

	private void registerForClicks() {
		// TODO Auto-generated method stub
		pageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view_clicked,
					int position, long arg3) {
				// TODO Auto-generated method stub

				TextView temp = (TextView) view_clicked;
				
				gotoPageTV = (TextView) findViewById(R.id.GotoPageTV);
				gotoPageTV.setText(temp.getText().toString());
			}

		});
	}
	
	public void SaveOption(View v){
		//TODO This method will be called when the SaveOption button is Clicked.
	}

}