package be.arno.crud.items;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.app.DownloadManager.Query;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import be.arno.crud.DatabaseManager;
import be.arno.crud.CatemDBHelper;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class ItemsRepository {
	 
	private static final String LOG_TAG = "ItemsRepository";
	
    protected CatemDBHelper db;
    protected Dao<Item, Integer> itemsDao;
 
    
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

    
    // TODO: categoryId ?
    /* returns number of successfully created rows */
    public int create(Item item) {
    	Log.i(LOG_TAG, "int create(Item)");
    	Date createdAt = new Date();
    	item.setCreatedAt(createdAt);
    	item.setUpdatedAt(createdAt);
    	if ( item.isValid() ) {
        	Log.i(LOG_TAG, "item.isValid() == true");
	        try {
	            return itemsDao.create(item);
	        } catch (SQLException e) {
	            // TODO: Exception Handling
	            e.printStackTrace();
	        }
    	}	
        return 0;
    }
    
    
    // TODO: categoryId ?
    /* returns number of successfully updated rows */
    public int update(Item item) {
		Log.i(LOG_TAG, "int update(Item) | .getId() : " + item.getId());
    	Date updatedAt = new Date();
    	item.setUpdatedAt(updatedAt);
        if ( item.isValid() ) {
			try {
	            return itemsDao.update(item);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
        }
        return 0;
    }

    // TODO: categoryId ?
    public int delete(int id) {
        try {
            return itemsDao.deleteById(id);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    
    // TODO: categoryId ?
    public Item getById(int id) {
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
    
    
    public int deleteAll(int categoryId) {
    	try {
    		DeleteBuilder<Item, Integer> deleteBuilder = itemsDao.deleteBuilder();
    		deleteBuilder.where().eq(Item.COLUMN_CATEGORY_ID, categoryId);
    		deleteBuilder.delete();
		} catch (SQLException e) {}
    	return 0;
    }
    		

    public long getCount() {
    	try {
    		return itemsDao.countOf();
    	} catch (SQLException e) {}
    	return 0;
    }
   
    
    public long getCount(int categoryId) {
    	try {
    		return itemsDao.queryBuilder().selectColumns(Item.COLUMN_ID)
    									  .where()
    									  .eq(Item.COLUMN_CATEGORY_ID, categoryId)
    									  .countOf();
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
    
    
    public Cursor getCursorItemById(int id) {
    	Cursor c = null;
    	try {
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
    
    
    public List<Item> getAll_light() {
        try {
            return getRawAllLight().query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }


    /* ======== SEARCH ON DATE */

    public Where<Item, Integer> getWhereSearchBetweenDates_light(String searchMin, String searchMax) {
    	try {
	    	return itemsDao.queryBuilder()
	    				   .selectColumns(Item.COLUMN_ID,
	    						   		  Item.COLUMN_CATEGORY_ID,
			         			   	  	  Item.COLUMN_NAME,
			         			   	  	  Item.COLUMN_DATE,
			         			   	  	  Item.COLUMN_BOOL)
	    			       .where()
	    			       .between(Item.COLUMN_DATE, searchMin, searchMax);
		} catch (SQLException e) { e.printStackTrace(); }
    	return null;
    }
        
    public List<Item> getSearchBetweenDates_light(String searchMin, String searchMax) {
        try {
        	return getWhereSearchBetweenDates_light(searchMin, searchMax).query();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public List<Item> getSearchBetweenDates_light(int categoryId, String searchMin, String searchMax) {
        try {
        	return getWhereSearchBetweenDates_light(searchMin, searchMax).and().eq(Item.COLUMN_CATEGORY_ID, categoryId).query();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public Cursor getCursorSearchBetweenDates_light(String searchMin, String searchMax) {
    	try {
    		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getWhereSearchBetweenDates_light(searchMin, searchMax).prepare()).getRawResults();
    		return adr.getRawCursor();
		} catch (SQLException e) { e.printStackTrace(); }
    	return null;
	}

    public List<Item> getSearchOnYear_light(String searchYear) {
    	return getSearchBetweenDates_light(searchYear+"-01-01", searchYear+"-12-31");
    }
    public List<Item> getSearchOnYear_light(int categoryId, String searchYear) {
    	return getSearchBetweenDates_light(categoryId, searchYear+"-01-01", searchYear+"-12-31");
    }
    public Cursor getCursorSearchOnYear_light(String searchYear) {
    	return getCursorSearchBetweenDates_light(searchYear+"-01-01", searchYear+"-12-31");
	}

    public List<Item> getSearchOnYearMonth_light(String searchYearMonth) {
		return getSearchBetweenDates_light(searchYearMonth+"-01", searchYearMonth+"-31");
    }
    public List<Item> getSearchOnYearMonth_light(int categoryId, String searchYearMonth) {
		return getSearchBetweenDates_light(categoryId, searchYearMonth+"-01", searchYearMonth+"-31");
    }
    public Cursor getCursorSearchOnYearMonth_light(String searchYearMonth) {
		return getCursorSearchBetweenDates_light(searchYearMonth+"-01", searchYearMonth+"-31");
	}

    public List<Item> getSearchOnDate_light(String searchDate) {
		return getSearchBetweenDates_light(searchDate, searchDate);
    }
    public List<Item> getSearchOnDate_light(int categoryId, String searchDate) {
		return getSearchBetweenDates_light(categoryId, searchDate, searchDate);
    }
    public Cursor getCursorSearchOnDate_light(String searchDate) {
		return getCursorSearchBetweenDates_light(searchDate, searchDate);
	}


    /* ======== FROM CATEGORY */

    public Where<Item, Integer> getWhereAll(int categoryId) {
        try {
	    	return itemsDao.queryBuilder()
	    			       .where()
	    			       .eq(Item.COLUMN_CATEGORY_ID, categoryId);
		} catch(SQLException e) {}
		return null;
    }

    public Where<Item, Integer> getWhereAll_light(int categoryId) {
        try {
	    	return itemsDao.queryBuilder()
		            	   .selectColumns(Item.COLUMN_ID,
			         			   	  	  Item.COLUMN_CATEGORY_ID,
			         			   	  	  Item.COLUMN_NAME,
			         			   	  	  Item.COLUMN_DATE,
			         			   	  	  Item.COLUMN_BOOL)
	    			       .where()
	    			       .eq(Item.COLUMN_CATEGORY_ID, categoryId);
		} catch(SQLException e) {}
		return null;
    }

    public List<Item> getAll(int categoryId) {
        try {
	    	return getWhereAll(categoryId).query();
		} catch(SQLException e) {}
		return null;
    }
    public Cursor getCursorAll(int categoryId) {
        try {
	   		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getWhereAll(categoryId).prepare()).getRawResults();
	   		return adr.getRawCursor();
		} catch(SQLException e) {}
		return null;
	}
    
    public List<Item> getAll_light(int categoryId) {
        try {
	    	return getWhereAll_light(categoryId).query();
		} catch(SQLException e) {}
		return null;
    }
    
    public Cursor getCursorAll_light(int categoryId) {
        try {
	   		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getWhereAll_light(categoryId).prepare()).getRawResults();
	   		return adr.getRawCursor();
		} catch(SQLException e) {}
		return null;
	}
    
    
    /* ======== SEARCH ON NAME */
    
    public Where<Item, Integer> getWhereSearchOnName(String searchName) {
        try {
	    	return itemsDao.queryBuilder().where()
	    								  .like(Item.COLUMN_NAME, "%" + searchName + "%");
		} catch(SQLException e) {}
		return null;
    }
    
    public Where<Item, Integer> getWhereSearchOnName_light(String searchName) {
        try {
	    	return itemsDao.queryBuilder()
		            	   .selectColumns(Item.COLUMN_ID,
		            			   	  	  Item.COLUMN_CATEGORY_ID,
		            			   	  	  Item.COLUMN_NAME,
		            			   	  	  Item.COLUMN_DATE,
		            			   	  	  Item.COLUMN_BOOL)
	    				    .where()
	    					.like(Item.COLUMN_NAME, "%" + searchName + "%");
		} catch(SQLException e) {}
		return null;
    }
    	
    public Cursor getCursorSearchOnName(String searchName) {
        try {
	   		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getWhereSearchOnName(searchName).prepare()).getRawResults();
	   		return adr.getRawCursor();
		} catch(SQLException e) {}
		return null;
	}
    
    public List<Item> getSearchOnName(String searchName) {
        try {
	       	return getWhereSearchOnName(searchName).query();
		} catch(SQLException e) {}
		return null;
    }
    
    public List<Item> getSearchOnName(int categoryId, String searchName) {
        try {
	       	return getWhereSearchOnName(searchName).and().eq(Item.COLUMN_CATEGORY_ID, categoryId).query();
		} catch(SQLException e) {}
		return null;
    }
    
    public Cursor getCursorSearchOnName_light(String searchName) {
        try {
	   		AndroidDatabaseResults adr = (AndroidDatabaseResults) itemsDao.iterator(getWhereSearchOnName_light(searchName).prepare()).getRawResults();
	   		return adr.getRawCursor();
		} catch(SQLException e) {}
		return null;
	}
    
    public List<Item> getSearchOnName_light(String searchName) {
        try {
    		return getWhereSearchOnName_light(searchName).query();
		} catch(SQLException e) {}
		return null;
    }

    public List<Item> getSearchOnName_light(int categoryId, String searchName) {
    	try {
    		return getWhereSearchOnName_light(searchName).and().eq(Item.COLUMN_CATEGORY_ID, categoryId).query();
    	} catch(SQLException e) {}
    	return null;
    }

    
    /* ======== Filters */

    public Where<Item, Integer> getWhereOnlyWithDate_light() {
    	try {
    		return itemsDao.queryBuilder()
	    				   .selectColumns(Item.COLUMN_ID,
	    						   		  Item.COLUMN_CATEGORY_ID,
				       			  		  Item.COLUMN_NAME,
				       			  		  Item.COLUMN_DATE,
				       			  		  Item.COLUMN_BOOL)
    					   .where()
    					   .isNotNull(Item.COLUMN_DATE);
    	} catch (SQLException e) {}
    	return null;
    }

    public List<Item> getOnlyWithDate_light(int categoryId) {
        try {
            return getWhereOnlyWithDate_light()
            		.and()
					.eq(Item.COLUMN_CATEGORY_ID, categoryId)
					.query();
        } catch (SQLException e) {}
        return null;
    }
    

    public Where<Item, Integer> getWhereOnlyBool_light(int bool) {
        try {
            return itemsDao.queryBuilder()
            			   .selectColumns(Item.COLUMN_ID,
            					   		  Item.COLUMN_CATEGORY_ID,
            					   		  Item.COLUMN_NAME,
            					   		  Item.COLUMN_DATE,
            					   		  Item.COLUMN_BOOL)
            			   .where()
            			   .eq(Item.COLUMN_BOOL, bool);
        } catch (SQLException e) {}
        return null;
    }
        
    public List<Item> getOnlyBool_light(int categoryId, int bool) {
        try {
            return getWhereOnlyBool_light(bool)
            		.and()
					.eq(Item.COLUMN_CATEGORY_ID, categoryId)
					.query();
        } catch (SQLException e) {}
        return null;
    }
   
}
