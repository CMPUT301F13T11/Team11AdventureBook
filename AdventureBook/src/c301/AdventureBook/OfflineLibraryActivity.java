package c301.AdventureBook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import c301.AdventureBook.Models.Story;

import com.example.adventurebook.R;

public class OfflineLibraryActivity extends Activity {

	ArrayList<Story> offlineStoryLibrary;
	String title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		offlineStoryLibrary = new ArrayList<Story>();
		
		FileLoader fLoader = new FileLoader(this);
		offlineStoryLibrary = fLoader.loadAllStoryFiles();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_library);

		populateListView();
	}

	public void launchNewStoryActivity(View v) {
		Intent i = new Intent(this, CreateStoryActivity.class);
		startActivity(i);
	}

	public void launchOnlineLibraryActivity(View v) {
		Intent i = new Intent(this, OnlineLibraryActivity.class);
		// startActivity(i);
	}

	private void populateListView() {
		// Tutorial from : https://www.youtube.com/watch?v=4HkfDObzjXk

		ListView offlineLV = (ListView) findViewById(R.id.offline_library_listView);
		ArrayAdapter<Story> adapter = new CustomAdapter();
		offlineLV.setAdapter(adapter);
		registerForContextMenu(offlineLV);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("Publish Online");
		menu.add("Edit Story");
		menu.add("Delete Story");

		View thisItem = v;
		TextView titleText = (TextView) v.findViewById(R.id.titleTV);
		this.title = (String) titleText.getText();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onContextItemSelected(item);

		if (item.getTitle() == "Publish Online") {
			// Do Publish Story Function
			Toast.makeText(this, "Publish " + this.title, Toast.LENGTH_LONG)
			.show();
		}
		else if (item.getTitle() == "Edit Story") {
			
	        Intent i = new Intent(OfflineLibraryActivity.this, EditStoryActivity.class);
	        Bundle bundle = new Bundle();
	        bundle.putSerializable("editStory", offlineStoryLibrary.get(1));
	        i.putExtras(bundle);
	        startActivity(i);
			Toast.makeText(this, "Edit " + this.title, Toast.LENGTH_LONG)
					.show();
		} 
		else if (item.getTitle() == "Delete Story") {
			// Do Delete Story Function
			Toast.makeText(this, "Delete " + this.title, Toast.LENGTH_LONG)
					.show();

		}
		return true;
	}

	private class CustomAdapter extends ArrayAdapter<Story> {

		public CustomAdapter() {
			super(OfflineLibraryActivity.this, R.layout.offline_library_row,
					offlineStoryLibrary);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Tutorial used from: https://www.youtube.com/watch?v=WRANgDgM2Zg

			// Make sure we have a view to work with (may have been given null)

			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.offline_library_row, parent, false);
			}

			Story currentStory = offlineStoryLibrary.get(position);

			// Fill the view
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.storyThumbnailView);
			imageView.setImageResource(currentStory.getImageIcon());

			TextView titleText = (TextView) itemView.findViewById(R.id.titleTV);
			titleText.setText(currentStory.getTitle());

			TextView authorText = (TextView) itemView
					.findViewById(R.id.authorTV);
			authorText.setText(currentStory.getAuthor());

			TextView dateText = (TextView) itemView
					.findViewById(R.id.dateCreatedTV);
			dateText.setText(currentStory.getDate());

			return itemView;
		}
	}
}