package be.arno.crud.categories;

import be.arno.crud.App;
import be.arno.crud.items.ItemsRepository;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
		
	@DatabaseField(generatedId = true)
	private int _id;
	@DatabaseField	// ORM
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
		String s = this.name + " :: " + getCountItems();
		return s;
	}
	
	public boolean isValid() {
		if ( this.name.isEmpty() || this.name == null )
			return false;
	return true;
	}
	
	public long getCountItems() {
		ItemsRepository itemsRepos = new ItemsRepository(App.getContext());
		return itemsRepos.getCount(this._id);
	}
	
	
}
