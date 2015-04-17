package edu.bsu.cs222.domain;

import java.io.Serializable;

public class Book implements Comparable<Book>, Serializable {
	private static final long serialVersionUID = 1470068710071619854L;
	private int id;
	private String name;

	public Book(){
	
	}
	
	public Book(String name) {
		setName(name);
	}

	public Book(int id, String name) {
		this.id=id;
		setName(name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStringId() {
		return String.valueOf(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=format(name);
	}

	public static String format(String name){
		String[] tokens=name.toLowerCase().split(" ");
		String result="";
		for(String token:tokens){
			token=token.trim();
			if(token.length()>0){
				result+=Character.toUpperCase(token.charAt(0))+token.substring(1)+" ";
			}
		}		
		return result.trim();
	}
	@Override
	public int compareTo(Book another) {
		return name.compareTo(another.name);
	}
}
