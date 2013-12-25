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


public class ItemListActivity extends Activity {

	// Contiendra le texte et l'ID du filtre
	private ListFilter listFilter;
	
	// Adapter de _listFilter_
	private ArrayAdapter<ListFilter> filterListArrayAdapter;

	// Adapter de la liste des _Item_
	// private ArrayAdapter<Item> itemArrayAdapter;
	
	// Autres views
	private ListView lsvwList;  // Liste
	private Button bttnFilter;  // Bouton du filtre
	private TextView txvwCount; // Nombre de résultats
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Item/List", "onCreate");
		setContentView(R.layout.activity_item_list);
		
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
						Intent i = new Intent(getApplicationContext(), ItemNewActivity.class);
						startActivity(i);
				}});

		lsvwList = (ListView)findViewById(R.id.itemList_lsvwList);
		// ListView onClick, ouvre le show Item
		lsvwList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Item item = (Item)lsvwList.getItemAtPosition(position);
				Log.i("Item/List/ListView", "OnItemClick - item id: " + item.getId());
				Intent intent = new Intent(getApplicationContext(), ItemShowActivity.class);
				intent.putExtra("ID", "" + item.getId());

				// TODO : mettre ailleurs
				ArrayList<Integer> ids = new ArrayList<Integer>();
				int c = lsvwList.getCount();
				
				for ( int i = 0 ; i < c ; i+=1 ) {
					ids.add(  ((Item)lsvwList.getItemAtPosition(i)).getId()  );
				}
				
				intent.putExtra("LAST", position);
				intent.putExtra("IDS", ids);
				
				startActivity(intent);
			}});

		// ListView onLongClick, popup l'ID
		lsvwList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				Item item = (Item)lsvwList.getItemAtPosition(position);
				Log.i("Item/List/ListView", "OnItemLongClick - item id: " + item.getId());
				new AlertDialog.Builder(ItemListActivity.this).setMessage("ID: "+item.getId()).show();
				return true;
			}});
		
		// Peuple _items_ en fonction du _listFilter_
		// ArrayList<Item> items = getList();
		
		// Affiche la liste d'Items et le nom du filtre
		// supprimée car appelée au onStart;
		// fillList(items);
	}

	
	// Met à jour la liste au _start_ de l'_activity_
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("Item/List", "onStart");
		fillList(getList());
	}


	// Récupère la liste selon le _listFilter_ depuis la DB via l'adapter
	private ArrayList<Item> getList() {

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
				items = (ArrayList<Item>) repos.getAllLight(); // ORM
				//items = itemAdapter.getAllLight(); -ORM
				break;
			}
			
		// itemAdapter.close(); -ORM
		return items;		
	}

	
	// Affiche la liste filtrée dans le ListView
	private void fillList(ArrayList<Item> items) {
		
		// Liste non personnalisée :
		// itemArrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
		// lsvwList.setAdapter(itemArrayAdapter);

		// Liste personnalisée :
		lsvwList.setAdapter(new ItemCustomListAdapter(this, items));

		txvwCount.setText(getString(R.string.items_found) + ": " + items.size());
		bttnFilter.setText(getString(R.string.filter) + ": "
		           + listFilter.getName());
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
                		fillList(getList());
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
}
