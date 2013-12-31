package be.arno.crud.categories;

import be.arno.crud.R;
import be.arno.crud.Helper;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CategoryNewActivity extends Activity {

	private static final String LOG_TAG = "CategoryNewActivity";
	
	private EditText edtxName;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_new);

		Log.i(LOG_TAG, "onCreate");
		
		edtxName = (EditText)findViewById(R.id.categoryForm_edtxName);
		
		Button bttnSave = (Button)findViewById(R.id.categoryNew_bttnCreate);
		bttnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "bttnSave.onClick");
				long l = create();
				Log.i(LOG_TAG, "return code: " + l);
				// TODO : vérifier tous les codes de retour
				if ( l == -1 ) {
					Toast.makeText(getApplicationContext(), R.string.category_not_created, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.category_created, Toast.LENGTH_SHORT).show();
					finish();
				}
		}});
	}

	private long create() {
		
		Category category = new Category();
		category.setName(edtxName.getText().toString());
		
		CategoriesRepository repos = new CategoriesRepository(getApplicationContext());
		return repos.create(category);
	}
	
}
