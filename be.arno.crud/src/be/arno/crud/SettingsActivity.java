package be.arno.crud;

import java.io.ObjectOutputStream.PutField;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

	private static final String LOG_TAG = "SettingsActivity";
    
	public static final String KEY_PREF_SEARCH_STEP = "searchStep";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "void onCreate (Bundle)");
         getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
    }



	public static class Preferences extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
		}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i(LOG_TAG, "void onDestroy()");
		
		PreferenceManager pm = getPreferenceManager();
		SharedPreferences sp;

		//Log.i(LOG_TAG, pm.getSharedPreferences().getString("searchStep", null));

		if ( ( sp = pm.getSharedPreferences()).getString("searchStep", null).equals("0") ) {
			SharedPreferences.Editor ed = sp.edit();
			ed.putString("searchStep", "-1").commit();
			//Log.i(LOG_TAG, "0_1");
			
		}
		
		//addPreferencesFromResource(R.xml.settings);
	}

	
		
	
	}
}
	

