package be.arno.crud.items;

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
import android.widget.Toast;


public class ItemListActivity extends Activity {

	private static final String LOG_TAG = "ItemListActivity";
	
	private int categoryId;
	
	// Contiendra le texte et l'ID du filtre
	private ListFilter listFilter;
	
	// Adapter de _listFilter_
	private ArrayAdapter<ListFilter> filterListArrayAdapter;
	
	// Autres views
	private ListView lsvwList;  // Liste
	private Button bttnFilter;  // Bouton du filtre
	private TextView txvwCount; // Nombre de résultats
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		Log.i(LOG_TAG, "void onCreate(Bundle)");
		
		categoryId = getCategoryIdFromParams();
		
		txvwCount = (TextView)findViewById(R.id.itemList_txvwCount);
		
		// Initialisation de la liste des filtres
		initFilter();

		// Initialisation du filtre par défaut
		listFilter = filterListArrayAdapter.getItem(0);
		
		
		// Button du filtre, redirige vers la métode de sélection du filtre
		bttnFilter = (Button)findViewById(R.id.itemList_bttnFilter);
		bttnFilter.setOnClickListener(
				new OnClickListener() {			
					@Override
					public void onClick(View v) {
						AlertDialog.Builder ad = dialogFilterSelect();
						ad.show();
				}});
		
		
		Button bttnNew = (Button)findViewById(R.id.itemList_bttnNew);
		bttnNew.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), ItemNewActivity.class);
						intent.putExtra("CATEGORY_ID", categoryId);
						startActivity(intent);
				}});

		
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
		lsvwList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				Item item = (Item)lsvwList.getItemAtPosition(position);
				new AlertDialog.Builder(ItemListActivity.this)
							   .setMessage(getString(R.string.id) + " " + item.getId())
							   .show();
				return true;
			}});
	}

	
	// Met à jour la liste au _start_ de l'_activity_
	@Override
	protected void onStart() {
		super.onStart();
		fillList(getListFromDB());
	}


	// Récupère la liste selon le _listFilter_ depuis la DB
	private ArrayList<Item> getListFromDB() {

		ArrayList<Item> items = null;
		// ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext()); -ORM
		// itemAdapter.openReadable(); -ORM
		ItemsRepository repos = new ItemsRepository(getApplicationContext());
		
		switch(listFilter.getRsql()) {
			case 1:
				items = (ArrayList<Item>) repos.getOnlyWithDateLight(); // ORM
				// itemAdapter.getOnlyWithDate(); -ORM
				break;
			case 2:
				items = (ArrayList<Item>) repos.getOnlyBoolLight(1); // ORM
				// items = itemAdapter.getOnlyBool(1); -ORM
				break;
			case 3:
				items = (ArrayList<Item>) repos.getOnlyBoolLight(0); // ORM
				// items = itemAdapter.getOnlyBool(0); -ORM
				break;
			default:
				items = (ArrayList<Item>) repos.getAllLight(categoryId); // ORM
				//items = itemAdapter.getAllLight(); -ORM
				break;
			}
			
		// itemAdapter.close(); -ORM
		return items;		
	}

	
	// Affiche la liste filtrée dans le ListView
	private void fillList(ArrayList<Item> items) {
		
		// Liste personnalisée :
		lsvwList.setAdapter(new ItemCustomListAdapter(this, items));

		txvwCount.setText(getString(R.string.items_found) + ": " + items.size());
		bttnFilter.setText(getString(R.string.filter) + ": " + listFilter.getName());
	}
	

	// Dialog du choix du filtre, depuis _filterListArrayAdapter_
	//public void filterChoice() {
	public AlertDialog.Builder dialogFilterSelect() {
	
		AlertDialog.Builder adb = new AlertDialog.Builder(ItemListActivity.this);
        
		adb.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                }});
		// Ne pas confondre l'int de la list et l'int de l'adapter
        adb.setSingleChoiceItems(filterListArrayAdapter, listFilter.getRsql(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	listFilter = filterListArrayAdapter.getItem(which);
                		dialog.dismiss();
                		fillList(getListFromDB());
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
	

	// Initialisation de l'adapter de la liste des _listFilter_
	public void initFilter() {
		filterListArrayAdapter = new ArrayAdapter<ListFilter>(
                ItemListActivity.this,
                android.R.layout.select_dialog_singlechoice);
		
        filterListArrayAdapter.add(new ListFilter("Tous", 0));
        filterListArrayAdapter.add(new ListFilter("Avec date", 1));
        filterListArrayAdapter.add(new ListFilter("Booléen Oui", 2));
        filterListArrayAdapter.add(new ListFilter("Booléen Non", 3));
	}


	private int getCategoryIdFromParams() {

		Bundle extra = this.getIntent().getExtras();
		if ( extra != null ) {
			return extra.getInt("CATEGORY_ID");
		}
		return -1;
	}
}
