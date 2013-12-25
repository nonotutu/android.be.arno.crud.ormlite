package be.arno.crud.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.arno.crud.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class ItemSearchActivity extends Activity {

	// Adapter de la liste des _Item_
	private ArrayAdapter<Item> itemArrayAdapter;

	private EditText edtxSearch; // champ de recherche
	private ListView lsvwList;   // liste des résultats
	private TextView txvwCount;  // nombre d'_Item_ dans la liste
	//private Switch swchLive;

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("=> ItemSeachActivity", "onStart");
		fillList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_search);
		
		Log.i("=> ItemSearchActivity", "onCreate");

		edtxSearch = (EditText)findViewById(R.id.itemSearch_edtxSearch);
		lsvwList = (ListView)findViewById(R.id.itemSearch_lsvwList);
		txvwCount = (TextView)findViewById(R.id.itemSearch_txvwCount);
		final Switch swchLive = (Switch)findViewById(R.id.itemSearch_swchLive);
		
		edtxSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//Log.i("edtxSearch", "onTextChanged");
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//Log.i("edtxSearch", "beforeTextChanged");
			}
			@Override
			public void afterTextChanged(Editable s) {
				Log.i("edtxSearch", "afterTextChanged");
				// uniquement si la recherche en live est activée et que l'EditText n'est pas vide
				if ( swchLive.isChecked() == true && edtxSearch.getText().length() != 0)
					fillList();
			}
		});
		
		
		Button bttnSearch = (Button)findViewById(R.id.itemSearch_bttnSearch);
		bttnSearch.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("=> ItemSeachActivity", "bttnSearch.onClick");
						fillList();
					}
				}
			);

		Button bttnNew = (Button)findViewById(R.id.itemSearch_bttnNew);
		bttnNew.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("=> ItemSeachActivity", "bttnNew.onClick");
						Intent i = new Intent(getApplicationContext(), ItemNewActivity.class);
						startActivity(i);
					}
				}
			);

		lsvwList.setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
						Log.i("=> ItemSeachActivity", "lsvwList.onItemClick");
						Item item = (Item)lsvwList.getItemAtPosition(position);
						Intent intent = new Intent(getApplicationContext(), ItemShowActivity.class);
						intent.putExtra("ID", "" + item.getId());
						
						// TODO : mettre ailleurs
						ArrayList<Integer> ids = new ArrayList<Integer>();
						int c = itemArrayAdapter.getCount();
						
						for ( int i = 0 ; i < c ; i+=1 ) {
							ids.add(itemArrayAdapter.getItem(i).getId());
						}
						
						intent.putExtra("LAST", position);
						intent.putExtra("IDS", ids);
						startActivity(intent);
					}
				}
			);
	}


	// Get et affiche la liste dans le ListView
	private void fillList() {
		Log.i("=> ItemSearchActivity", "fillList");
		List<Item> items = getList(edtxSearch.getText().toString());
		itemArrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
		lsvwList.setAdapter(itemArrayAdapter);
		txvwCount.setText(getString(R.string.items_found) + ": " + items.size());
	}

	
	// Récupère la liste selon le champ de recherche depuis la DB via l'Adapter
	private List<Item> getList(String search) {

		Log.i("=> ItemSearchActivity", "getList");
		
		// Retire les leading & trailing spaces
		search = search.trim();

		// Retire les double spaces
		while ( search.contains("  ") )	search = search.replace("  ", " ");
		
		// Découpe la recherche à chaque espace
		String[] searchs = search.split(" ");
		
		// Ouvre la DB via d'Adapter
		List<Item> items = new ArrayList<Item>();
		// ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext()); -ORM
		// itemAdapter.openReadable(); -ORM

		ItemsRepository repos = new ItemsRepository(getApplicationContext()); // ORM

		// Boucle sur les 3 premiers mots
		int i = 0;
		while ( i < searchs.length && i <= 3 ) {
			String s = searchs[i].toString();
			// Si le mot fait 10 caractères ...
			if ( s.length() == 10 ) {
				Pattern pattern = Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d$");
				Matcher matcher = pattern.matcher(s);
				// ... et qu'il correspond à dddd-dd-dd ...
				if ( matcher.matches() ) {
				    // ... va chercher les items dont la date (non vérifiée) correspond dans la DB et les ajoute à la liste en cours
					items.addAll(repos.getSearchOnDateLight(s)); // ORM
				}
			}
			// Si le mot fait 7 caractères ...
			if ( s.length() == 7 ) {
				Pattern pattern = Pattern.compile("^\\d\\d\\d\\d-\\d\\d$");
				Matcher matcher = pattern.matcher(s);
				// ... et qu'il correspond à dddd-dd ...
				if ( matcher.matches() ) {
				    // ... va chercher les items dont l'année et le mois correspondent dans la DB et les ajoute à la liste en cours
					items.addAll(repos.getSearchOnYearMonthLight(s)); // ORM
				}
			}
			// Si le mot fait 4 caractères ...
			if ( s.length() == 4 ) {
				Pattern pattern = Pattern.compile("^\\d\\d\\d\\d$");
				Matcher matcher = pattern.matcher(s);
				// ... et qu'il correspond à un nombre de 4 chiffres ...
				if ( matcher.matches() ) {
				    // ... va chercher les items dont l'année correspond dans la DB et les ajoute à la liste en cours
					items.addAll(repos.getSearchOnYearLight(s)); // ORM
					//items.addAll(itemAdapter.getSearchOnYear(s)); //-ORM
				}
			}
			// Si le mot fait au moins 2 caractères ...
			if ( s.length() >= 2 )
				// ... va chercher les items contenant le mot dans la DB et jes ajoute à la liste en cours 
				items.addAll(repos.getSearchOnNameLight(s));
				// items.addAll(itemAdapter.getSearchOnName(s)); -ORM
			i = i + 1;
		}

		// Ferme la DB via l'Adapter
		// itemAdapter.close(); -ORM

		return removeDuplicates(items);
	}
	
	// Suppression des doublons, via une astucieuse double boucle qui, non seulement contente de m'avoir pris 2 heures de mon temps, m'a rappelé que les cours de principes de programmation étaient bien loins. Bordel.
	private List<Item> removeDuplicates(List<Item> items) {
		Log.i("=> ItemSearchActivity", "removeDuplicates");
		int j;
		int i = 0;
		while ( i < items.size() - 1 ) {
			j = i + 1;
			while ( j < items.size() ) {
				if ( items.get(i).getId() == items.get(j).getId() ) {
					items.remove(j);
				}
				else {
					j = j + 1;
				}
			}
		i = i + 1;
		}		
		return items;
	}

}
