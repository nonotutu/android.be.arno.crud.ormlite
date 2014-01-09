package be.arno.crud.items;

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


public class ItemsServer {
	 
	private static final String LOG_TAG = "ItemsServer";
	
	//public static final String ADDRESS = "http://192.168.1.210:3000/items";

	private Context context;
	public String address = null;
	
    
    public ItemsServer(Context context) {
    	Log.i(LOG_TAG, "public ItemsServer(Context)");
    	this.context = context;
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		this.address = settings.getString("serverUrl", null);
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
    
    
    // TODO : je ne sais pas si c'est bien de faire comme ça ?
    public int delete(Item item) {
    	delete(item.getId());
    	return 0;
    }

    
    public long delete(long id) {

    	Log.i(LOG_TAG, "public Long delete(long id)");

    	AST_deleteItem task = new AST_deleteItem();
		task.execute(id);

		long result = -1;

		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }

		Log.i(LOG_TAG, "public Long delete(long id) return: " + result);

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
	    Log.i(LOG_TAG, "long getCount()");
		
		AST_count task = new AST_count();
		task.execute();
	
		long result = -4;
	
		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		Log.i(LOG_TAG, "long getCount result: " + result);
	
		return result;
    }
    
    
    public List<Item> getAll_light(long categoryId, long[] limitoffset) {
	    Log.i(LOG_TAG, "List<Item> getAll_light(int, long[])");
		
		AST_getAll_light task = new AST_getAll_light();
		task.execute(categoryId, limitoffset[0], limitoffset[1]);

		List<Item> result = new ArrayList<Item>();
		
		try { result = task.get(); }
		catch (Exception e) { e.printStackTrace(); }
	
		Log.i(LOG_TAG, "long getCount result: " + result);
	
		return result;
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
				HttpDelete httpDelete = new HttpDelete(address + "/" + id[0]);
				
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
	}
    

    private class AST_getAllIds extends AsyncTask<Void, Void, long[]> {

		@Override
		protected long[] doInBackground(Void... v) {
			
			Log.i(LOG_TAG, "AST_getAllIds - doInBackground(Void... v)");
			
			StringBuffer stringBuffer = new StringBuffer("");
			BufferedReader bufferedReader = null;
	
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(address + "_ids");
				
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
    
    
    
    

    private class AST_count extends AsyncTask<Void, Void, Long> {

		@Override
		protected Long doInBackground(Void... v) {
			
			Log.i(LOG_TAG, "AST_count | Long doInBackground(Void... v)");
			
			InputStream inputStream = null;
			BufferedReader bufferedReader = null;
	
			String result = null;
			
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
    
    

    private class AST_getAll_light extends AsyncTask<Long, Void, List<Item>> {

		@Override
		protected List<Item> doInBackground(Long... categoryId_limit_offset) {
			
			Log.i(LOG_TAG, "AST_getAll_light - doInBackground(Integer... categoryId)");
			
			String result = null;
	
			try {
				
				HttpClient httpClient = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(address + 
						"categories/" + categoryId_limit_offset[0] + "/items" +
						"?limit=" + categoryId_limit_offset[1] +
						"&offset=" + categoryId_limit_offset[2]);
								
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);

				InputStream inputStream = httpResponse.getEntity().getContent();
				if(inputStream != null) 
	                result = convertInputStreamToString(inputStream);
				
				JSONArray jsonArray = new JSONArray(result.toString());

				List<Item> items = new ArrayList<Item>();
				
				String str2fl;
				
				if ( jsonArray != null ) {
					Log.i(LOG_TAG, "jsonArray = " + jsonArray.toString());
					for ( int i = 0; i < jsonArray.length() ; i+= 1 ) {
						Item item = new Item();
						item.setId(jsonArray.getJSONObject(i).getInt("id"));
						item.setCategoryId(jsonArray.getJSONObject(i).getInt("category_id"));
						item.setName(jsonArray.getJSONObject(i).getString("name"));
						item.setDate(jsonArray.getJSONObject(i).getString("date"));
						item.setBool(jsonArray.getJSONObject(i).getBoolean("bool")?1:0);
						//item.setRating(Float.parseFloat(  ( jsonArray.getJSONObject(i).getString("rating"))==""?"0":jsonArray.getJSONObject(i).getString("rating")  ));
						items.add(item);
					}
				}
				
				return items;
				
			} catch (Exception e) {
				Log.e(LOG_TAG, "Exception: " + e.getMessage());
			}
			return null; // TODO retourner nouvelle list<item> vide ? sinon pointeur null vers liste affichée
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