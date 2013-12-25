package be.arno.crud;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Helper {
	
	// Retourne un String "yyyy-MM-dd".
	// Retourne NULL si date non valide TODO : vérifier.
	public static String dateInts2String(int year, int month, int day) {
		try {  
		    GregorianCalendar dateGC = new GregorianCalendar(year,month,day);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(dateGC.getTime());
		    return date;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/*
	public Date ints2Date(int year, int month, int day) {
		String strDate = year + "-" + month + "-" + day;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		try {  
		    Date date = format.parse(strDate);
		    return date;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/

	
}
