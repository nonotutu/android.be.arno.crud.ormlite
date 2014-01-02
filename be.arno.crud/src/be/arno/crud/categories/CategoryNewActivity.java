package be.arno.crud.categories;

import be.arno.crud.R;
import be.arno.crud.Toaster;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;







public class CategoryNewActivity extends Activity {

	private static final String LOG_TAG = "CategoryNewActivity";
	
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
				int i = createCategoryInDB();
				if ( i == 1 ) {
					Toaster.showToast(getApplicationContext(),
							Toaster.SUCCESS, R.string.category_created);
					finish();
				} else {
					Toaster.showToast(getApplicationContext(),
							Toaster.ERROR, R.string.category_not_created);
				}
				return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_new);

		Log.i(LOG_TAG, "onCreate");
		
		edtxName = (EditText)findViewById(R.id.categoryForm_edtxName);
		
		
		
		
		
		
		/*
		Button bttnSave = (Button)findViewById(R.id.categoryNew_bttnCreate);
		bttnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "bttnSave.onClick");
				long l = create();
				Log.i(LOG_TAG, "return code: " + l);
				if ( l == 1 ) {
					Toast.makeText(getApplicationContext(),
							R.string.category_created, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.category_not_created, Toast.LENGTH_SHORT).show();
				}
		}});*/
	}

	private int createCategoryInDB() {
		Log.i(LOG_TAG, "int createCategoryInDB()");
		
		Category category = new Category();
		category.setName(edtxName.getText().toString());
		
		CategoriesRepository categoriesRepository = 
				new CategoriesRepository(getApplicationContext());
		return categoriesRepository.create(category);
	}
}
