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
		
		// VFEC
		Button bttnFill = (Button)findViewById(R.id.itemIndex_bttnFill);		
		bttnFill.setOnLongClickListener(
			new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					
						new Thread(
							new Runnable() {
								public void run() {
									
									fillWithPokemons();

						}}).start();			
						Toast.makeText(getApplicationContext(), "Instruction sent.", Toast.LENGTH_LONG).show();
						onRestart();
					//}

					return false;
		}});
		
		// VFEC
		Button bttnDeleteAll = (Button)findViewById(R.id.itemIndex_bttnDeleteAll);		
		bttnDeleteAll.setOnLongClickListener(
			new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					
					ItemsRepository repos;
				    repos = new ItemsRepository(getApplicationContext());
					repos.deleteAll();
					
					ItemsServer server;
					server = new ItemsServer(getApplicationContext());
					server.delete(16923);
					
					onRestart();
					return false;
		}});

	}
	

	
	public void fillWithPokemons() {
		
		ItemsRepository repos;
	    repos = new ItemsRepository(getApplicationContext());

	    ItemsServer server;
	    server = new ItemsServer(getApplicationContext());
		
		Random rand = new Random();
		
		Item item;
		
		for (int j = 0; j < 1; j+=1 ) {
		
			int i = 0;
			String mm, dd;
			
			while ( i < Pokemons.LIST.length ) {
			
			mm = "" + (rand.nextInt(12)+1);
			if (mm.length() < 2) mm = "0" + mm;
			
			dd = "" + (rand.nextInt(28)+1);
			if (dd.length() < 2) dd = "0" + dd;
			
			item = new Item();
				item.setName(Pokemons.LIST[i]);
				item.setDate((rand.nextInt(2100-1900)+1900) + "-" + mm + "-" + dd);
				item.setRating((rand.nextInt(10)+1)/Float.parseFloat("2.0"));
				item.setBool(rand.nextInt(2));
				String resid = "" + (i+1);
				if ( resid.length() == 1 ) resid = "00" + resid;
				if ( resid.length() == 2 ) resid = "0" + resid;
				// Log.i("fillWithPokemons", "p" + resid);
				int res = getResources().getIdentifier("p" + resid, "drawable", "be.arno.crud");
				Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), res);
				item.setImage(mBitmap);
				
				server.create(item);
				repos.create(item);
				
			i+=1;
			}
		}

		Log.i("static", "array filled");
	}
	
	
	
}
