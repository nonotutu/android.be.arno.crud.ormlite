package be.arno.crud;

import java.util.Random;

import be.arno.crud.R;
import be.arno.crud.categories.CategoriesRepository;
import be.arno.crud.categories.CategoryIndexActivity;
import be.arno.crud.items.Item;
import be.arno.crud.items.ItemSearchActivity;
import be.arno.crud.items.ItemsRepository;
import be.arno.crud.items.Pokemons;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String LOG_TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button bttnCategories = (Button)findViewById(R.id.main_bttnCategories);
		Button bttnClose = (Button)findViewById(R.id.main_bttnClose);
		
		bttnCategories.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), CategoryIndexActivity.class);
				startActivity(i);
			}
		});

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
					
					CategoriesRepository categoriesRepos;
				    categoriesRepos = new CategoriesRepository(getApplicationContext());
					
				    if ( categoriesRepos.getCount() > 0 ) {
						new Thread(
							new Runnable() {
								public void run() {
									fillWithPokemons();
						}}).start();
						Toaster.showToast(getApplicationContext(),
								  Toaster.INFO,
								  "Instruction sent.");
						onRestart();
				    } else {
				    	Toaster.showToast(getApplicationContext(),
								  Toaster.ERROR,
								  "There must be at least 1 Category to insert Items.");
				    }
				    
						
					//}

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
								ItemsRepository itemsRepository = new ItemsRepository(getApplicationContext());
								itemsRepository.deleteAll();
								CategoriesRepository categoriesRepository = new CategoriesRepository(getApplicationContext());
								categoriesRepository.deleteAll();
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
			

	}
	

	
	public void fillWithPokemons() {
		
		CategoriesRepository categsRepos;
	    categsRepos = new CategoriesRepository(getApplicationContext());
		
	    int[] categs = categsRepos.getAllIds();
	    
		ItemsRepository itemsRepos;
	    itemsRepos = new ItemsRepository(getApplicationContext());

	    // ItemsServer server;
	    // server = new ItemsServer(getApplicationContext());
		
		Random rand = new Random();
		
		Item item;
		
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
				item.setCategory(cat);
				String resid = "" + (i+1);
				if ( resid.length() == 1 ) resid = "00" + resid;
				if ( resid.length() == 2 ) resid = "0" + resid;
				// Log.i("fillWithPokemons", "p" + resid);
				int res = getResources().getIdentifier("p" + resid, "drawable", "be.arno.crud");
				Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), res);
				item.setImage(mBitmap);
				
				// server.create(item);
				itemsRepos.create(item);
				
			i+=1;
			}
		}

		Log.i("static", "array filled");
	}
	
		
}
