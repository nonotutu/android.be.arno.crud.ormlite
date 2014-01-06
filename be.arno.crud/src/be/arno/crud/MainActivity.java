package be.arno.crud;

import java.util.Random;

import be.arno.crud.R;
import be.arno.crud.categories.CategoriesDataSourceSelector;
import be.arno.crud.categories.CategoryListActivity;
import be.arno.crud.items.Item;
import be.arno.crud.items.ItemSearchActivity;
import be.arno.crud.items.ItemsDataSourceSelector;
import be.arno.crud.items.Pokemons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

	private static final String LOG_TAG = "MainActivity";

	private TextView txvwCount;
	private TextView txvwDataSource;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent i = new Intent(this, SettingsActivity.class);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setCount();
	}
	
	private void setCount() {
		CategoriesDataSourceSelector categoriesData = new CategoriesDataSourceSelector(getApplicationContext());
		ItemsDataSourceSelector itemsData = new ItemsDataSourceSelector(getApplicationContext());
		txvwCount.setText(categoriesData.getCount() + " - " + itemsData.getCount());
		
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		txvwDataSource.setText("Server : " + settings.getString("serverUrl", null));
	}
	
	/*
	
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txvwCount = (TextView)findViewById(R.id.main_txvwCount);
		txvwDataSource = (TextView)findViewById(R.id.main_txvwDataSource);

		Button bttnCategories = (Button)findViewById(R.id.main_bttnCategories);
		bttnCategories.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), CategoryListActivity.class);
				startActivity(i);
			}
		});

		Button bttnClose = (Button)findViewById(R.id.main_bttnClose);
		bttnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		Button bttnFill = (Button)findViewById(R.id.main_bttnFillDB);		
		bttnFill.setOnLongClickListener(
			new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					
					CategoriesDataSourceSelector categoriesData;
				    categoriesData = new CategoriesDataSourceSelector(getApplicationContext());
					
				    if ( categoriesData.getCount() > 0 ) {
				    	AST_fillWithPokemons task = new AST_fillWithPokemons();
				    	task.execute();
				    } else {
				    	Toaster.showToast(getApplicationContext(),
								  Toaster.ERROR,
								  "There must be at least 1 Category to insert Items.");
				    }
				    
					return false;
		}});
		

		Button bttnDeleteAll = (Button)findViewById(R.id.main_bttnClearDB);		
		bttnDeleteAll.setOnLongClickListener(
			new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {

			    	Toaster.showToast(getApplicationContext(),
							  		  Toaster.INFO,
							  		  "Instruction sent.");
					new Thread(
						new Runnable() {
							public void run() {
								ItemsDataSourceSelector itemsData = new ItemsDataSourceSelector(getApplicationContext());
								itemsData.deleteAll();
								CategoriesDataSourceSelector categoriesData = new CategoriesDataSourceSelector(getApplicationContext());
								categoriesData.deleteAll();
					}}).start();
			
					onRestart();
					return false;
		}});
		
		Button bttnSearch = (Button)findViewById(R.id.main_bttnSearch);
		bttnSearch.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ItemSearchActivity.class);
					startActivity(i);
		}});
		
		Button bttnSync = (Button)findViewById(R.id.main_bttnSync);
		bttnSync.setOnLongClickListener(
			new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
			    	/*
					Toaster.showToast(getApplicationContext(),
					  		  Toaster.INFO,
					  		  "Instruction sent.");
			    	
			    	Intent i = new Intent(getApplicationContext(), SynchronizationActivity.class);
					startActivity(i);
					*/
					return false;
		}});


	}
	

	private class AST_fillWithPokemons extends AsyncTask<Void, Integer, String> {
		
		NotificationManager notifManag = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		final int notificationRef = 2013;
		final int progressNotificationRef = 2014;

		@Override
		protected void onPreExecute() {
			Log.i(LOG_TAG, "AST_fillWithPokemons | void onPreExecute()");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			Log.i(LOG_TAG, "AST_fillWithPokemons | String doInBackground(Void... params)");
			
			CategoriesDataSourceSelector categoriesData;
		    categoriesData = new CategoriesDataSourceSelector(getApplicationContext());
			
		    int[] categs = categoriesData.getAllIds();
		    
			ItemsDataSourceSelector itemsData;
		    itemsData = new ItemsDataSourceSelector(getApplicationContext());
	
			Random rand = new Random();
			
			Item item;

			int max = Pokemons.LIST.length;
			
			for (int j = 0; j < 1; j+=1 ) {
			
				int i = 0;
				int cat;
				String mm, dd;
				
				while ( i < Pokemons.LIST.length ) {
				
				mm = "" + (rand.nextInt(12)+1); if (mm.length() < 2) mm = "0" + mm;
				dd = "" + (rand.nextInt(28)+1); if (dd.length() < 2) dd = "0" + dd;
	
				cat = categs[rand.nextInt(categs.length)];
				
				item = new Item();
					item.setName(Pokemons.LIST[i]);
					item.setDate((rand.nextInt(2100-1900)+1900) + "-" + mm + "-" + dd);
					item.setRating((rand.nextInt(10)+1)/Float.parseFloat("2.0"));
					item.setBool(rand.nextInt(2));
					item.setCategoryId(cat);
					String resid = "" + (i+1);
					if ( resid.length() == 1 ) resid = "00" + resid;
					if ( resid.length() == 2 ) resid = "0" + resid;
					int res = getResources().getIdentifier("p" + resid, "drawable", "be.arno.crud");
					Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), res);
					item.setImage(mBitmap);
					
					itemsData.create(item);
					publishProgress(max, i);

					
				i+=1;
				}
			}
				
			return null;
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... i) {
			NotificationMaker notifMaker = new NotificationMaker(getApplicationContext(), "Filling database", null, null, null, null, i[0], i[1]);
			notifManag.notify(progressNotificationRef, notifMaker.getNotif());
		}
		
		@Override
		protected void onPostExecute(String result) {
			notifManag.cancel(progressNotificationRef);
			NotificationMaker notifMaker = new NotificationMaker(getApplicationContext(), "Database filled", null, null, null, MainActivity.class);
			notifManag.notify(notificationRef, notifMaker.getNotif());
		}
		

	}
}
	

