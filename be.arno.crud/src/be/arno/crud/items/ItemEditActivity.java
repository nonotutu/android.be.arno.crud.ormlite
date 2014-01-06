package be.arno.crud.items;

import be.arno.crud.Helper;
import be.arno.crud.R;
import be.arno.crud.Toaster;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.ToggleButton;
import android.widget.Switch;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;

public class ItemEditActivity extends Activity {
	
	private static final String LOG_TAG = "ItemEditActivity";

	private Item item;
	
	private EditText edtxName;
	private DatePicker dtpkDate;
	private Switch swchDate;
	private RatingBar rtbrRating;
	private ToggleButton tgbtBool;
	private ImageButton imbtImage;

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_save, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case R.id.action_save:
	        	int i = updateItemInDB();
				if ( i == 1 ) {
					Toaster.showToast(getApplicationContext(),
									  Toaster.SUCCESS, R.string.item_updated);
					finish();
				} else {
					Toaster.showToast(getApplicationContext(),
							  		  Toaster.ERROR, R.string.item_not_updated);
				}
				return true;
	        case android.R.id.home:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_edit);
		
	    getActionBar().setDisplayHomeAsUpEnabled(true);

		edtxName =   (EditText)    findViewById(R.id.itemForm_edtxName);
		dtpkDate =   (DatePicker)  findViewById(R.id.itemForm_dtpkDate);
		swchDate =   (Switch)      findViewById(R.id.itemForm_swchDate);
		rtbrRating = (RatingBar)   findViewById(R.id.itemForm_rtbrRating);
		tgbtBool =   (ToggleButton)findViewById(R.id.itemForm_tgbtBool);
		imbtImage =  (ImageButton) findViewById(R.id.itemForm_imbtImage);
		
		/*
		Button bttnUpdate = (Button)findViewById(R.id.itemEdit_bttnUpdate);
		bttnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = updateItemInDB(); // TODO : vérifier codes retour
				if ( i == 1 ) {
					Toast.makeText(getApplicationContext(),
							R.string.item_updated, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), 
							R.string.item_not_updated, Toast.LENGTH_SHORT).show();
				}
		}});*/

		assignItemFromDB(getIdFromBundle());
				
		fillFields();
	}


	private int getIdFromBundle() {
		Log.i(LOG_TAG, "int getIdFromBundle()");
		int id = -1;
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) 
			id = extra.getInt("ID");
		Log.i(LOG_TAG, "int getIdFromBundle() return : " + id);
		return id;
	}


	private void assignItemFromDB(int itemId) {
		Log.i(LOG_TAG, "int getIdFromBundle(int itemId) | " + itemId);
		ItemsDataSourceSelector itemsData =
				new ItemsDataSourceSelector(getApplicationContext());
        item = itemsData.getById(itemId);
        // termine l'activité si l'Item n'existe pas dans la DB
        if ( item == null ) {
			Toaster.showToast(getApplicationContext(),
					  Toaster.ERROR,
					  R.string.item_does_not_exist);
			finish();        	
        }
	}
	

	private int updateItemInDB() {
		Log.i(LOG_TAG, "int updateItemInDB()");

		String date = null;
		if ( swchDate.isChecked() )		
			date = Helper.dateInts2String(dtpkDate.getYear(),
										  dtpkDate.getMonth(),
										  dtpkDate.getDayOfMonth());
		Item i = new Item();
		i.setId    	 (item.getId());
		i.setCategoryId(item.getCategoryId());
		i.setName  	 (edtxName.getText().toString());
		i.setDate    (date);
		i.setRating  (rtbrRating.getRating());
		i.setBool    (tgbtBool.isChecked()?1:0);
		i.setImage   (((BitmapDrawable)imbtImage.getDrawable()).getBitmap());
		
		ItemsDataSourceSelector itemsData = 
				new ItemsDataSourceSelector(getApplicationContext());
		int ii = itemsData.update(i);
		Log.i(LOG_TAG, "int updateItemInDB() | return : " + ii);
		return ii;
	}

	
	
	private void fillFields() {
		if ( item != null ) {		
			edtxName.setText(item.getName());
			if ( item.getDate() != null ) {
					swchDate.setChecked(true);
					dtpkDate.updateDate(item.getDatePart("yyyy"),
										item.getDatePart("MM")-1, 
										item.getDatePart("dd")); }
			rtbrRating.setRating(item.getRating());
			tgbtBool.setChecked(item.getBool()==1?true:false);
			imbtImage.setImageBitmap(item.getImage());			
		}
	}
}
