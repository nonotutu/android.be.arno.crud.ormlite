package be.arno.crud.categories;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import be.arno.crud.DatabaseManager;
import be.arno.crud.CatemDBHelper;
import be.arno.crud.items.Item;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class CategoriesRepository { // ORM
	 
	private static final String LOG_TAG = "CategoriesRepository";
	
    private CatemDBHelper db;
    Dao<Category, Integer> categoriesDao;
 
    
    public int[] getAllIds() {
    	
    	List<Category> listIds = null;
    	
        try {
            listIds = categoriesDao.queryBuilder()
            		.selectColumns(Category.COLUMN_ID).query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }

        if ( listIds != null && listIds.size() > 0 ) {
        	int[] intIds = new int[listIds.size()];
        	for ( int i = 0 ; i < listIds.size() ; i+=1 ) {
        		intIds[i] = listIds.get(i).getId();
        	}
        	return intIds;
        } else {
        	return null;
        }		
    }  
    
    
    public CategoriesRepository(Context context) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(context);
            categoriesDao = db.getCategoriesDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
 
    }
    
    // TODO : gérer codes retour
    public int create(Category category) {
    	Log.i(LOG_TAG, "public int create(Item)");
        try {
            return categoriesDao.create(category);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public int update(Category category) {
        try {
            return categoriesDao.update(category);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public int delete(Category category) {
        try {
            return categoriesDao.delete(category);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    
    public int delete(int id) { // for ORM
        try {
            return categoriesDao.deleteById(id);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    
    public Category getCategoryById(int id) {
        try {
            return categoriesDao.queryForId(id);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    
    
    public List<Category> getAll() {
        try {
            return categoriesDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
 
    
    public int deleteAll() {
    	try {
    		 categoriesDao.delete(categoriesDao.deleteBuilder().prepare());
    	} catch (SQLException e) {}
    	return 0;
    }

    
    public long getCount() {
    	try {
    		return categoriesDao.countOf();
    	} catch (SQLException e) {}
    	return 0;
    }
    
    public QueryBuilder<Category, Integer> getRawAllLight() {
    	return categoriesDao.queryBuilder()
        		.selectColumns(Category.COLUMN_NAME);
    }
        
}