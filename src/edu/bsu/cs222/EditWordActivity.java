package edu.bsu.cs222;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Book;
import edu.bsu.cs222.domain.Word;

public class EditWordActivity extends Activity {

	private Word word;
	private TextView tvName;
	private Spinner spinBook;
	private EditText etComment;
	private WordDao wordDao;
	private BookDao bookDao;
	private List<Book> books;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_word);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Edit Word");
		tvName = (TextView) findViewById(R.id.tv_name);
		spinBook = (Spinner) findViewById(R.id.spin_book);
		etComment = (EditText) findViewById(R.id.et_comment);
		
		wordDao=new WordDao(this);
		bookDao=new BookDao(this);
		
		books=bookDao.list();
		spinBook.setAdapter(adapter);
		
		Intent intent=getIntent();
		word=(Word) intent.getSerializableExtra("word");
		tvName.setText("Name: "+word.getName());
		spinBook.setSelection(0);
		for(int i=0;i<books.size();i++){
			if(books.get(i).getId()==word.getBookId()){
				spinBook.setSelection(i);
				break;
			}
		}
		etComment.setText(word.getComment());
	}

	@Override
	protected void onDestroy() {
		wordDao.closeDB();
		bookDao.closeDB();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_save:
			save();
			return true;
		case R.id.action_delete:
			delete();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void save() {
		int position=spinBook.getSelectedItemPosition();
		Book book=books.get(position);
		word.setBookId(book.getId());
		word.setComment(etComment.getText().toString());
		wordDao.edit(word);
		finish();		
	}

	private void delete() {
		new AlertDialog.Builder(this).setTitle("Confirm")
		.setMessage("are you sure delete?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				wordDao.delete(word.getId());
				finish();
			}

		}).setNegativeButton("No", null).show();
	}
	
	private ListAdapter adapter = new ListAdapter() {
		@Override
		public int getCount() {
			if (books == null) {
				return 0;
			}
			return books.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.book_item_small, parent, false);
			}
			TextView tvBook = (TextView) convertView.findViewById(R.id.tv_book);
			final Book book = books.get(position);
			tvBook.setText(book.getName());
			return convertView;
		}

	};
	
}
