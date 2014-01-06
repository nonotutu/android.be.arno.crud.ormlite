package be.arno.crud.items;

import be.arno.crud.R;
import be.arno.crud.Helper;
import be.arno.crud.Toaster;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.ToggleButton;

public class ItemNewActivity extends Activity {

	private static final String LOG_TAG = "ItemNewActivity";
	
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
				int i = createItemInDB();
				if ( i == 1 ) {
					Toaster.showToast(getApplicationContext(),
							Toaster.SUCCESS, R.string.item_created);
					finish();
				} else {
					Toaster.showToast(getApplicationContext(),
							Toaster.ERROR, R.string.item_not_created);
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
		setContentView(R.layout.activity_item_new);

		Log.i(LOG_TAG, "void onCreate(Bundle)");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		edtxName = (EditText)findViewById(R.id.itemForm_edtxName);
		dtpkDate = (DatePicker)findViewById(R.id.itemForm_dtpkDate);
		swchDate = (Switch)findViewById(R.id.itemForm_swchDate);
		rtbrRating = (RatingBar)findViewById(R.id.itemForm_rtbrRating);
		tgbtBool = (ToggleButton)findViewById(R.id.itemForm_tgbtBool);
		imbtImage = (ImageButton)findViewById(R.id.itemForm_imbtImage);
		
		/*
		Button bttnSave = (Button)findViewById(R.id.itemNew_bttnUpdate);
		bttnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "bttnSave.onClick");
				long l = create();
				Log.i(LOG_TAG, "return code: " + l);
				if ( l == 1 ) {
					Toast.makeText(getApplicationContext(),
							R.string.item_created, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.item_not_created, Toast.LENGTH_SHORT).show();
				}
		}});*/
	}

	private int createItemInDB() {
		Log.i(LOG_TAG, "int createItemInDB()");
		
		String date = null;
		if ( swchDate.isChecked() )		
			date = Helper.dateInts2String(dtpkDate.getYear(),
										  dtpkDate.getMonth(),
										  dtpkDate.getDayOfMonth());
		Item item = new Item();
		item.setCategoryId(getCategoryIdFromBundle());
		item.setName(edtxName.getText().toString());
		item.setDate(date);
		item.setRating(rtbrRating.getRating());
		item.setBool(tgbtBool.isChecked()?1:0);		
		// TODO : i.setImage

		
		ItemsDataSourceSelector dataSource = new ItemsDataSourceSelector(getApplicationContext());
		
		return dataSource.create(item);
	}
	
	private int getCategoryIdFromBundle() {
		Log.i(LOG_TAG, "int getCategoryIdFromBundle()");
		int categoryId = -1;
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			categoryId = extra.getInt("CATEGORY_ID");
		}
		Log.i(LOG_TAG, "int getCategoryIdFromBundle() return : " + categoryId);
		return categoryId;		
	}
}
