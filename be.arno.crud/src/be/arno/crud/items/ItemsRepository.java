package be.arno.crud.items;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class ItemsRepository { // ORM
	 
	private static final String LOG_TAG = "ItemsRepository";
	
    private ItemDBHelper db;
    Dao<Item, Integer> itemsDao;
 
    
    public ItemsRepository(Context context) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(context);
            itemsDao = db.getItemsDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
 
    }
    
    // TODO : gérer codes retour
    public int create(Item item) {
    	Log.i(LOG_TAG, "public int create(Item)");
        try {
            return itemsDao.create(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public int update(Item item) {
        try {
            return itemsDao.update(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public int delete(Item item) {
        try {
            return itemsDao.delete(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    
    public int delete(int id) { // for ORM
        try {
            return itemsDao.deleteById(id);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    
    public Item getItemById(int id) {
        try {
            return itemsDao.queryForId(id);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    
    
    public List<Item> getAll() {
        try {
            return itemsDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
 
    
    public int deleteAll() {
    	try {
    		 itemsDao.delete(itemsDao.deleteBuilder().prepare());
    	} catch (SQLException e) {}
    	return 0;
    }

    
    public long getCount() {
    	try {
    		return itemsDao.countOf();
    	} catch (SQLException e) {}
    	return 0;
    }
    
    public Cursor getCursorAllLight() { // for ORM
    	/* PUTIN !
		When you are building your own query with ORMLite, you use the QueryBuilder object.
		queryBuilder.prepare() returns a PreparedQuery which is used by various methods in the DAO.
		You can call dao.iterator(preparedQuery) which will return a CloseableIterator which is used to iterate through the results.
		There is a iterator.getRawResults() to get access to the DatabaseResults class.
		Under Android, this can be cast to an AndroidDatabaseResults which has a getCursor() method on it to return the Android Cursor.
		*/
    	
    	Cursor c = null;
    	try {
        	AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getRawAllLight().prepare()).getRawResults();
			c = adr.getRawCursor();
		} catch (Exception e) { // SQLException
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return c;
    }
    
    
    public Cursor getCursorItemById(int id) { // for ORM
    	Cursor c = null;
    	try {
    		//AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(itemsDao.queryBuilder().where().idEq(id).prepare()).getRawResults();
    		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(itemsDao.queryBuilder().selectColumns(Item.COLUMN_NAME, Item.COLUMN_DATE, Item.COLUMN_BOOL).where().idEq(id).prepare()).getRawResults();
			c = adr.getRawCursor();
		} catch (Exception e) { // SQLException
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return c;
	}
    
    
    public QueryBuilder<Item, Integer> getRawAllLight() {
    	return itemsDao.queryBuilder()
        		.selectColumns(Item.COLUMN_NAME,
 					   Item.COLUMN_DATE,
 					   Item.COLUMN_BOOL);
    }
    
    
    public List<Item> getAllLight() {
        try {
            return getRawAllLight().query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    
    
    public List<Item> getSearchBetweenDatesLight(String searchLow, String searchMax) {
        try {
        	return itemsDao.queryBuilder()
            		.selectColumns(Item.COLUMN_NAME,
					   		       Item.COLUMN_DATE,
					   		       Item.COLUMN_BOOL).where().between(Item.COLUMN_DATE, searchLow, searchMax).query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    
 
    public List<Item> getSearchOnYearLight(String searchYear) {
    	return getSearchBetweenDatesLight(searchYear+"-01-01", searchYear+"-12-31");
    }

    
    public List<Item> getSearchOnYearMonthLight(String searchYearMonth) {
		return getSearchBetweenDatesLight(searchYearMonth+"-01", searchYearMonth+"-31");
    }


    public List<Item> getSearchOnDateLight(String searchDate) {
        try {
        	return itemsDao.queryBuilder()
            		.selectColumns(Item.COLUMN_NAME,
     					   		   Item.COLUMN_DATE,
     					   		   Item.COLUMN_BOOL).where().eq("date", searchDate).query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> getSearchOnNameLight(String searchName) {
        try {
        	return itemsDao.queryBuilder()
            		.selectColumns(Item.COLUMN_NAME,
            					   Item.COLUMN_DATE,
            					   Item.COLUMN_BOOL).where().like("name", "%"+searchName+"%").query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    
    public List<Item> getOnlyWithDateLight() {
        try {
            return itemsDao.queryBuilder()
            		.selectColumns(Item.COLUMN_NAME,
					   		       Item.COLUMN_DATE,
					   		       Item.COLUMN_BOOL).where().isNotNull("date").query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    

    public List<Item> getOnlyBoolLight(int bool) {
        try {
            // Cursor c = db.query(TABLE_ITEMS, ALL_COLUMNS, "bool = ?", new String[] {""+bool}, null, null, null);
            return itemsDao.queryBuilder()
            		.selectColumns(Item.COLUMN_NAME,
					   		       Item.COLUMN_DATE,
					   		       Item.COLUMN_BOOL).where().eq("bool", bool).query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }  
    
}