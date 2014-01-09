package be.arno.crud.categories;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import be.arno.crud.App;
import be.arno.crud.DatabaseManager;
import be.arno.crud.CatemDBHelper;
import be.arno.crud.items.Item;
import be.arno.crud.items.ItemsDataSourceSelector;
import be.arno.crud.items.ItemsRepository;
import be.arno.crud.items.ItemsServer;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class CategoriesDataSourceSelector {

	private static final String LOG_TAG = "CategoriesDataSourceSelector";

	private static final int DS_LOCAL = 1;
	private static final int DS_HTTP = 2;

	private int dataSource = 0;
	
    private CategoriesRepository repos;
    private CategoriesServer server;
    
    
    public CategoriesDataSourceSelector(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		
		String dataSource = settings.getString("dataSource", null);

		if ( dataSource.equals("local")) {
			this.dataSource = DS_LOCAL;
	    	this.repos = new CategoriesRepository(context);			
		}
		if ( dataSource.equals("httpServer")) {
			this.dataSource = DS_HTTP;
	    	this.server = new CategoriesServer(context);
		}

    }
    
    
    public int[] getAllIds() {
    	return repos.getAllIds();
    }
    
    
    /** has to return the number of successfully created rows */
    public int create(Category category) {
    	return repos.create(category);
    }
    

    /** has to return the number of successfully updated rows */
    public int update(Category category) {
    	return repos.update(category);
    }
    
    
    public int delete(int id) {
    	return repos.delete(id);
    }

    
    public int deleteAll() {
    	return repos.deleteAll();
    }

    
    public Category getCategory(int id) {
    	switch(dataSource){
    	case DS_LOCAL:
        	return repos.getCategoryById(id);
    	case DS_HTTP:
        	return server.getCategoryById(id);
    	}
    	return null;
    }
    
    
    public List<Category> getAll() {

    	switch(dataSource) {
    	case DS_LOCAL:
    	   	return repos.getAll();
    	case DS_HTTP:
    		return server.getAll();
    	}
    	
    	return null;
    }
 
        
    public long getCount() {
    	return repos.getCount();
    }

        
}