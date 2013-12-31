package be.arno.crud;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseManager { // ORM
	 
    private CatemDBHelper databaseHelper = null;
 
    //gets a helper once one is created ensures it doesn't create a new one
    public CatemDBHelper getHelper(Context context)
    {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, CatemDBHelper.class);
        }
        return databaseHelper;
    }
 
    //releases the helper once usages has ended
    public void releaseHelper(CatemDBHelper helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
 
}