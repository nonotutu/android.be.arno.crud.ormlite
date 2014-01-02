package be.arno.crud;

public class PairIdName {
	
	private int id;
	private String name;
	
	public PairIdName(int id, String name) {
		this.name = name;
		this.id = id;		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setRsql(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return "[ " + this.id + " : " + this.name + " ] ";
	}
				
}
