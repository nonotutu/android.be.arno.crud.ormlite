package be.arno.crud.items;

import be.arno.crud.PairIdName;
import be.arno.crud.R;
import be.arno.crud.categories.CategoriesDataSourceSelector;
import be.arno.crud.categories.CategoriesRepository;
import be.arno.crud.categories.Category;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class ItemListActivity extends Activity {

	private static final String LOG_TAG = "ItemListActivity";
	
	private Category category;
	private long[] limitoffset;
	private ArrayList<Item> items;
	
	// Contiendra le texte et l'ID du filtre
	private PairIdName filter;
	
	// Adapter de _listFilter_
	private ArrayAdapter<PairIdName> filtersArrayAdapter;
	
	private ListView lsvwList;  // Liste
	private TextView txvwCount; // Nombre de résultats
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_filter_new, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	        case R.id.action_new:
				Intent intent = new Intent(getApplicationContext(), ItemNewActivity.class);
				intent.putExtra("CATEGORY_ID", category.getId());
				startActivity(intent);
	        	return true;
	        case R.id.action_filter:
	        	AlertDialog.Builder alertDialog = dialogFilterSelect();
	        	alertDialog.show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(menuItem);
	    }
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		Log.i(LOG_TAG, "void onCreate(Bundle)");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		assignCategoryFromBundleThenDB();
		assignLimitoffset();
		
		txvwCount = (TextView)findViewById(R.id.itemList_txvwCount);
		
		// Initialisation de la liste des filtres
		initFilters();

		// Initialisation du filtre par défaut
		filter = filtersArrayAdapter.getItem(0);
		
		
		lsvwList = (ListView)findViewById(R.id.itemList_lsvwList);
		lsvwList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Intent intent = new Intent(getApplicationContext(), ItemShowActivity.class);

				// TODO : mettre ailleurs
				ArrayList<Integer> ids = new ArrayList<Integer>();
				int c = lsvwList.getCount();
				
				for ( int i = 0 ; i < c ; i+=1 ) {
					ids.add( ((Item)lsvwList.getItemAtPosition(i)).getId() );
				}
				
				intent.putExtra("POSITION_IN_IDS", position);
				intent.putExtra("ARRAY_IDS", ids);
				
				startActivity(intent);
			}});

		
		// ListView onLongClick, popup l'ID
		lsvwList.setOnItemLongClickListener(
				new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
						Item item = (Item)lsvwList.getItemAtPosition(position);
						new AlertDialog.Builder(ItemListActivity.this)
								.setMessage(getString(R.string.id) + " " + item.getId())
								.show();
						return true;
		}});
		
		ImageButton bttn_previous = (ImageButton)findViewById(R.id.itemList_bttnPrevious);
		bttn_previous.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if ( limitoffset[1] > 0 ) {
							limitoffset[1] -= limitoffset[0];
							onStart();
						}
		}});
		
		ImageButton bttn_next = (ImageButton)findViewById(R.id.itemList_bttnNext);
		bttn_next.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if ( items.size() >= limitoffset[0] ) {
							limitoffset[1] += limitoffset[0];
							onStart();
						}
		}});
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		// this.setTitle(category.getName());
		
		assignListFromDB();
		fillList(items);
	}


	private void assignListFromDB() {

		ItemsDataSourceSelector itemsData = new ItemsDataSourceSelector(getApplicationContext());
		
		switch(filter.getId()) {
			case 1:
				items = (ArrayList<Item>) itemsData.getOnlyWithDate_light(category.getId(), limitoffset);
				break;
			case 2:
				items = (ArrayList<Item>) itemsData.getOnlyBool_light(category.getId(), 1, limitoffset);
				break;
			case 3:
				items = (ArrayList<Item>) itemsData.getOnlyBool_light(category.getId(), 0, limitoffset);
				break;
			default:
				items = (ArrayList<Item>) itemsData.getAll_light(category.getId(), limitoffset);
				break;				
			}
	}

	
	// Affiche la liste filtrée dans le ListView
	private void fillList(ArrayList<Item> items) {
		
		// Liste personnalisée :
		lsvwList.setAdapter(new ItemCustomListAdapter(this, items));

		txvwCount.setText(getString(R.string.items_found) + ": " + items.size());
		//bttnFilter.setText(getString(R.string.filter) + ": " + filter.getName());
	}
	

	// Dialog du choix du filtre, depuis _filterListArrayAdapter_
	public AlertDialog.Builder dialogFilterSelect() {
	
		AlertDialog.Builder adb = new AlertDialog.Builder(ItemListActivity.this);
        
		adb.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                }});
		// Ne pas confondre l'int de la list et l'int de l'adapter
        adb.setSingleChoiceItems(filtersArrayAdapter, filter.getId(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	filter = filtersArrayAdapter.getItem(which);
                		dialog.dismiss();
                		limitoffset[1] = 0;
                		onStart();
                }});
        /* TODO : NOT ok pour API 16
        adb.setOnDismissListener(
        		new OnDismissListener() {
        			@Override
        			public void onDismiss(DialogInterface dialog) {
        				fillList(getList());				
        		}});*/
		return adb;
        }
	

	// Initialisation de l'adapter de la liste _filters_
	public void initFilters() {
		filtersArrayAdapter = new ArrayAdapter<PairIdName>(
                ItemListActivity.this,
                android.R.layout.select_dialog_singlechoice);
		
        filtersArrayAdapter.add(new PairIdName(0, "Tous"));
        filtersArrayAdapter.add(new PairIdName(1, "Avec date"));
        filtersArrayAdapter.add(new PairIdName(2, "Booléen Oui"));
        filtersArrayAdapter.add(new PairIdName(3, "Booléen Non"));
	}


	private void assignLimitoffset() {
		Log.i(LOG_TAG, "void assignLimitoffsetFromBundle()");
			
		if (limitoffset == null) {	
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());			
			limitoffset = new long[] {Long.parseLong(settings.getString("searchStep", "100")),0};
		}
	}
	
	
	private void assignCategoryFromBundleThenDB() {
		Log.i(LOG_TAG, "void assignCategoryFromBundleThenDB()");
		int catId = -1;
		
		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			catId = extra.getInt("CATEGORY_ID");
		}
		
		CategoriesDataSourceSelector categoriesData =
				new CategoriesDataSourceSelector(getApplicationContext());
		category = categoriesData.getCategory(catId);
	}
}
