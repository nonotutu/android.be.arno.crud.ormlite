package be.arno.crud.items;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**

   --------------
	\ Appelant /
	 ----------
	
	2.  Content Resolver (query, insert, update, delete)
		idéal Async
	
	4.	Traitement Cursor pour en faire un objet
		idéal Async
	
   --------------
	\  Appelé  /
	 ----------

	1. 	Définir le Content Provider : classe qui extends ContentProvider
		L'enregistrer dans le Manifest
		URI matcher
	
	3.	CP <--> DBAdap <--> DB
		idéal Async

*/


public class ItemContentProvider extends ContentProvider {

	private static final String LOG_TAG = "ItemContentProvider";
	
	private static final String authority = "be.arno.crud.ItemProvider";
	private static final String uriItems = "content://" + authority + "/items";
	public static final Uri CONTENT_URI = Uri.parse(uriItems);
	public static final int COLONNE_ID = 0;
	public static final int COLONNE_NAME = 1;
	public static final int COLONNE_DATE = 2;
	private ItemsRepository repos;
	
	@Override
	public boolean onCreate() {
		repos = new ItemsRepository(this.getContext());
		if ( repos != null ) {
			return true;
		}
		return false;
	}
	
	private static final int ITEM                            = 0;
	private static final int ITEMS                     	     = 10;
	private static final int ITEMS_LIGHT                     = 11;
	private static final int ITEMS_FROM_CATEGORY             = 100;
	private static final int ITEMS_FROM_CATEGORY_LIGHT       = 101;
	private static final int ITEMS_SEARCH_ON_NAME_LIGHT      = 1511;
	private static final int ITEMS_SEARCH_ON_DATE_LIGHT      = 1521;
	private static final int ITEMS_SEARCH_ON_YEARMONTH_LIGHT = 1541;
	private static final int ITEMS_SEARCH_ON_YEAR_LIGHT      = 1551;
	
	private static final int ITEM_DELETE                     = 2;
	
	private static final UriMatcher uriMatcher; static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(authority, "item/#",                        ITEM);
		uriMatcher.addURI(authority, "items",                         ITEMS);
		uriMatcher.addURI(authority, "items/light",                   ITEMS_LIGHT);
		uriMatcher.addURI(authority, "items/#",              	      ITEMS_FROM_CATEGORY);
		uriMatcher.addURI(authority, "items/#/light",                 ITEMS_FROM_CATEGORY_LIGHT);
		uriMatcher.addURI(authority, "items/searchOnName/light",      ITEMS_SEARCH_ON_NAME_LIGHT);
		uriMatcher.addURI(authority, "items/searchOnDate/light",      ITEMS_SEARCH_ON_DATE_LIGHT);
		uriMatcher.addURI(authority, "items/searchOnYearMonth/light", ITEMS_SEARCH_ON_YEARMONTH_LIGHT);
		uriMatcher.addURI(authority, "items/searchOnYear/light",      ITEMS_SEARCH_ON_YEAR_LIGHT);
		uriMatcher.addURI(authority, "item/delete/#",                 ITEM_DELETE);		
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int id = 0;
		
		switch (uriMatcher.match(uri)) {
		
			case ITEM_DELETE:
				id = 0;
				try { id = Integer.parseInt(uri.getPathSegments().get(2)); }
				catch (Exception e) { return -1; }
				return repos.delete(id);
				//c.setNotificationUri(getContext().getContentResolver(), uri);

			default:
				return -2;
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {

		Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String)");
		
		Cursor c;
		int integer;
		
		switch (uriMatcher.match(uri)) {
		
			case ITEM:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS");
				try { integer = Integer.parseInt(uri.getPathSegments().get(1)); }
				catch (Exception e) { return null; }
				c = repos.getCursorItemById(integer);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			case ITEMS:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS");
			    c = repos.getCursorAllLight();
			    c.setNotificationUri(getContext().getContentResolver(), uri);
			    return c;

			case ITEMS_FROM_CATEGORY:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_FROM_CATEGORY");
				try { integer = Integer.parseInt(uri.getPathSegments().get(1)); }
				catch (Exception e) { return null; }
			    c = repos.getCursorAll(integer);
			    c.setNotificationUri(getContext().getContentResolver(), uri);
			    return c;
			
			case ITEMS_FROM_CATEGORY_LIGHT:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_FROM_CATEGORY_LIGHT");
				try { integer = Integer.parseInt(uri.getPathSegments().get(1)); }
				catch (Exception e) { return null; }
			    c = repos.getCursorAll_light(integer);
			    c.setNotificationUri(getContext().getContentResolver(), uri);
			    return c;

			case ITEMS_SEARCH_ON_NAME_LIGHT:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_SEARCH_ON_NAME_LIGHT");
				c = repos.getCursorSearchOnName_light(selection);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			case ITEMS_SEARCH_ON_DATE_LIGHT:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_SEARCH_ON_DATE_LIGHT");
				c = repos.getCursorSearchOnDate_light(selection);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			case ITEMS_SEARCH_ON_YEARMONTH_LIGHT:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_SEARCH_ON_YEARMONTH_LIGHT");
				c = repos.getCursorSearchOnYearMonth_light(selection);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			case ITEMS_SEARCH_ON_YEAR_LIGHT:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case ITEMS_SEARCH_ON_YEAR_LIGHT");
				c = repos.getCursorSearchOnYear_light(selection);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			default:
				Log.i(LOG_TAG, "Cursor query(Uri, String[], String, String[], String) | case default");
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}	
}
