package edu.bsu.cs222.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.bsu.cs222.domain.Word;

public class WordDao extends BaseDao {

	public static final String SELECT_FIELD_LIST="select id,bookId,name,comment,flag from word ";
	public WordDao(Context context) {
		super(context);
	}

	public void add(Word word){
		ContentValues values=new ContentValues();
		values.put("bookId", word.getBookId());
		values.put("name", word.getName());
		values.put("comment", word.getComment());
		values.put("flag", word.getFlag());
		db.insert("word", null, values);
	}
	
	public void edit(Word word){
		ContentValues values=new ContentValues();
		values.put("bookId", word.getBookId());
		values.put("name", word.getName());
		values.put("comment", word.getComment());
		values.put("flag", word.getFlag());
		db.update("word", values, "id=?", new String[]{word.getStringId()});		
	}
	
	public void delete(int id){
		db.delete("word", "id=?", new String[]{String.valueOf(id)});
	}
	
	public void clear(){
		db.delete("word", null , null);
	}
	
	public Word get(String name){
		String sql=SELECT_FIELD_LIST+"where name=?";
		Cursor cursor=db.rawQuery(sql,new String[]{name});
		if (cursor.moveToNext()){
			return mapRow(cursor);
		}
		return null;
	}

	public List<Word> list(){
		List<Word> words=new ArrayList<Word>();
		String sql=SELECT_FIELD_LIST;
		Cursor cursor=db.rawQuery(sql,null);
		while (cursor.moveToNext()){
			words.add(mapRow(cursor));
		}
		Collections.sort(words);
		return words;
	}

	public List<Word> listByBookId(int bookId) {
		List<Word> words=new ArrayList<Word>();
		String sql=SELECT_FIELD_LIST+"where bookId=?";
		Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(bookId)});
		while (cursor.moveToNext()){
			words.add(mapRow(cursor));
		}
		Collections.sort(words);
		return words;
	}

	public List<Word> search(String name){
		List<Word> words=new ArrayList<Word>();
		String sql=SELECT_FIELD_LIST+"where name like ?";
		Cursor cursor=db.rawQuery(sql,new String[]{"%"+name+"%"});
		while (cursor.moveToNext()){
			words.add(mapRow(cursor));
		}
		Collections.sort(words);
		return words;
	}

	private Word mapRow(Cursor cursor) {
		Word word=new Word();
		word.setId(cursor.getInt(0));
		word.setBookId(cursor.getInt(1));
		word.setName(cursor.getString(2));
		word.setComment(cursor.getString(3));
		word.setFlag(cursor.getInt(4));
		return word;
	}

}
