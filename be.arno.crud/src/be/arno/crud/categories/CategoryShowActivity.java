package be.arno.crud.categories;

import java.util.ArrayList;
import java.util.List;

import be.arno.crud.R;
import be.arno.crud.myApp;
import be.arno.crud.items.ItemListActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class CategoryShowActivity extends Activity {

	private Category category;

	private TextView txvwId;
	private TextView txvwName;
	private TextView txvwItemsCount;
	private SeekBar skbrPosition;
	private TextView txvwPosition;
	private ArrayList<Integer> ids;
	private int last;

	// TODO : retirer le start activity for result de Item
	@Override
	protected void onRestart() {
		super.onRestart();
		fillFields();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_show);

		txvwId =         (TextView) findViewById(R.id.categoryShow_txvwId);
		txvwName =       (TextView) findViewById(R.id.categoryShow_txvwName);
		txvwItemsCount = (TextView) findViewById(R.id.categoryShow_txvwItemsCount);
		skbrPosition =   (SeekBar)  findViewById(R.id.categoryShow_skbrPosition);
		txvwPosition =   (TextView) findViewById(R.id.categoryShow_txvwPosition);
		
		Button bttnItems = (Button)findViewById(R.id.categoryShow_bttnItems);
		bttnItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
				intent.putExtra("CATEGORY_ID", category.getId() );
				startActivity(intent);
			}
		});
		
		Button bttnDelete = (Button)findViewById(R.id.categoryShow_bttnDelete);
		bttnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog d = askConfirmation();
				d.show();
			}
		});

		Button bttnEdit = (Button)findViewById(R.id.categoryShow_bttnEdit);
		bttnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(getApplicationContext(), CategoryEditActivity.class);
				// intent.putExtra("ID", "" + category.getId() );
				// startActivity(intent);
			}
		});

		Button bttnPrev = (Button)findViewById(R.id.categoryShow_bttnPrev);
		bttnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( last > 0 ) {
					last = last - 1;
					getCategoryFromDB(ids.get(last));
					fillFields();
				}
			}
		});

		Button bttnNext = (Button)findViewById(R.id.categoryShow_bttnNext);
		bttnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( last < ids.size()-1 ) {
					last = last + 1;
					getCategoryFromDB(ids.get(last));
					fillFields();
				}
			}
		});

		skbrPosition.setOnSeekBarChangeListener(
			new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					last = seekBar.getProgress();
					getCategoryFromDB(ids.get(last));
					fillFields();
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					txvwPosition.setText( " " + (progress+1) + " / " + ids.size() + " ");
				}
			});


		// récupérer l'ID / IDS / LAST dans le Bundle
		int id = getIdFromParams();

		skbrPosition.setMax(ids.size()-1);

		// récupérer l'item de la DB
		getCategoryFromDB(id);

		// afficher les informations
		fillFields();
		
	}


	private void deleteCategory() {
		
		CategoriesRepository repos = new CategoriesRepository(getApplicationContext());
		repos.delete(category);
	
		// TODO : vérifier si supprimé
		finish();
	}


	private int getIdFromParams() {

		String strId = null;
		int intId;

		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			strId = extra.getString("ID");
			ids = extra.getIntegerArrayList("IDS");
			last = extra.getInt("LAST");
		}
		try {
			intId = Integer.parseInt(strId);
			return intId;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Not an integer", Toast.LENGTH_LONG).show();
			finish();
		}
		return -1;
	}


	private void getCategoryFromDB(int id) {
		
		CategoriesRepository repos;
        repos = new CategoriesRepository(this);
        category = repos.getCategoryById(id);
		
	}


	private void fillFields() {
		if ( category != null ) {		
			txvwId.setText(""+category.getId());
			txvwName.setText(category.getName());
			txvwItemsCount.setText(""+category.getCountItems());
			skbrPosition.setProgress(last);
			txvwPosition.setText( " " + (last+1) + " / " + ids.size() + " ");
		} else {
			Toast.makeText(getApplicationContext(), "@string/category_does_not_exist", Toast.LENGTH_LONG).show();
			finish();
		}
	}


	private Dialog askConfirmation() {

		Dialog d = new AlertDialog.Builder(this)
		.setMessage(R.string.sureDelete)
		.setNegativeButton(android.R.string.no, null)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteCategory();
				}
			})
		.create();
		return d;
	    }

}
