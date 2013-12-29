package be.arno.crud.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;


public class ItemsServer {
	 
	private static final String LOG_TAG = "ItemsServer";
	
	public static final String ADDRESS = "http://192.168.0.14:3000/items";

	private Context context;
	
    
    public ItemsServer(Context context) {
    	Log.i(LOG_TAG, "public ItemsServer(Context)");
    	this.context = context;
    }
    
    
    public Long create(Item item) {
    	
    	Log.i(LOG_TAG, "public Long create(Item)");

    	AST_createItem task = new AST_createItem();
		task.execute(item);

		Long result = Long.valueOf(-1);

		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }

		Log.i(LOG_TAG, "public Long create(Item) result: " + result);

		return result;
    }
    
    
    public int update(Item item) {
    	return 0;
    }
    
    
    public int delete(Item item) {
    	return 0;
    }

    
    public Long delete(int id) {

    	Log.i(LOG_TAG, "public Long delete(int)");

    	AST_deleteItem task = new AST_deleteItem();
		task.execute(id);

		Long result = Long.valueOf(-1);

		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }

		Log.i(LOG_TAG, "public Long delete(int) result: " + result);

		return result;
    }

    
    public Item getItemById(int id) {
    	return null;
    }
    
    
    public List<Item> getAll() {
    	return null;
    }
 
    
    public int deleteAll() {
    	return 0;
    }

    
    public long getCount() {
    	return 0;
    }
    
    public List<Item> getAllLight() {
    	return null;
    }
    
    
    public List<Item> getSearchBetweenDatesLight(String searchLow, String searchMax) {
    	return null;
    }
    
 
    public List<Item> getSearchOnYearLight(String searchYear) {
    	return null;
    }

    
    public List<Item> getSearchOnYearMonthLight(String searchYearMonth) {
    	return null;
    }


    public List<Item> getSearchOnDateLight(String searchDate) {
    	return null;
    }

    public List<Item> getSearchOnNameLight(String searchName) {
    	return null;
    }

    
    public List<Item> getOnlyWithDateLight() {
    	return null;
    }
    

    public List<Item> getOnlyBoolLight(int bool) {
    	return null;
    }

    
    // data-method="delete" href="/items/16918"
    private class AST_deleteItem extends AsyncTask<Integer, Void, Long> {

		@Override
		protected Long doInBackground(Integer... id) {

			Log.i(LOG_TAG, "AST_deleteItem - Long doInBackground");
			
			StringBuffer stringBuffer = new StringBuffer("");
			BufferedReader bufferedReader = null;

			try {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpDelete httpDelete = new HttpDelete(ADDRESS + "/" + id[0]);
				
				HttpResponse httpResponse = httpClient.execute(httpDelete);
				
				Log.i(LOG_TAG, "http delete status code: " + httpResponse.getStatusLine().getStatusCode());
				
				InputStream inputStream = httpResponse.getEntity().getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				String ligneLue;
				while ( ( ligneLue = bufferedReader.readLine() ) != null) {
					stringBuffer.append(ligneLue).append("\n");
					Log.i(LOG_TAG, ligneLue);
				}

				//JSONObject jsonReturned = new JSONObject(stringBuffer.toString());

				//if ( ! jsonReturned.isNull("id") ) {
				//	return jsonReturned.getLong("id");
				//} else {
				//	return Long.valueOf(0);
				//}
				
			} catch (Exception e) {
				Log.e(LOG_TAG, "Exception: " + e.getMessage());
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						Log.e(LOG_TAG, "IOException: " + e.getMessage());
					}
				}
			}
			return Long.valueOf(-1);
		}
	}



    /** RETURNS :
	 * Long >=  0 : id, inserted
	 * Long ==  0 : not valid
	 * Long == -1 : error caught
	 */
    private class AST_createItem extends AsyncTask<Item, Void, Long> {

		@Override
		protected Long doInBackground(Item... item) {
			
			StringBuffer stringBuffer = new StringBuffer("");
			BufferedReader bufferedReader = null;
	
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(ADDRESS);
				
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
	}
    
    
    
}