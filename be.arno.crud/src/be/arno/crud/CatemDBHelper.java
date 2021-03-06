package be.arno.crud;

import java.sql.SQLException;

import org.w3c.dom.Comment;

import be.arno.crud.categories.Category;
import be.arno.crud.items.Item;

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
public class CatemDBHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String LOG_TAG = "CatemDBHelper";

    private static final String DATABASE_NAME = "catems";
    private static final int DATABASE_VERSION = 1;

	private Dao<Item, Integer>     itemsDao      = null;
	private Dao<Category, Integer> categoriesDao = null;
    private RuntimeExceptionDao<Item, Integer>     itemsRuntimeDao = null;
    private RuntimeExceptionDao<Category, Integer> categoriesRuntimeDao = null;
    
    
	public CatemDBHelper(Context context) { // ORM
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public CatemDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		
		try {
			TableUtils.createTable(connectionSource, Item.class);
			TableUtils.createTable(connectionSource, Category.class);
		} catch (SQLException e) { e.printStackTrace(); }
		
		//throw new RuntimeException(e); } // TODO : je ne comprends pas
        //RuntimeExceptionDao<Item, Integer> dao = getItemsDataDao();
        //Item item = new Item();
        //dao.create(item);
		
	}
	

	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
		Log.i(LOG_TAG, "void onUpgrade(SQLiteDatabase, ConnectionSource, int, int)");
		// TODO : ORMiser
		
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
	 
	public Dao<Category, Integer> getCategoriesDao() throws SQLException {	// ORM
		if (categoriesDao == null) {
			categoriesDao = getDao(Category.class);
		}
	    return categoriesDao;
	}
	
	 public RuntimeExceptionDao<Category, Integer> getCategoriesDataDao() { // ORM
        if (categoriesRuntimeDao == null) {
            categoriesRuntimeDao = getRuntimeExceptionDao(Category.class);
        }
        return categoriesRuntimeDao;
    }

	 
	 
	public void deleteAll() {
		
	}
	 
	 
	 @Override
	 public void close() { // ORM
		 super.close();
		 itemsDao = null;
	 }
	
}
