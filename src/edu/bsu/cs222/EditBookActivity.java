package edu.bsu.cs222;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.domain.Book;

public class EditBookActivity extends Activity {

	private Book book;
	private EditText etName;
	private BookDao bookDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_book);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Edit Book");
		etName = (EditText) findViewById(R.id.et_name);

		Intent intent = getIntent();
		book = (Book) intent.getSerializableExtra("book");
		etName.setText(book.getName());

		bookDao = new BookDao(this);
	}

	@Override
	protected void onDestroy() {
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
		book.setName(etName.getText().toString());
		if (bookDao.exists(book)) {
			Toast.makeText(this, "name exists.", Toast.LENGTH_SHORT).show();
			return;
		}
		bookDao.edit(book);
		finish();
	}

	private void delete() {
		new AlertDialog.Builder(this).setTitle("Confirm")
				.setMessage("are you sure delete?")
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						bookDao.delete(book.getId());
						finish();
					}

				}).setNegativeButton("No", null).show();
	}
}
