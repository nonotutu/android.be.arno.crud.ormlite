package be.arno.crud.items;

import java.util.ArrayList;
import java.util.List;

import be.arno.crud.R;
import be.arno.crud.myApp;

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


public class ItemShowActivity extends Activity {

	private Item item;

	private TextView txvwId;
	private TextView txvwName;
	private TextView txvwDate;
	private RatingBar rtbrRating;
	private TextView txvwBool;
	private SeekBar skbrPosition;
	private TextView txvwPosition;
	private ImageView imvwImage;
	private ArrayList<Integer> ids;
	private int last;

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case myApp.CODE_ACTIVITY_EDIT_ITEM:
			switch(resultCode) {
			case RESULT_OK:
				// refresh if updated
				getItemFromDB(item.getId());
				fillFields();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_show);

		txvwId =       (TextView) findViewById(R.id.itemShow_txvwId);
		txvwName =     (TextView) findViewById(R.id.itemShow_txvwName);
		txvwDate =     (TextView) findViewById(R.id.itemShow_txvwDate);
		rtbrRating =   (RatingBar)findViewById(R.id.itemShow_rtbrRating);
		txvwBool =     (TextView) findViewById(R.id.itemShow_txvwBool);
		skbrPosition = (SeekBar)  findViewById(R.id.itemShow_skbrPosition);
		txvwPosition = (TextView) findViewById(R.id.itemShow_txvwPosition);
		imvwImage =    (ImageView)findViewById(R.id.itemShow_imvwImage);
		
		Button bttnDelete = (Button)findViewById(R.id.itemShow_bttnDelete);
		bttnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog d = askConfirmation();
				d.show();
			}
		});

		Button bttnEdit = (Button)findViewById(R.id.itemShow_bttnEdit);
		bttnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("item.getId()", "" + item.getId());
				Intent i = new Intent(getApplicationContext(), ItemEditActivity.class);
				i.putExtra("ID", "" + item.getId() );
				startActivityForResult(i, myApp.CODE_ACTIVITY_EDIT_ITEM);
			}
		});

		Button bttnPrev = (Button)findViewById(R.id.itemShow_bttnPrev);
		bttnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( last > 0 ) {
					last = last - 1;
					getItemFromDB(ids.get(last));
					fillFields();
				}
			}
		});

		Button bttnNext = (Button)findViewById(R.id.itemShow_bttnNext);
		bttnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( last < ids.size()-1 ) {
					last = last + 1;
					getItemFromDB(ids.get(last));
					fillFields();
				}
			}
		});

		skbrPosition.setOnSeekBarChangeListener(
			new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					last = seekBar.getProgress();
					getItemFromDB(ids.get(last));
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
		getItemFromDB(id);

		// afficher les informations
		fillFields();
		
		
	}


	private void deleteItem() {
		
		ItemsRepository repos = new ItemsRepository(getApplicationContext());
		repos.delete(item);
		
		/* -ORM
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());
		itemAdapter.openWritable();
		itemAdapter.delete(item);
		itemAdapter.close();
		*/
		
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


	private void getItemFromDB(int id) {
		
		ItemsRepository repos; // ORM

		//init the comments repository
        repos = new ItemsRepository(this); // ORM
        //get all comments
        item = repos.getItemById(id); // ORM
		
		/* -ORM
		Item i;
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());
		itemAdapter.openReadable();
		i = itemAdapter.getItemById(id);
		itemAdapter.close();
		item = i;
		*/
		
	}


	private void fillFields() {
		if ( item != null ) {		
			txvwId.setText(""+item.getId());
			txvwName.setText(item.getName());
			txvwDate.setText(item.getDate());
			rtbrRating.setRating(item.getRating());
			txvwBool.setText(""+item.getBool());
			imvwImage.setImageBitmap(item.getImage());
			skbrPosition.setProgress(last);
			txvwPosition.setText( " " + (last+1) + " / " + ids.size() + " ");
		} else {
			Toast.makeText(getApplicationContext(), "Item doesn't exist", Toast.LENGTH_LONG).show();
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
					deleteItem();
					//Toast.makeText(getApplicationContext(), "Cliqué", Toast.LENGTH_LONG).show();
				}
			})
		.create();
		return d;
	    }

}
