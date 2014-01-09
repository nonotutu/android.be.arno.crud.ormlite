package be.arno.crud.items;

import java.util.List;

import be.arno.crud.App;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ItemsDataSourceSelector {

	private static final String LOG_TAG = "ItemsDataSourceSelector";

	private static final int DS_LOCAL = 1;
	private static final int DS_HTTP = 2;

	private int dataSource = 0;
	
    private ItemsRepository repos;
    private ItemsServer server;
    
    
    public ItemsDataSourceSelector(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		
		String dataSource = settings.getString("dataSource", null);

		if ( dataSource.equals("local")) {
			this.dataSource = DS_LOCAL;
	    	this.repos = new ItemsRepository(context);			
		}
		if ( dataSource.equals("httpServer")) {
			this.dataSource = DS_HTTP;
	    	this.server = new ItemsServer(context);
		}

    }

    
    /** has to return the number of successfully created rows */
    public int create(Item item) {
    	return repos.create(item);
    }
    
    
    /** has to return the number of successfully updated rows */
    public int update(Item item) {
    	return repos.create(item);
    }

    /*
    public int delete(Item item) {
    	return repos.create(item);
    }*/

    
    public int delete(int id) {
    	return repos.delete(id);
    }

    
    public Item getById(int id) {
    	return repos.getById(id);
    }

    
    // TODO
    public List<Item> getByIds(Integer... ids) {
    	// return repos.getItemById(id);
    	return null;
    }
    
    /*
    public List<Item> getAll() {
    	return repos.getAll();
    }*/
 
    
    public int deleteAll() {
    	return repos.deleteAll();
    }
    
    
    public int deleteAll(int categoryId) {
    	return repos.deleteAll(categoryId);
    }
    		
    // TODO : codes de retour
    public long getCount() {
    	
    	switch(dataSource) {
    	case DS_LOCAL:
        	return repos.getCount();
    	case DS_HTTP:
    		return server.getCount();
    	}
    	
    	return -4;
    }
   
    /*
    public long getCount(int categoryId) {
    	return repos.getCount(categoryId);
    }*/

    public long getCount(int categoryId, long[] limitoffset) {
    	
    	switch(dataSource) {
    	case DS_LOCAL:
    		//if ( limitoffset == null ) return getCount(categoryId);
        	return repos.getCount(categoryId, limitoffset);
    	case DS_HTTP:
    		return 8888;
    	}
    	return -1;
    }
    
    /*
    public List<Item> getAll_light() {
    	return repos.getAll_light();
    }*/
  
    /*
    public List<Item> getAll_light(int categoryId) {
    	return repos.getAll_light(categoryId);
    }*/
    
    public List<Item> getAll_light(int categoryId, long[] limitoffset) {
    	
    	switch(dataSource) {
    	case DS_LOCAL:
    		return repos.getAll_light(categoryId, limitoffset);
    	case DS_HTTP:
    		return server.getAll_light(categoryId, limitoffset);
    	}
    	return null;
    }
    

    public List<Item> getSearchOnYear_light(String searchYear, long[] limitoffset) {
    	return repos.getSearchOnYear_light(searchYear, limitoffset);
    }

 

    public List<Item> getSearchOnName_light(String searchName, long[] limitoffset) {
    	return repos.getSearchOnName_light(searchName, limitoffset);
    }

    
    public List<Item> getOnlyWithDate_light(int categoryId, long[] limitoffset) {
    	return repos.getOnlyWithDate_light(categoryId, limitoffset);
    }
    

    public List<Item> getOnlyBool_light(int categoryId, int bool, long[] limitoffset) {
    	return repos.getOnlyBool_light(categoryId, bool, limitoffset);    	
    }  
    
}