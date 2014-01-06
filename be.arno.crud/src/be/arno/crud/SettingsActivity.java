package be.arno.crud;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

	private static final String LOG_TAG = "Settings_Activity";

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
	}
}
	

