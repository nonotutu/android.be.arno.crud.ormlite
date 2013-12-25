package be.arno.crud.items;

import be.arno.crud.R;
import be.arno.crud.Helper;
import be.arno.crud.R.id;
import be.arno.crud.R.layout;
import be.arno.crud.R.menu;
import be.arno.crud.R.string;

/*
import java.text.SimpleDateFormat;
import java.util.Date;
*/

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
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

public class ItemNewActivity extends Activity {

	private EditText edtxName;
	private DatePicker dtpkDate;
	private Switch swchDate;
	private RatingBar rtbrRating;
	private ToggleButton tgbtBool;
	private ImageButton imbtImage;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_new);

		edtxName = (EditText)findViewById(R.id.itemForm_edtxName);
		dtpkDate = (DatePicker)findViewById(R.id.itemForm_dtpkDate);
		swchDate = (Switch)findViewById(R.id.itemForm_swchDate);
		rtbrRating = (RatingBar)findViewById(R.id.itemForm_rtbrRating);
		tgbtBool = (ToggleButton)findViewById(R.id.itemForm_tgbtBool);
		imbtImage = (ImageButton)findViewById(R.id.itemForm_imbtImage);
		
		Button bttnSave = (Button)findViewById(R.id.itemNew_bttnUpdate);
		bttnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long l = insertItemInDB();
				Log.i("Item/New", "Created: " + l);
				// TODO : vérifier tous les codes de retour
				if ( l == -1 ) {
					Toast.makeText(getApplicationContext(), R.string.item_not_created, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.item_created, Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
		
	}

	private long insertItemInDB() {
		
		String date = null;
		
		if ( swchDate.isChecked() )		
			date = Helper.dateInts2String(dtpkDate.getYear(), dtpkDate.getMonth(), dtpkDate.getDayOfMonth());
		
		//Item item = new Item();
		Item item = new Item();
		//i.setId(item.getId());
		item.setName(edtxName.getText().toString());
		item.setDate(date);
		item.setRating(rtbrRating.getRating());
		item.setBool(tgbtBool.isChecked()?1:0);		
		// TODO : i.setImage
		
		ItemsRepository repos = new ItemsRepository(getApplicationContext()); // ORM
		return repos.create(item); // ORM
		
		/* -ORM
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());
		itemAdapter.openWritable();
		l = itemAdapter.insert(item);
		itemAdapter.close();
		*/
		
		// return l;
	}

	
}
