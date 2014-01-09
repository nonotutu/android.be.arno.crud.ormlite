package be.arno.crud.categories;

import android.content.Context;
import be.arno.crud.App;
import be.arno.crud.items.ItemsDataSourceSelector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
		
	@DatabaseField(generatedId = true)
	private int _id;
	@DatabaseField
	private String name;

	
	public Category() {}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	public int getId() {
		return this._id;
	}
	public void setId(int id) {
		this._id = id;
	}
	

	public String toString() {
		String s = this.name;
		return s;
	}
	
	
	public boolean isValid() {
		if ( this.name == null || this.name.isEmpty() )
			return false;
	return true;
	}
	

	public long getCountItems(Context context, long[] limitoffset) {
		ItemsDataSourceSelector itemsData = 
				new ItemsDataSourceSelector(context);
		return itemsData.getCount(this._id, limitoffset);
	}
	
	
}
