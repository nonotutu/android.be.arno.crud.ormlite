package be.arno.crud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.arno.crud.items.Item;
import be.arno.crud.items.ItemsRepositoryForSync;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SynchronizationActivity extends Activity {
//		
//	private static final String LOG_TAG = "SynchronizationActivity";
//
//	private static Context context;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_synchronization);
//		
//		context = getApplicationContext();
//		
//		AST_synchronization task = new AST_synchronization();
//		task.execute();
//		
//
//		// Toaster.showToast(App.getContext(), Toaster.INFO, count + " items updated.");
//	}
//	
//	
//	
//	private static class AST_synchronization extends AsyncTask<Void, Void, List<Integer>> {
//		
//		@Override
//		protected List<Integer> doInBackground(Void... v) {
//			Log.i(LOG_TAG, "AST_create | Long doInBackground(Item toServer()");
//
//			ItemsRepositoryForSync iRepos = new ItemsRepositoryForSync(App.getContext());
//			
//			Item item;
//			
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost("http://192.168.0.210:3000/categories/4/items");
//
//			
//			int count_Iterations = 0;
//			int count_NewlyCreatedSuccessfullyPosted = 0;
//			int count_NewlyCreatedFaillllllllyPosted = 0;
//			int count_NewlyCreatedFaillllllReupdated = 0;
//			
//			
//			/** STEP x
//			 * 
//			 * 		post all new items
//			 * 
//			 */
//			// TODO : attention, si un item ne s'update pas, infinite loop
//			while ( ( item = iRepos.getFirstNewlyCreated() ) != null ) {
//				count_Iterations += 1;
//				
//				Date synced_at = new Date();
//				
//				long remote_id = 0;
//				remote_id = doStep1(httpClient, httpPost, item, synced_at);
//
//				if ( remote_id == 1 ) {
//					if ( iRepos.updateSuccessPosted(item, remote_id, synced_at) == 1 ) {
//						count_NewlyCreatedSuccessfullyPosted += 1;
//					} else {
//						count_NewlyCreatedFaillllllReupdated += 1;
//						/*break;*/ }
//				} else {
//					count_NewlyCreatedFaillllllllyPosted += 1;
//					/*break;*/ }
//			}
//			
//			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//			Date last_sync_at = null;
//			
//			if ( preferences.contains("last_synced_at") ) {
//				Long long_last_sync_at = preferences.getLong("last_synced_at", 0);
//				Log.i("dadadate", "" + ( last_sync_at = new Date(long_last_sync_at)));
//			} else {
//				Log.i("dadadate", "does not exist");
//			}
//			
//			/** STEP x
//			 * 
//			 * 		retrieve all new items
//			 * 
//			 */
//			List<Item> items = doStep2(
//					last_sync_at.getYear(),
//					last_sync_at.getMonth(),
//					last_sync_at.getDay(),
//					last_sync_at.getHours(),
//					last_sync_at.getMinutes(),
//					last_sync_at.getSeconds()					
//					);
//			
//			int i = 0;
//			
//			while ( i < items.size() ) {
//				iRepos.createNewlyDownloaded(items.get(i));
//				i+=1;
//			}
//			
//			preferences.edit().putLong("last_synced_at", (new Date()).getTime()).commit();
//
//			List<Integer> results = new ArrayList<Integer>();
//			results.add(count_Iterations);
//			results.add(count_NewlyCreatedSuccessfullyPosted);
//			results.add(count_NewlyCreatedFaillllllllyPosted);
//			results.add(count_NewlyCreatedFaillllllReupdated);
//			
//			return results;
//		}
//		
//		
//		
//		
//		
//		
//				
//		private long doStep1(HttpClient httpClient, HttpPost httpPost, Item item, Date synced_at) {
//			
//			StringBuffer stringBuffer = new StringBuffer("");
//			BufferedReader bufferedReader = null;
//			
//			try {
//
//				String jsonToPost = "";
//				
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.accumulate("name", item.getName());
//				jsonObject.accumulate("date", item.getDate());				
//				jsonObject.accumulate("bool", item.getBool());
//				jsonObject.accumulate("category_id", 4);
//				jsonObject.accumulate("icreated_at", item.getCreatedAt());
//				jsonObject.accumulate("iupdated_at", item.getUpdatedAt());
//				jsonObject.accumulate("isynced_at",  synced_at);
//
//				jsonToPost = jsonObject.toString();
//				
//				StringEntity se = new StringEntity(jsonToPost, HTTP.UTF_8);				
//
//				httpPost.setEntity(se);
//				
//				httpPost.setHeader("Accept", "application/json");
//				httpPost.setHeader("Content-type", "application/json");
//				
//				HttpResponse httpResponse = httpClient.execute(httpPost);
//
//				InputStream inputStream = httpResponse.getEntity().getContent();
//				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//				String ligneLue;
//				while ( ( ligneLue = bufferedReader.readLine() ) != null) {
//					stringBuffer.append(ligneLue).append("\n");
//				}
//
//				JSONObject jsonReturned = new JSONObject(stringBuffer.toString());
//				Log.i("json", jsonReturned.toString());
//
//				if ( ! jsonReturned.isNull("id") ) {
//					return jsonReturned.getLong("id");
//				} else {
//					return 0;
//				}
//
//				// TODO : comprendre
//			} catch (Exception e) {
//				Log.e("LOG_TAG", "Exception: " + e.getMessage());
//			} finally {
//				if (bufferedReader != null) {
//					try {
//						bufferedReader.close();
//					} catch (IOException e) {
//						Log.e("LOG_TAG", "IOException: " + e.getMessage());
//					}
//				}
//			}
//			return -1;
//		}
//		
//		
//		
//		private List<Item> doStep2(int ye, int mo, int da, int ho, int mi, int se) {
//			
//			HttpClient httpClient = new DefaultHttpClient();
//			
//			try {
//
//				List<NameValuePair> params = new LinkedList<NameValuePair>();
//
//		        params.add(new BasicNameValuePair("ye", "2014"));
//		        params.add(new BasicNameValuePair("mo", "10"));
//		        params.add(new BasicNameValuePair("da", "10"));
//		        params.add(new BasicNameValuePair("ho", "10"));
//			    params.add(new BasicNameValuePair("mi", "10"));
//			    params.add(new BasicNameValuePair("se", "10"));				    
//
//			    String paramString = URLEncodedUtils.format(params, "utf-8");
//
//				HttpGet httpGet = new HttpGet("http://192.168.0.210:3000/all_items_created_since.json?"+ paramString );
//				
//				HttpResponse httpResponse = httpClient.execute(httpGet);
//				HttpEntity httpEntity = httpResponse.getEntity();
//				
//				if (httpEntity != null) {
//					InputStream inputStream = httpEntity.getContent();
//					
//					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//					StringBuilder stringBuilder = new StringBuilder();
//					
//					String line = bufferedReader.readLine();
//					while (line != null) {
//						stringBuilder.append(line + "\n");
//						Log.i("====>", line);
//						line = bufferedReader.readLine();
//					}
//					bufferedReader.close();
//					
//					//Analyse du retour
//					JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//					//JSONObject jsonResponseData = jsonObject.getJSONObject("responseData");
//
//					//String nbrResultats = jsonResponseData.getJSONObject("items").getString("name");
//					
//					JSONArray jsonArray = new JSONArray();
//					jsonArray = jsonObject.getJSONArray("items");
//					
//					List<Item> items = new ArrayList<Item>();
//					
//					int index = 0;
//					
//					while ( index < jsonArray.length()) {
//						Item item = new Item();
//						item.setRemoteId( jsonArray.getJSONObject(index).getInt(    "id" )  );
//						item.setCategory(7);
//						item.setName(     jsonArray.getJSONObject(index).getString( "name") );
//						item.setDate(     jsonArray.getJSONObject(index).getString( "date") );
//						item.setBool(	  jsonArray.getJSONObject(index).getBoolean("bool")?1:0 );
//						//item.setImage(image)
//						//item.setRating(   jsonArray.getJSONObject(i).getDouble(  "rating") );
//						try {
//							item.setCreatedAt( (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(jsonArray.getJSONObject(index).getString( "icreated_at") ));
//							item.setUpdatedAt( (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(jsonArray.getJSONObject(index).getString( "iupdated_at") ));
//							item.setSyncedAt(  (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(jsonArray.getJSONObject(index).getString( "isynced_at") ));
//						} catch( Exception e ) {}
//						items.add(item);
//						index+=1;
//					}
//					//Log.i("====>", jsonObject.);
//					
//					return items;
//				}
//				
//			} catch (IOException e) {
//				Log.e(LOG_TAG, e.getMessage());
//			} catch (JSONException e) {
//				System.out.println("hello");
//				Log.e(LOG_TAG, e.getMessage());
//			}
//			return null;
//			
//		}
//		
//		
//	}
}

		