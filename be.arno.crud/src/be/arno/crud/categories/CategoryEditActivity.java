package be.arno.crud.categories;

import be.arno.crud.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;


public class CategoryEditActivity extends Activity {

	private Category category;
	
	private EditText edtxName;
	
	@Override
	protected void onRestart() {
		super.onRestart();
		fillFields();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_edit);

		edtxName =   (EditText)    findViewById(R.id.categoryForm_edtxName);
		
		Button bttnUpdate = (Button)findViewById(R.id.categoryEdit_bttnUpdate);
		bttnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long l = updateCategoryInDB();
				if ( l == 1 ) {
					Toast.makeText(getApplicationContext(), "Category updated", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Category not updated", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// récupérer l'ID dans le Bundle
		int id = getIdFromParams();
		
		// récupérer l'item de la DB
		getCategoryFromDB(id);
		
		fillFields();
	}


	private int getIdFromParams() {

		int id = -1;
		
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) id = extra.getInt("ID");
		return id;
		
	}


	private void getCategoryFromDB(int id) {
		
		CategoriesRepository categoryRepository = new CategoriesRepository(getApplicationContext());
        category = categoryRepository.getCategoryById(id);

	}
	

	private long updateCategoryInDB() {
		
		Category c = new Category();
		c.setId    (category.getId());
		c.setName  (edtxName.getText().toString());
		
		CategoriesRepository repos = new CategoriesRepository(getApplicationContext());
		return repos.update(c);
		
	}

	
	
	private void fillFields() {
		if ( category != null ) {		
			edtxName.setText(category.getName());
		} else {
			Toast.makeText(getApplicationContext(), "Category doesn't exist", Toast.LENGTH_LONG).show();
			finish();
		}
	}
}
