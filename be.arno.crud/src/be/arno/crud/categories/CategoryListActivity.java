package be.arno.crud.categories;

import be.arno.crud.R;
import be.arno.crud.items.ItemNewActivity;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;




public class CategoryListActivity extends Activity {

	private static final String LOG_TAG = "CategoryListActivity";

	
	
	// Liste non personnalisée :
	// Adapter de la liste des _Item_
	private ArrayAdapter<Category> categoryArrayAdapter;
	
	
	
	private ListView lsvwList;  // Liste
	private Button bttnFilter;  // Bouton du filtre
	private TextView txvwCount; // Nombre de résultats
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_new, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
   	        case R.id.action_new:
   				Intent intent = new Intent(getApplicationContext(), CategoryNewActivity.class);
   				startActivity(intent);
   	        	return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
		Log.i(LOG_TAG, "void onCreate(Bundle)");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		txvwCount = (TextView)findViewById(R.id.categoryList_txvwCount);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*Button bttnNew = (Button)findViewById(R.id.categoryList_bttnNew);
		bttnNew.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(), CategoryNewActivity.class);
						startActivity(i);
				}});*/

		
		lsvwList = (ListView)findViewById(R.id.categoryList_lsvwList);
		lsvwList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Intent intent = new Intent(getApplicationContext(), CategoryShowActivity.class);

				// TODO : mettre ailleurs
				ArrayList<Integer> ids = new ArrayList<Integer>();
				int c = lsvwList.getCount();
				
				for ( int i = 0 ; i < c ; i+=1 ) {
					ids.add( ((Category)lsvwList.getItemAtPosition(i)).getId() );
				}
				
				intent.putExtra("POSITION_IN_IDS", position);
				intent.putExtra("ARRAY_IDS", ids);
				
				startActivity(intent);
			}});

		
		// ListView onLongClick, popup sur le nombre d'éléments
		lsvwList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				Category category = (Category)lsvwList.getItemAtPosition(position);
				new AlertDialog.Builder(CategoryListActivity.this)
				               .setMessage(getString(R.string.item_count) + ": " /*+ category.getCountItems(getApplicationContext())*/)
				               .show();
				return true;
			}});
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		fillList(getListFromDB());
	}


	private ArrayList<Category> getListFromDB() {

		ArrayList<Category> categories = null;
		CategoriesDataSourceSelector categoriesData = new CategoriesDataSourceSelector(getApplicationContext());
		categories = (ArrayList<Category>) categoriesData.getAll();
		return categories;
	}

	
	
	
	
	
	
	
		
	
	
	
	
	
	
	
	
	
	// Affiche la liste filtrée dans le ListView
	private void fillList(ArrayList<Category> categories) {
		
		// Liste non personnalisée :
		categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
		lsvwList.setAdapter(categoryArrayAdapter);

		txvwCount.setText(getString(R.string.categories_found) + ": " + categories.size());
	}
	
}
