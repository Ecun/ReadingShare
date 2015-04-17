package edu.bsu.cs222.domain;

import java.io.Serializable;

public class Word implements Comparable<Word>,Serializable {

	private static final long serialVersionUID = 6159659460595061499L;
	
	private int id;
	private int bookId;
	private String name;
	private String comment;
	private int flag;

	public Word(){
		
	}
	
	public Word(int bookId,String name){
		this.bookId=bookId;
		this.name=name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getStringId() {
		return String.valueOf(id);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getComment() {
		if(comment==null){
			return "";
		}
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Word)){
			return false;
		}
		Word other=(Word) o;
		if(!name.equals(other.name)){
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(Word another) {
		int t = another.flag-flag;
		if (t == 0) {
			t = name.compareTo(another.name);
		}
		return t;
	}

	@Override
	public String toString() {
		return "Word [id=" + id + ", bookId=" + bookId + ", name=" + name
				+ ", comment=" + comment + ", flag=" + flag + "]";
	}
	
}
