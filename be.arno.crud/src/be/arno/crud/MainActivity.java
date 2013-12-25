package be.arno.crud;

import be.arno.crud.R;
import be.arno.crud.items.ItemIndexActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button bttnItems = (Button)findViewById(R.id.main_bttnItems);
		Button bttnClose = (Button)findViewById(R.id.main_bttnClose);
		
		bttnItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ItemIndexActivity.class);
				startActivity(i);
			}
		});

		bttnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		/**
		
		ImageView imvw = (ImageView)findViewById(R.id.main_imvwImage);
		imvw.setScaleType(ScaleType.FIT_XY);
		


		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p001);
		
		Item item = new Item();
		item.setName("Arnaud");
		item.setImage(mBitmap);
		
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());
		itemAdapter.openWritable();

		itemAdapter.insert(item);
		
		itemAdapter.close();

		/**
		
		ItemDBAdapter itemAdapter = new ItemDBAdapter(getApplicationContext());

		itemAdapter.openReadable();
		
		Item item = itemAdapter.getFirst();
		
		Log.i("item", item.getName());
		Log.i("item", item.getImage().toString());

		imvw.setImageBitmap(item.getImage());
		
		itemAdapter.close();
		*/
	}
}
