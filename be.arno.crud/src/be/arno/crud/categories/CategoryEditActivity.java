package be.arno.crud.categories;

import be.arno.crud.R;
import be.arno.crud.Toaster;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;







public class CategoryEditActivity extends Activity {

	private static final String LOG_TAG = "CategoryEditActivity";

	private Category category;
	
	private EditText edtxName;
	
	
	
	
	
	
	
	
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
	        	int i = updateCategoryInDB();
				if ( i == 1 ) {
					Toaster.showToast(getApplicationContext(),
							  	      Toaster.SUCCESS, R.string.category_updated);
					finish();
				} else {
					Toaster.showToast(getApplicationContext(),
							  		  Toaster.ERROR, R.string.category_not_updated);
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
		setContentView(R.layout.activity_category_edit);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		edtxName = (EditText) findViewById(R.id.categoryForm_edtxName);
		
		
		
		
		
		/*
		Button bttnUpdate = (Button)findViewById(R.id.categoryEdit_bttnUpdate);
		bttnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = updateCategoryInDB();
				if ( i == 1 ) {
					Toast.makeText(getApplicationContext(),
							R.string.category_updated, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), 
							R.string.category_not_updated, Toast.LENGTH_SHORT).show();
				}
		}});*/

		assignCategoryFromDB(getIdFromBundle());
		
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


	private void assignCategoryFromDB(int categoryId) {
		Log.i(LOG_TAG, "int getIdFromBundle(int itemId) | " + categoryId);		
		CategoriesDataSourceSelector categoriesData = 
				new CategoriesDataSourceSelector(getApplicationContext());
        category = categoriesData.getCategory(categoryId);
        // termine l'activité si la Category n'existe pas dans la DB
        if ( category == null ) {
        	Toaster.showToast(getApplicationContext(),
					  		  Toaster.ERROR,
					  		  R.string.category_does_not_exist);
        	finish();
        }
	}

	
	private int updateCategoryInDB() {
		Log.i(LOG_TAG, "int updateCategoryInDB()");
		
		Category c = new Category();
		c.setId    (category.getId());
		c.setName  (edtxName.getText().toString());
		
		CategoriesDataSourceSelector categoriesData = 
				new CategoriesDataSourceSelector(getApplicationContext());
		int i = categoriesData.update(c);
		Log.i(LOG_TAG, "int updateCategoryInDB() | return : " + i);
		return i;
	}

	
	private void fillFields() {
		if ( category != null ) {		
			edtxName.setText(category.getName());
		}		
	}
}
