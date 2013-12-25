package be.arno.crud.items;

import java.io.ByteArrayOutputStream;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField; 	// ORM
import com.j256.ormlite.table.DatabaseTable;	// ORM

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@DatabaseTable(tableName = "items")	// ORM
public class Item {

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_BOOL = "bool";
		
	@DatabaseField(generatedId = true)	// ORM
	private int _id;
	@DatabaseField	// ORM
	private String name;
	@DatabaseField // ORM
	private String date;
	@DatabaseField	// ORM
	private float rating;
	@DatabaseField	// ORM
	private int bool;
	@DatabaseField(dataType=DataType.BYTE_ARRAY)	// ORM
	private byte[] image; // ORM
	// private Bitmap image; // -ORM

	public Item() {}

	
	public void setImage(Bitmap image) {
		byte[] byteArray = null;
		if ( image != null ) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// TODO : comprendre
			image.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byteArray = stream.toByteArray();
		}
		this.image = byteArray;
	}
	
	
	public Bitmap getImage() {
		if ( this.image != null ) {
			// TODO : comprendre
			Bitmap bitmap = BitmapFactory.decodeByteArray(this.image, 0, this.image.length);
			return bitmap;
		}
		return null;
	}
	
	
	public int getBool() {
		return this.bool;
	}
	
	public void setBool(int bool) {
		this.bool = bool;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getDatePart(String part) {
		String s = null;
		int i;
		if ( part == "yyyy" ) {
			s = date.substring(0, 4);
		} else if ( part == "MM" ) {
			s = date.substring(5, 7);
		} else if ( part == "dd" ) {
			s = date.substring(8, 10);
		}
		
		i = Integer.parseInt(s);
		return i;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return this._id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public float getRating() {
		return this.rating;
	}
	public void setRating(float rating) {
		if ( rating > 5 ) rating = 5;
		if ( rating < 0 ) rating = 0;
		this.rating = rating;
	}

	public String getCharedRating() {
		int i = (int) (this.rating * 2);
		switch(i) {
		case 0:
			return "_____";
		case 1:
			return "=____";
		case 2:
			return "#____";
		case 3:
			return "#=___";
		case 4:
			return "##___";
		case 5:
			return "##=__";
		case 6:
			return "###__";
		case 7:
			return "###=_";
		case 8:
			return "####_";
		case 9:
			return "####=";
		case 10:
			return "#####";
		default:
			return "XXXXX";
		}
	}
	
	public String toString() {
		String s = this.name + " :: " + this.getCharedRating();
		return s;
	}
	
	public boolean isValid() {
		if ( this.name.isEmpty() || this.name == null )
			return false;
		if ( this.bool < 0 || this.bool > 1 )
			return false;
	return true;
	}	
}
