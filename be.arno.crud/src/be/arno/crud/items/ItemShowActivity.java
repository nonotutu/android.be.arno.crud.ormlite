package be.arno.crud.items;

import java.util.ArrayList;

import be.arno.crud.R;
import be.arno.crud.Toaster;
import be.arno.crud.categories.CategoriesRepository;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

//TODO : gérer les mises à jour de listes : ids

public class ItemShowActivity extends Activity {
	
	private static final String LOG_TAG = "ItemShowActivity";

	private Item item;

	private TextView txvwId;
	private TextView txvwCategory;
	private TextView txvwName;
	private TextView txvwDate;
	private RatingBar rtbrRating;
	private TextView txvwBool;
	private SeekBar skbrPosition;
	private TextView txvwPosition;
	private ImageView imvwImage;
	private TextView txvwCreatedAt;
	private TextView txvwUpdatedAt;

	
	private ArrayList<Integer> array_ids; // liste des ids
	private int position_in_ids;	      // position dans la liste des ids

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_edit_delete, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	        case R.id.action_edit:
	        	if ( item != null ) {
					Intent intent = new Intent(getApplicationContext(),
							                   ItemEditActivity.class);
					intent.putExtra("ID", item.getId() );
					startActivity(intent);
				}
	        	return true;
	        case R.id.action_delete:
	        	if ( item != null ) {
					Dialog dialog = askConfirmationForDelete();
					dialog.show();
				}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(LOG_TAG, "void onStart()");
		assignItemFromDB(array_ids.get(position_in_ids));
		fillFields();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_show);

		Log.i(LOG_TAG, "void onCreate(Bundle)");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		txvwId =        (TextView) findViewById(R.id.itemShow_txvwId);
		txvwCategory =  (TextView) findViewById(R.id.itemShow_txvwCategory);
		txvwName =      (TextView) findViewById(R.id.itemShow_txvwName);
		txvwDate =      (TextView) findViewById(R.id.itemShow_txvwDate);
		rtbrRating =    (RatingBar)findViewById(R.id.itemShow_rtbrRating);
		txvwBool =      (TextView) findViewById(R.id.itemShow_txvwBool);
		skbrPosition =  (SeekBar)  findViewById(R.id.itemShow_skbrPosition);
		txvwPosition =  (TextView) findViewById(R.id.itemShow_txvwPosition);
		imvwImage =     (ImageView)findViewById(R.id.itemShow_imvwImage);
		txvwCreatedAt = (TextView) findViewById(R.id.itemShow_txvwCreatedAt);
		txvwUpdatedAt = (TextView) findViewById(R.id.itemShow_txvwUpdatedAt);

		
		
		
		
		
		
		
		
		
		
		/*Button bttnDelete = (Button)findViewById(R.id.itemShow_bttnDelete);
		bttnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( item != null ) {
					Dialog dialog = askConfirmationForDelete();
					dialog.show();
				}
		}});*/

		
		/*Button bttnEdit = (Button)findViewById(R.id.itemShow_bttnEdit);
		bttnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( item != null ) {
					Intent intent = new Intent(getApplicationContext(),
							                   ItemEditActivity.class);
					intent.putExtra("ID", item.getId() );
					startActivity(intent);
				}
		}});*/

		
		ImageButton bttnPrev = (ImageButton)findViewById(R.id.itemShow_bttnPrev);
		bttnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( position_in_ids > 0 ) {
					position_in_ids = position_in_ids - 1;
					assignItemFromDB(array_ids.get(position_in_ids));
					fillFields();
				}
		}});

		
		ImageButton bttnNext = (ImageButton)findViewById(R.id.itemShow_bttnNext);
		bttnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( position_in_ids < array_ids.size()-1 ) {
					position_in_ids = position_in_ids + 1;
					assignItemFromDB(array_ids.get(position_in_ids));
					fillFields();
				}
		}});

		
		skbrPosition.setOnSeekBarChangeListener(
			new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					position_in_ids = seekBar.getProgress();
					assignItemFromDB(array_ids.get(position_in_ids));
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


	private void deleteItem() {
		Log.i(LOG_TAG, "void deleteItem()");		
		ItemsDataSourceSelector itemsData = 
				new ItemsDataSourceSelector(getApplicationContext());
		itemsData.delete(item.getId());
	
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
		Log.i(LOG_TAG, "getIdFromBundle()");
		int id = -1;
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			id = extra.getInt("ID");
		}
		return id;
	}*/


	private void assignItemFromDB(int itemId) {
		Log.i(LOG_TAG, "assignItemFromDB(int itemId)");
		ItemsDataSourceSelector itemsData =
				new ItemsDataSourceSelector(getApplicationContext());
        item = itemsData.getById(itemId);
	}


	private void fillFields() {
		Log.i(LOG_TAG, "void fillFields()");
		
		skbrPosition.setProgress(position_in_ids);
		txvwPosition.setText( (position_in_ids+1) + " / " + array_ids.size() );

		if ( item != null ) {		
			txvwId.setText(""+item.getId());
			txvwCategory.setText(item.getCategoryName());
			txvwName.setText(item.getName());
			txvwDate.setText(item.getDate());
			rtbrRating.setRating(item.getRating());
			txvwBool.setText(""+item.getBool());
			imvwImage.setImageBitmap(item.getImage());
			txvwCreatedAt.setText(""+item.getCreatedAt());
			txvwUpdatedAt.setText(""+item.getUpdatedAt());
		} else {
			clearFields();
			Toaster.showToast(getApplicationContext(),
					  Toaster.ERROR,
					  R.string.item_does_not_exist);
		}
	}
	

	private void clearFields() {
		Log.i(LOG_TAG, "void clearFields()");
		txvwId.setText("");
		txvwCategory.setText("");
		txvwName.setText("");
		txvwDate.setText("");
		rtbrRating.setRating(0);
		txvwBool.setText("");
		imvwImage.setImageBitmap(null);
		txvwCreatedAt.setText("");
		txvwUpdatedAt.setText("");
	}


	private Dialog askConfirmationForDelete() {
		Log.i(LOG_TAG, "Dialog askConfirmationForDelete()");
		Dialog d = new AlertDialog.Builder(this)
		.setMessage(R.string.sure_delete_item)
		.setNegativeButton(android.R.string.no, null)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteItem();
		}}).create();
		return d;
	    }
}
