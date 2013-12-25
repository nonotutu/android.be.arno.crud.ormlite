package be.arno.crud.items;

import java.sql.SQLException;

import org.w3c.dom.Comment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
								  // ORM
public class ItemDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "items";
    private static final int DATABASE_VERSION = 1;

	private Dao<Item, Integer> itemsDao = null;	// ORM
    private RuntimeExceptionDao<Item, Integer> itemsRuntimeDao = null;	// ORM
    
    /* -ORM
	private static final String CREATE_REQUEST = " create table " +
			ItemDBAdapter.TABLE_ITEMS   + " ( " +
			ItemDBAdapter.COLUMN_ID     + " integer primary key autoincrement, " +
			ItemDBAdapter.COLUMN_NAME   + " text not null, " +
			ItemDBAdapter.COLUMN_DATE   + " text, " +
			ItemDBAdapter.COLUMN_RATING + " text, " +
			ItemDBAdapter.COLUMN_BOOL   + " int, " +
			ItemDBAdapter.COLUMN_IMAGE  + " blob ); ";
	*/
	
    /* -ORM
	private static final String UPGRADE_REQUEST_2 = " alter table " +
			ItemDBAdapter.TABLE_ITEMS +
			" ADD COLUMN " +
			ItemDBAdapter.COLUMN_RATING + " text " + " ; ";
	
	private static final String UPGRADE_REQUEST_3 = " alter table " +
			ItemDBAdapter.TABLE_ITEMS +
			" ADD COLUMN " +
			ItemDBAdapter.COLUMN_BOOL   + " int "  + " ; ";
	
	private static final String UPGRADE_REQUEST_4 = " alter table " +
			ItemDBAdapter.TABLE_ITEMS +
			" ADD COLUMN " +
			ItemDBAdapter.COLUMN_IMAGE  + " blob " + " ; ";
	*/
    
	public ItemDBHelper(Context context) { // ORM
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public ItemDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
											// ORM
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		
		 // db.execSQL(CREATE_REQUEST); // -ORM
		
		try { // ORM
			TableUtils.createTable(connectionSource, Item.class);
		} catch (SQLException e) { e.printStackTrace(); }
		
		//throw new RuntimeException(e); } // TODO : je ne comprends pas
        //RuntimeExceptionDao<Item, Integer> dao = getItemsDataDao();
        //Item item = new Item();
        //dao.create(item);
		
	}
	
											 // ORM
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
		Log.i("DatabaseHelper", "onUpgrade");
		
		// TODO : ORMiser
		
		/* -ORM
		if ( oldVersion < 2 )
			db.execSQL(UPGRADE_REQUEST_2);
		if ( oldVersion < 3 )
			db.execSQL(UPGRADE_REQUEST_3);
		if ( oldVersion < 4 )
			db.execSQL(UPGRADE_REQUEST_4);
			*/
	}
	

	public Dao<Item, Integer> getItemsDao() throws SQLException {	// ORM
		if (itemsDao == null) {
			itemsDao = getDao(Item.class);
		}
	    return itemsDao;
	}
	
	 public RuntimeExceptionDao<Item, Integer> getItemsDataDao() { // ORM
	        if (itemsRuntimeDao == null) {
	            itemsRuntimeDao = getRuntimeExceptionDao(Item.class);
	        }
	        return itemsRuntimeDao;
	    }
	 
	 @Override
	 public void close() { // ORM
		 super.close();
		 itemsDao = null;
	 }
	
}
