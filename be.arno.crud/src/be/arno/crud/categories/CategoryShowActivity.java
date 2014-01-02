package be.arno.crud.categories;

import java.util.ArrayList;

import be.arno.crud.R;
import be.arno.crud.Toaster;
import be.arno.crud.items.ItemListActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


// TODO : gérer les mises à jour de listes : ids

public class CategoryShowActivity extends Activity {
	
	private static final String LOG_TAG = "CategoryShowActivity";

	private Category category;
	private ArrayList<Integer> array_ids; // liste des ids
	private int position_in_ids;		  // position dans la liste des ids

	private TextView txvwId;
	private TextView txvwName;
	private TextView txvwItemsCount;
	private SeekBar skbrPosition;
	private TextView txvwPosition;
		
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(LOG_TAG, "void onStart()");
		assignCategoryFromDB(array_ids.get(position_in_ids));
		fillFields();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_show);
		Log.i(LOG_TAG, "void onCreate(Bundle)");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		txvwId =         (TextView) findViewById(R.id.categoryShow_txvwId);
		txvwName =       (TextView) findViewById(R.id.categoryShow_txvwName);
		txvwItemsCount = (TextView) findViewById(R.id.categoryShow_txvwItemsCount);
		skbrPosition =   (SeekBar)  findViewById(R.id.categoryShow_skbrPosition);
		txvwPosition =   (TextView) findViewById(R.id.categoryShow_txvwPosition);
		
		
		Button bttnItems = (Button)findViewById(R.id.categoryShow_bttnItems);
		bttnItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( category != null ) {		
					Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
					intent.putExtra("CATEGORY_ID", category.getId() );
					startActivity(intent);
				}
		}});
		
		
		Button bttnDelete = (Button)findViewById(R.id.categoryShow_bttnDelete);
		bttnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( category != null ) {		
					Dialog dialog = askConfirmationForDelete();
					dialog.show();
				}
		}});

		
		Button bttnEdit = (Button)findViewById(R.id.categoryShow_bttnEdit);
		bttnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( category != null ) {		
					Intent intent = new Intent(getApplicationContext(),
							                   CategoryEditActivity.class);
					intent.putExtra("ID", category.getId() );
					startActivity(intent);
				}
		}});
		
		
		Button bttnPrev = (Button)findViewById(R.id.categoryShow_bttnPrev);
		bttnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( position_in_ids > 0 ) {
					position_in_ids = position_in_ids - 1;
					assignCategoryFromDB(array_ids.get(position_in_ids));
					fillFields();
				}
		}});
		
		
		Button bttnNext = (Button)findViewById(R.id.categoryShow_bttnNext);
		bttnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( position_in_ids < array_ids.size()-1 ) {
					position_in_ids = position_in_ids + 1;
					assignCategoryFromDB(array_ids.get(position_in_ids));
					fillFields();
				}
		}});
		
		
		skbrPosition.setOnSeekBarChangeListener(
			new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					position_in_ids = seekBar.getProgress();
					assignCategoryFromDB(array_ids.get(position_in_ids));
					fillFields();
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					txvwPosition.setText( (progress+1) + " / " + array_ids.size() );
				}
			});
		
		
		// récupérer les infos de liste (IDS & POSITION)
		assignParamsFromBundle();
		
		skbrPosition.setMax(array_ids.size()-1);
	}


	private void deleteCategory() {
		Log.i(LOG_TAG, "void deleteCategory()");		
		CategoriesRepository categoriesRepository = 
				new CategoriesRepository(getApplicationContext());
		categoriesRepository.delete(category);
	
		// TODO : vérifier si supprimé
		finish();
	}


	private void assignParamsFromBundle() {
		Log.i(LOG_TAG, "assignParamsFromBundle()");
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			array_ids = extra.getIntegerArrayList("ARRAY_IDS");
			array_ids.add(0); // pour tester un élément vide TODO : supprimer ligne
			position_in_ids = extra.getInt("POSITION_IN_IDS");
		}
	}
	
	/*
	private int getIdFromBundle() {
		Log.i(LOG_TAG, "int getIdFromBundle()");
		int id = -1;
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			id = extra.getInt("ID");
		}
		return id;
	}*/


	private void assignCategoryFromDB(int categoryId) {
		Log.i(LOG_TAG, "assignCategoryFromDB(int categoryId) | " + categoryId);
		CategoriesRepository categoriesRepository =
				new CategoriesRepository(getApplicationContext());
        category = categoriesRepository.getCategoryById(categoryId);
	}


	private void fillFields() {
		Log.i(LOG_TAG, "void fillFields()");
		
		skbrPosition.setProgress(position_in_ids);
		txvwPosition.setText( (position_in_ids+1) + " / " + array_ids.size() );
		
		if ( category != null ) {		
			txvwId.setText(""+category.getId());
			txvwName.setText(category.getName());
			txvwItemsCount.setText(""+category.getCountItems());
		} else {
			clearFields();
			Toaster.showToast(getApplicationContext(),
					  Toaster.ERROR,
					  R.string.category_does_not_exist);
		}
	}

	
	
	
	
	
	private void clearFields() {
		Log.i(LOG_TAG, "clearFields()");
		txvwId.setText("");
		txvwName.setText("");
		txvwItemsCount.setText("");
	}

	
	
	
	

	private Dialog askConfirmationForDelete() {
		Log.i(LOG_TAG, "askConfirmationForDelete()");
		Dialog d = new AlertDialog.Builder(this)
		.setMessage(R.string.sure_delete_category_and_nested_items)
		.setNegativeButton(android.R.string.no, null)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteCategory();
		}}).create();
		return d;
	    }
}
