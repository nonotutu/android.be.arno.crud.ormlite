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

	private static final String authority = "be.arno.crud.ItemProvider";
	private static final String uriItems = "content://" + authority + "/items";
	public static final Uri CONTENT_URI = Uri.parse(uriItems);
	public static final int COLONNE_ID = 0;
	public static final int COLONNE_NAME = 1;
	public static final int COLONNE_DATE = 2;
	private ItemsRepository repos;
	
	@Override
	public boolean onCreate() {
		repos = new ItemsRepository(this.getContext()); // ORM
		// itemDBAdapter = new ItemDBAdapter(this.getContext()); -ORM
		// itemDBAdapter.openReadable(); -ORM
		// if ( itemDBAdapter != null ) { -ORM
		if ( repos != null ) {
			return true;
		}
		return false;
	}
	
	private static final int TOUS_ITEMS  = 0;
	private static final int ITEM_UNIQUE = 1;
	private static final int ITEM_DELETE = 2;
	private static final UriMatcher uriMatcher; static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(authority, "items",         TOUS_ITEMS);
		uriMatcher.addURI(authority, "item/#",        ITEM_UNIQUE);
		uriMatcher.addURI(authority, "item/delete/#", ITEM_DELETE);
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
				return -1;
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

		Cursor c;
		int id = 0;
		
		switch (uriMatcher.match(uri)) {
		
			case TOUS_ITEMS:

			    c = repos.getCursorAllLight();
			    c.setNotificationUri(getContext().getContentResolver(), uri);
			    return c;
				
			case ITEM_UNIQUE:

				id = 0;
				try { id = Integer.parseInt(uri.getPathSegments().get(1)); }
				catch (Exception e) { return null; }
				//c = itemDBAdapter.getCursorItemById(id); -ORM
				c = repos.getCursorItemById(id); // ORM
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;

			default:
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}	
}
