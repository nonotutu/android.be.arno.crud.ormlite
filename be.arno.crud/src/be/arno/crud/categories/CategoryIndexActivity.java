package be.arno.crud.categories;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import be.arno.crud.App;
import be.arno.crud.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryIndexActivity extends Activity {

	private static final String LOG_TAG = "CategoryIndexActivity";
	
	private TextView txvwCount;
	
	@Override
	protected void onRestart() {
		super.onRestart();
		setCount();
	}
	
	private void setCount() {
		CategoriesRepository repos;
        repos = new CategoriesRepository(this);
        long count = repos.getCount();
		txvwCount.setText(""+count);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_index);

		txvwCount = (TextView)findViewById(R.id.categoryIndex_txvwCount);
	
		setCount();
		

		Button bttnNew = (Button)findViewById(R.id.categoryIndex_bttnNew);
		bttnNew.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), CategoryNewActivity.class);
					startActivity(i);
				}
			}
		);
	

		Button bttnList = (Button)findViewById(R.id.categoryIndex_bttnList);
		bttnList.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), CategoryListActivity.class);
					startActivity(i);
				}
			}
		);
	}
}