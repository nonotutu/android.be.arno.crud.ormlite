package be.arno.crud.categories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class CategoriesServer {
	 
	private static final String LOG_TAG = "CategoriesServer";
	
	private Context context;
	public String address = null;
	
    
    public CategoriesServer(Context context) {
    	Log.i(LOG_TAG, "public ItemsServer(Context)");
    	this.context = context;
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		this.address = settings.getString("serverUrl", null);
    }

    public long countItems(long categoryId) {
    	 Log.i(LOG_TAG, "long count_items()");
 		
 		AST_countItems task = new AST_countItems();
 		task.execute(categoryId);
 	
 		long count = -1;
 		
 		try { count = task.get(); }
 		catch (Exception e) { e.printStackTrace(); }
 	
 		return count;
    }
    
    
    /*
    public long getCount() {
	    Log.i(LOG_TAG, "long getCount()");
		
		AST_count task = new AST_count();
		task.execute();
	
		long result = -4;
	
		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		Log.i(LOG_TAG, "long getCount result: " + result);
	
		return result;
    }*/

    
    public List<Category> getAll() {
	    Log.i(LOG_TAG, "List<Category> getAll()");
		
		AST_getAll task = new AST_getAll();
		task.execute();
	
		List<Category> categories = new ArrayList<Category>();
		
		try { categories = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		return categories;
    }

    
    
    public Category getCategoryById(int id) {
	    Log.i(LOG_TAG, "Category getCategoryById(int)");
		
		AST_getCategory task = new AST_getCategory();
		task.execute(id);
	
		Category category = new Category();
		
		try { category = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		return category;
    }


    



    /** RETURNS :
	 * Long >=  0 : id, inserted
	 * Long ==  0 : not valid
	 * Long == -1 : error caught
	 */ /*
    private class AST_createItem extends AsyncTask<Item, Void, Long> {

		@Override
		protected Long doInBackground(Item... item) {
			
			StringBuffer stringBuffer = new StringBuffer("");
			BufferedReader bufferedReader = null;
	
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(address);
				
				String toPost = "";
				
				JSONObject jsonToPost = new JSONObject();
				jsonToPost.accumulate("name", item[0].getName());
				jsonToPost.accumulate("date", item[0].getDate());				
				jsonToPost.accumulate("bool", item[0].getBool());
				
				toPost = jsonToPost.toString();
				
				StringEntity stringEntity = new StringEntity(toPost, HTTP.UTF_8);
				httpPost.setEntity(stringEntity);
				
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpPost);

				InputStream inputStream = httpResponse.getEntity().getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				String ligneLue;
				while ( ( ligneLue = bufferedReader.readLine() ) != null) {
					stringBuffer.append(ligneLue).append("\n");
				}

				JSONObject jsonReturned = new JSONObject(stringBuffer.toString());

				if ( ! jsonReturned.isNull("id") ) {
					return jsonReturned.getLong("id");
				} else {
					return Long.valueOf(0);
				}
				
			} catch (Exception e) {
				Log.e("LOG_TAG", "Exception: " + e.getMessage());
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						Log.e("LOG_TAG", "IOException: " + e.getMessage());
					}
				}
			}
			return Long.valueOf(-1);	
		}
	}*/
    

    
    
/*
    private class AST_count extends AsyncTask<Void, Void, Long> {

		@Override
		protected Long doInBackground(Void... v) {
			
			Log.i(LOG_TAG, "AST_count | Long doInBackground(Void... v)");
			
			InputStream inputStream = null;
			BufferedReader bufferedReader = null;
	
			String result;
			
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(address + "items/root_count");
				
				Log.i(LOG_TAG, "HttpGet (" + address + "items/root_count" + ")");
				
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);

				inputStream = httpResponse.getEntity().getContent();
				
				if(inputStream != null)
	                result = convertInputStreamToString(inputStream);
	            else
	                result = "Did not work!";
				

				JSONObject jsonReturned = new JSONObject(result.toString());
								
				if ( ! jsonReturned.isNull("count") ) {
					long count = jsonReturned.getLong("count");
					return count;
				} else {
					return Long.valueOf(-2);
				}
				
			} catch (Exception e) {
				Log.e("LOG_TAG", "Exception: " + e.getMessage());
				return Long.valueOf(-3);
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						Log.e("LOG_TAG", "IOException: " + e.getMessage());
					}
				}
			}
			//return Long.valueOf(-4);
		}
	}
    */

    private class AST_getCategory extends AsyncTask<Integer, Void, Category> {

		@Override
		protected Category doInBackground(Integer... i) {
			
			Log.i(LOG_TAG, "AST_getAll | List<Category> doInBackground(Void... v)");
			
			try {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(address + "categories/" + i[0]);
				
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				InputStream inputStream = httpResponse.getEntity().getContent();				
				
				JSONObject jsonObject = new JSONObject(convertInputStreamToString(inputStream));
				
				if ( jsonObject != null ) {
					Category category = new Category();
					category.setId( jsonObject.getInt("id") );
					category.setName( jsonObject.getString("name") );
					return category;
				}
					
			} catch (Exception e) {
				Log.e("LOG_TAG", "Exception: " + e.getMessage());
			}
			return null;
		}
	}

    

    private class AST_getAll extends AsyncTask<Void, Void, List<Category>> {

		@Override
		protected List<Category> doInBackground(Void... v) {
			
			Log.i(LOG_TAG, "AST_getAll | List<Category> doInBackground(Void... v)");
			
			try {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(address + "categories");
				
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				InputStream inputStream = httpResponse.getEntity().getContent();				
				
				JSONArray jsonArray = new JSONArray(convertInputStreamToString(inputStream));
				List<Category> categories = new ArrayList<Category>();
				
				if ( jsonArray != null )
					for ( int i = 0 ; i < jsonArray.length() ; i+=1 ) {
						Category category = new Category();
						category.setId( jsonArray.getJSONObject(i).getInt("id") );
						category.setName( jsonArray.getJSONObject(i).getString("name") );
						categories.add(category);
					}
					
				return categories;
									
			} catch (Exception e) {
				Log.e("LOG_TAG", "Exception: " + e.getMessage());
			}
			return null;
		}
	}

    
    
    

    private class AST_countItems extends AsyncTask<Long, Void, Long> {

		@Override
		protected Long doInBackground(Long... l) {
			
			Log.i(LOG_TAG, "AST_count | Long doInBackground(Long... i)");
			
			String result = null;
			
			try {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(address + "categories/" + l[0] + "/count_items");
				
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				InputStream inputStream = httpResponse.getEntity().getContent();
				
				if(inputStream != null)
	                result = convertInputStreamToString(inputStream);

				// JSONObject jsonObject = new JSONObject(result.toString());
								
				if (result != null ) {
					return Long.parseLong(result.toString());
				} else {
					return Long.valueOf(-2);
				}
				
			} catch (Exception e) {
				Log.e("LOG_TAG", "Exception: " + e.getMessage());
				return Long.valueOf(-3);
			}
			//return Long.valueOf(-4);
		}
	}
    
    
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
    
    
}