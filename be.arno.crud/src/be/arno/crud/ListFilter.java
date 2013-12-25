package be.arno.crud;

public class ListFilter {
	
	private String name;
	private int rsql;
	
	public ListFilter(String name, int rsql) {
		this.name = name;
		this.rsql = rsql;		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setRsql(int rsql) {
		this.rsql = rsql;
	}
	
	public String getName() {
		return this.name;
	}
	public int getRsql() {
		return this.rsql;
	}
	
	public String toString() {
		return this.name;
	}
				
}
