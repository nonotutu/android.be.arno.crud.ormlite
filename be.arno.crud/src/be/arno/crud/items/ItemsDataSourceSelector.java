package be.arno.crud.items;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class ItemsDataSourceSelector {

	private static final String LOG_TAG = "ItemsDataSourceSelector";

    private ItemsRepository repos;
    
    public ItemsDataSourceSelector(Context context) {
    	this.repos = new ItemsRepository(context);
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
    
    
    public List<Item> getAll() {
    	return repos.getAll();
    }
 
    
    public int deleteAll() {
    	return repos.deleteAll();
    }
    
    
    public int deleteAll(int categoryId) {
    	return repos.deleteAll(categoryId);
    }
    		

    public long getCount() {
    	return repos.getCount();
    }
   
    
    public long getCount(int categoryId) {
    	return repos.getCount(categoryId);
    }
    

    public List<Item> getAll_light() {
    	return repos.getAll_light();
    }
  

    public List<Item> getAll_light(int categoryId) {
    	return repos.getAll_light(categoryId);
    }
    
     
    public List<Item> getSearchOnYear_light(String searchYear) {
    	return repos.getSearchOnYear_light(searchYear);
    }

    
    public List<Item> getSearchOnYearMonth_light(String searchYearMonth) {
		return repos.getSearchOnYearMonth_light(searchYearMonth);
    }


    public List<Item> getSearchOnDate_light(String searchDate) {
    	return repos.getSearchOnDate_light(searchDate);
    }
    

    public List<Item> getSearchOnName_light(String searchName) {
    	return repos.getSearchOnName_light(searchName);
    }

    
    public List<Item> getOnlyWithDate_light(int categoryId) {
    	return repos.getOnlyWithDate_light(categoryId);
    }
    

    public List<Item> getOnlyBool_light(int categoryId, int bool) {
    	return repos.getOnlyBool_light(categoryId, bool);    	
    }  
    
}