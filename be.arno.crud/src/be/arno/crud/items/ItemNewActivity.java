package be.arno.crud.items;

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

public class ItemNewActivity extends Activity {

	private static final String LOG_TAG = "ItemNewActivity";
	
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

		Log.i(LOG_TAG, "onCreate");
		
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
				Log.i(LOG_TAG, "bttnSave.onClick");
				long l = create();
				Log.i(LOG_TAG, "return code: " + l);
				// TODO : vérifier tous les codes de retour
				if ( l == -1 ) {
					Toast.makeText(getApplicationContext(), R.string.item_not_created, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.item_created, Toast.LENGTH_SHORT).show();
					finish();
				}
		}});
	}

	private long create() {
		
		String date = null;
		if ( swchDate.isChecked() )		
			date = Helper.dateInts2String(dtpkDate.getYear(), dtpkDate.getMonth(), dtpkDate.getDayOfMonth());
		
		Item item = new Item();
		item.setName(edtxName.getText().toString());
		item.setDate(date);
		item.setRating(rtbrRating.getRating());
		item.setBool(tgbtBool.isChecked()?1:0);		
		// TODO : i.setImage
		
		ItemsRepository repos = new ItemsRepository(getApplicationContext());
		return repos.create(item);
	}
	
}
