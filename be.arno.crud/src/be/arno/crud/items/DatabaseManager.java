package be.arno.crud.items;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseManager { // ORM
	 
    private ItemDBHelper databaseHelper = null;
 
    //gets a helper once one is created ensures it doesn't create a new one
    public ItemDBHelper getHelper(Context context)
    {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, ItemDBHelper.class);
        }
        return databaseHelper;
    }
 
    //releases the helper once usages has ended
    public void releaseHelper(ItemDBHelper helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
 
}