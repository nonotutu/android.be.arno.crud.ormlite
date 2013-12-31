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
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;


public class ItemsServer {
	 
	private static final String LOG_TAG = "ItemsServer";
	
	public static final String ADDRESS = "http://192.168.1.210:3000/items";

	private Context context;
	
    
    public ItemsServer(Context context) {
    	Log.i(LOG_TAG, "public ItemsServer(Context)");
    	this.context = context;
    }
    
    
    public long create(Item item) {
    	
    	Log.i(LOG_TAG, "public Long create(Item)");

    	AST_createItem task = new AST_createItem();
		task.execute(item);

		long result = -1;

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

    
    public long delete(long id) {

    	Log.i(LOG_TAG, "public Long delete(long)");

    	AST_deleteItem task = new AST_deleteItem();
		task.execute(id);

		long result = -1;

		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }

		Log.i(LOG_TAG, "public Long delete(long) result: " + result);

		return result;
    }

    
    public Item getItemById(int id) {
    	return null;
    }
    
    
    public List<Item> getAll() {
    	return null;
    }

    
    public long[] getAllIds() {
	    Log.i(LOG_TAG, "long[] getAllIds()");
	
		AST_getAllIds task = new AST_getAllIds();
		task.execute();
	
		long[] result = null;
	
		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		Log.i(LOG_TAG, "long[] getAllIds result: " + result);
	
		return result;
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
    private class AST_deleteItem extends AsyncTask<Long, Void, Long> {

		@Override
		protected Long doInBackground(Long... id) {

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
					// Log.i(LOG_TAG, ligneLue);
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
    

    private class AST_getAllIds extends AsyncTask<Void, Void, long[]> {

		@Override
		protected long[] doInBackground(Void... v) {
			
			Log.i(LOG_TAG, "AST_getAllIds - doInBackground(Void... v)");
			
			StringBuffer stringBuffer = new StringBuffer("");
			BufferedReader bufferedReader = null;
	
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(ADDRESS + "_ids");
				
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);

				InputStream inputStream = httpResponse.getEntity().getContent();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				String line;
				while ( ( line = bufferedReader.readLine() ) != null) {
					stringBuffer.append(line).append("\n");
				}

				JSONObject jsonReturned = new JSONObject(stringBuffer.toString());
				
				if ( ! jsonReturned.isNull("ids") ) {
					JSONArray jsonArray = jsonReturned.getJSONArray("ids");
					
					long[] ids = new long[jsonArray.length()];
					for ( int i = 0 ; i < jsonArray.length() ; i+= 1 ) {
						ids[i] = jsonArray.getLong(i);
					}
					return ids;
				} else {
					return null;
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
			return null;
		}
	}
    
    
}