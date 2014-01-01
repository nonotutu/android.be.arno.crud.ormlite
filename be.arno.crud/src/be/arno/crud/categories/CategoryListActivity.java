package be.arno.crud.categories;

import be.arno.crud.ListFilter;
import be.arno.crud.R;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class CategoryListActivity extends Activity {

	// Contiendra le texte et l'ID du filtre
	private ListFilter listFilter;

	// Liste non personnalisée :
	// Adapter de la liste des _Item_
	private ArrayAdapter<Category> categoryArrayAdapter;
	
	// Autres views
	private ListView lsvwList;  // Liste
	private Button bttnFilter;  // Bouton du filtre
	private TextView txvwCount; // Nombre de résultats
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Item/List", "onCreate");
		setContentView(R.layout.activity_category_list);
		
		txvwCount = (TextView)findViewById(R.id.categoryList_txvwCount);
		
		Button bttnNew = (Button)findViewById(R.id.categoryList_bttnNew);
		bttnNew.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(), CategoryNewActivity.class);
						startActivity(i);
				}});

		
		lsvwList = (ListView)findViewById(R.id.categoryList_lsvwList);
		lsvwList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Category category = (Category)lsvwList.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(), CategoryShowActivity.class);
				intent.putExtra("ID", category.getId());

				// TODO : mettre ailleurs
				ArrayList<Integer> ids = new ArrayList<Integer>();
				int c = lsvwList.getCount();
				
				for ( int i = 0 ; i < c ; i+=1 ) {
					ids.add(  ((Category)lsvwList.getItemAtPosition(i)).getId()  );
				}
				
				intent.putExtra("LAST", position);
				intent.putExtra("IDS", ids);
				
				startActivity(intent);
			}});

		// ListView onLongClick, popup l'ID
		lsvwList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				Category category = (Category)lsvwList.getItemAtPosition(position);
				new AlertDialog.Builder(CategoryListActivity.this).setMessage("@string/item_count"+category.getCountItems()).show();
				return true;
			}});
		
	}

	
	// Met à jour la liste au _start_ de l'_activity_
	@Override
	protected void onStart() {
		super.onStart();
		fillList(getList());
	}


	// Récupère la liste selon le _listFilter_ depuis la DB via l'adapter
	private ArrayList<Category> getList() {

		ArrayList<Category> categories = null;
		CategoriesRepository repos = new CategoriesRepository(getApplicationContext());
		categories = (ArrayList<Category>) repos.getAll();
		return categories;
	}

	
	// Affiche la liste filtrée dans le ListView
	private void fillList(ArrayList<Category> categories) {
		
		// Liste non personnalisée :
		categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
		lsvwList.setAdapter(categoryArrayAdapter);

		// Liste personnalisée :
		// lsvwList.setAdapter(new ItemCustomListAdapter(this, items));

		txvwCount.setText(getString(R.string.categories_found) + ": " + categories.size());
	}
	
}
