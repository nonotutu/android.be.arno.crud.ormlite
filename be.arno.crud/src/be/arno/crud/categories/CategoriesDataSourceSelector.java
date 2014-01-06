package be.arno.crud.categories;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import be.arno.crud.App;
import be.arno.crud.DatabaseManager;
import be.arno.crud.CatemDBHelper;
import be.arno.crud.items.Item;
import be.arno.crud.items.ItemsDataSourceSelector;
import be.arno.crud.items.ItemsRepository;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class CategoriesDataSourceSelector {
	 
	private static final String LOG_TAG = "CategoriesDateSourceSelector";
	
    private CategoriesRepository repos;
    private Context context;
    
    
    
    
    public int[] getAllIds() {
    	return repos.getAllIds();
    }  
    
    
    public CategoriesDataSourceSelector(Context context) {
    	this.repos = new CategoriesRepository(context);
    	this.context = context;
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

    
	private void deleteNestedItems(int categoryId) {
		ItemsDataSourceSelector itemsData = new ItemsDataSourceSelector(context);
    	itemsData.deleteAll(categoryId);
	}
	
    
    public Category getCategory(int id) {
    	return repos.getCategoryById(id);
    }
    
    
    public List<Category> getAll() {
    	return repos.getAll();
    }
 
    
    public int deleteAll() {
    	return repos.deleteAll();
    }

        
    public long getCount() {
    	return repos.getCount();
    }
    
//    
//    public QueryBuilder<Category, Integer> getRawAllLight() {
//    	return categoriesDao.queryBuilder()
//        		.selectColumns(Category.COLUMN_NAME);
//    }
        
}