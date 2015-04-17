package edu.bsu.cs222.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.bsu.cs222.domain.Book;

public class BookDao extends BaseDao {

	public static final String UNKNOWN="Unknown";

	public static final String SELECT_FIELD_LIST="select id,name from book ";
	public BookDao(Context context) {
		super(context);
	}

	public void add(Book book){
		ContentValues values=new ContentValues();
		values.put("name", book.getName());
		db.insert("book", null, values);
	}
	
	public void edit(Book book){
		ContentValues values=new ContentValues();
		values.put("name", book.getName());
		db.update("book", values, "id=?", new String[]{book.getStringId()});		
	}
	
	public void delete(int id){
		String[] params=new String[]{String.valueOf(id)};
		int bookId=get(UNKNOWN).getId();
		ContentValues values=new ContentValues();
		values.put("bookId", bookId);
		db.update("word", values, "bookId=?", params);		
		db.delete("book", "id=?", params);
	}
	
	public void clear(){
		db.delete("book", "name!=?",new String[]{UNKNOWN});
	}
	
	public boolean exists(Book book){
		String sql="select count(*) from book where id<>? and name=?";
		Cursor cursor=db.rawQuery(sql,new String[]{book.getStringId(),book.getName()});
		if (cursor.moveToNext()){
			return cursor.getInt(0)>0;
		}
		return false;
	}
	
	public Book get(String name){
		name=Book.format(name);
		String sql=SELECT_FIELD_LIST+"where name=?";
		Cursor cursor=db.rawQuery(sql,new String[]{name});
		if (cursor.moveToNext()){
			return mapRow(cursor);
		}
		return null;
	}

	public Book get(int id) {
		String sql=SELECT_FIELD_LIST+"where id=?";
		Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(id)});
		if (cursor.moveToNext()){
			return mapRow(cursor);
		}
		return null;
	}

	public List<Book> list(){
		List<Book> books=new ArrayList<Book>();
		String sql=SELECT_FIELD_LIST;
		Cursor cursor=db.rawQuery(sql,null);
		while (cursor.moveToNext()){
			books.add(mapRow(cursor));
		}
		Collections.sort(books);
		return books;
	}

	public List<Book> search(String name){
		List<Book> books=new ArrayList<Book>();
		String sql=SELECT_FIELD_LIST+"where name like ?";
		Cursor cursor=db.rawQuery(sql,new String[]{"%"+name+"%"});
		while (cursor.moveToNext()){
			books.add(mapRow(cursor));
		}
		Collections.sort(books);
		return books;
	}

	private Book mapRow(Cursor cursor) {
		Book book=new Book();
		book.setId(cursor.getInt(0));
		book.setName(cursor.getString(1));
		return book;
	}

}
