package be.arno.crud.items;
/*
import java.util.ArrayList;
import java.util.List;

import be.arno.crud.myApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


public class ItemDBAdapter {

    public static String Lock = "dblock";
	
	public static final String DB_NAME       = "itemsDB";
	public static final String TABLE_ITEMS   = "items";
	public static final String COLUMN_ID     = "_id";
	public static final String COLUMN_NAME   = "name";
	public static final String COLUMN_DATE   = "date";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_BOOL   = "bool";
	public static final String COLUMN_IMAGE  = "image";
	public static final int VERSION_NUMBER = 4;
	
	public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_DATE, COLUMN_RATING, COLUMN_BOOL, COLUMN_IMAGE};
	
	private ItemDBHelper itemDBHelper;
	private Context context;
	private SQLiteDatabase db;


	public ItemDBAdapter(Context context) {
		this.context = context;
	}


	public ItemDBAdapter openWritable() {
			itemDBHelper = new ItemDBHelper(context, DB_NAME, null, VERSION_NUMBER);
			db = itemDBHelper.getWritableDatabase();
			return this;
	}


	public ItemDBAdapter openReadable() {
		itemDBHelper = new ItemDBHelper(context, DB_NAME, null, VERSION_NUMBER);
		db = itemDBHelper.getReadableDatabase();
		return this;
	}
	

	public void close() {
		db.close();
		itemDBHelper.close();
	}

	/*
	public long insert(Item item) {
			if ( item.isValid() ) {
				ContentValues valeurs = new ContentValues();
				valeurs.put(COLUMN_NAME, item.getName());
				valeurs.put(COLUMN_DATE, item.getDate());
				valeurs.put(COLUMN_RATING, item.getRating());
				valeurs.put(COLUMN_BOOL, item.getBool());
				valeurs.put(COLUMN_IMAGE, item.getByteArrayImage());
				long l = -1;
				l = db.insert(TABLE_ITEMS, null, valeurs);			
				return l;
			}
		return -1;
	}*/	
	
	/*
	public long insert(ArrayList<Item> items) {
		
         db.beginTransaction();
         for (int i = 0; i < items.size(); i++) {
        	 insert(items.get(i));
         }
         db.setTransactionSuccessful();	
         db.endTransaction();
         Log.i("insert array", "array inserted");
         
		return -1;
	}*/

	/*
	public long update(Item item) {
		if ( item.isValid() ) {
			ContentValues valeurs = new ContentValues();
			valeurs.put(COLUMN_NAME, item.getName());
			//valeurs.put(COLUMN_DATE, item.getDate());
			valeurs.put(COLUMN_RATING, item.getRating());
			valeurs.put(COLUMN_BOOL, item.getBool());
			valeurs.put(COLUMN_IMAGE, item.getByteArrayImage());
			// TODO : sécuriser des injections SQL ?
			return db.update(TABLE_ITEMS, valeurs, COLUMN_ID + " = " + item.getId(), null);
		}
		return -1;
	}*/

	/* -ORM
	public int getCount() {
		// TODO : sécuriser des injections SQL
		Cursor c = db.query(TABLE_ITEMS, new String[] {COLUMN_ID}, null, null, null, null, null, null);
		return c.getCount();				
	}*/

	
	// - ORM
	/*
	// return Item si trouvé, null si non trouvé
	public Item getItemById(int id) {
		Cursor c = getCursorItemById(id);
		if (c.getCount() != 0) { return cursorToItem(c);}
		else                   { return null; }
	}*/

	/*
	public Cursor getCursorItemById(int id) {
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, COLUMN_ID + " = ? ", new String[] {"" + id}, null, null, null, null);
		c.moveToFirst();
		return c;
	}*/

	/*
	private Item cursorToItem(Cursor c) {
		Item item = new Item();
		item.setId            (c.getInt   (c.getColumnIndex(COLUMN_ID)));
		item.setName          (c.getString(c.getColumnIndex(COLUMN_NAME)));
		//item.setDate          (c.getString(c.getColumnIndex(COLUMN_DATE)));
		item.setRating        (c.getFloat (c.getColumnIndex(COLUMN_RATING)));		
		item.setBool          (c.getInt   (c.getColumnIndex(COLUMN_BOOL)));
		item.setByteArrayImage(c.getBlob  (c.getColumnIndex(COLUMN_IMAGE)));
		return item;
	}*/

	/* -ORM
	private Item cursorToItemLight(Cursor c) {
		Item item = new Item();
		item.setId            (c.getInt   (c.getColumnIndex(COLUMN_ID)));
		item.setName          (c.getString(c.getColumnIndex(COLUMN_NAME)));
		item.setDate          (c.getString(c.getColumnIndex(COLUMN_DATE)));
		item.setBool          (c.getInt   (c.getColumnIndex(COLUMN_BOOL)));
		return item;
	}*/

	/* -ORM
	// retourne List<Items> vide si 
	public ArrayList<Item> getAll() {
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor c = getCursorAll();
		int i = 0;
		while ( i < c.getCount() ) {
			items.add(cursorToItem(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/

	/* -ORM
	// retourne List<Items> vide si 
	public ArrayList<Item> getAllLight() {
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor c = getCursorAllLight();
		int i = 0;
		while ( i < c.getCount() ) {
			items.add(cursorToItemLight(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/
	/*
	public Cursor getCursorAll() {
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, null, null, null, null, null, "1000");
		c.moveToFirst();
		return c;
	}*/
	/*
	public Cursor getCursorAllLight() {
		Cursor c = db.query(TABLE_ITEMS, new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DATE, COLUMN_BOOL }, null, null, null, null, null, "1000");
		c.moveToFirst();
		return c;
	}*/
	
	/*
	public ArrayList<Item> getOnlyWithDate() {
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, "DATE IS NOT NULL", null, null, null, null);
		int i = 0;
		c.moveToFirst();
		while ( i < c.getCount() ) {
			items.add(cursorToItem(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/

	/*
	public ArrayList<Item> getSearchOnName(String search) {
		ArrayList<Item> items = new ArrayList<Item>();
		// Secured query
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, "NAME LIKE ?", new String[] {"%"+search+"%"}, null, null, null);
		int i = 0;
		c.moveToFirst();
		while ( i < c.getCount() ) {
			items.add(cursorToItem(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/

	/*
	public ArrayList<Item> getSearchOnYear(String search) {
		ArrayList<Item> items = new ArrayList<Item>();
		// Secured query
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, "strftime('%Y',DATE) = ?", new String[] {search}, null, null, null);
		int i = 0;
		c.moveToFirst();
		while ( i < c.getCount() ) {
			items.add(cursorToItem(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/

	/*
	public ArrayList<Item> getOnlyBool(int bool) {
		ArrayList<Item> items = new ArrayList<Item>();
		// Secured query
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, "bool = ?", new String[] {""+bool}, null, null, null);
		int i = 0;
		c.moveToFirst();
		while ( i < c.getCount() ) {
			items.add(cursorToItem(c));
			c.moveToNext();
			i = i + 1;
		}
		return items;
	}*/

	/* -ORM
	public void delete(Item item) {
		int i = 0;
		// TODO : sécuriser des injections SQL
		i = db.delete(TABLE_ITEMS, COLUMN_ID + " = " + item.getId(), null);
		Log.i("db.delete", Integer.toString(i));
	}*/

	/* -ORM
	public void deleteAll() {
		db.delete(TABLE_ITEMS, null, null);
	}*/

	/* -ORM
	public Item getFirst() {
		Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, null, null, null, null, null, " 1 ");
		if ( c.getCount() != 0 ) {
			c.moveToFirst();
			return cursorToItem(c);
		}
		else {
			return null;
		}
	}*/
//}
