package be.arno.crud.items;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import be.arno.crud.App;
import be.arno.crud.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemIndexActivity extends Activity {

	private static final String LOG_TAG = "IdemIndexActivity";
	
	private TextView txvwCount;
	
	@Override
	protected void onRestart() {
		super.onRestart();
		setCount();
	}
	
	private void setCount() {
		
		ItemsRepository repos; // ORM
	    
		//init the comments repository
        repos = new ItemsRepository(this); // ORM
        //get all comments
        long count = repos.getCount(); // ORM
        //create and set adapter on the listview
		
		/* -ORM
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());
		itemAdapter.openReadable();
		int i = itemAdapter.getCount();
		itemAdapter.close();
		*/ 
		txvwCount.setText(""+count);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_index);

		txvwCount = (TextView)findViewById(R.id.itemIndex_txvwCount);
	
		setCount();
			
		// VFEC
		Button bttnNew = (Button)findViewById(R.id.itemIndex_bttnNew);
		bttnNew.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ItemNewActivity.class);
					startActivity(i);
				}
			}
		);
	
		// VFEC
		Button bttnList = (Button)findViewById(R.id.itemIndex_bttnList);
		bttnList.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
					startActivity(i);
				}
			}
		);
	
		// VFEC
		Button bttnSearch = (Button)findViewById(R.id.itemIndex_bttnSearch);
		bttnSearch.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ItemSearchActivity.class);
					startActivity(i);
				}
			}
		);
	}
}
