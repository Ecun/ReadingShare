package edu.bsu.cs222;

import java.util.List;

import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.domain.Book;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookFragment extends BaseFragment {

	private ListView lvBook;
	private List<Book> books;

	private BookDao bookDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_book, container, false);

		bookDao = new BookDao(getActivity());

		lvBook = (ListView) view.findViewById(R.id.lv_book);
		lvBook.setAdapter(adapter);
		
		return view;
	}

	@Override
	public void active() {
		books = bookDao.list();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		active();
	}
	
	@Override
	public void onDestroy() {
		bookDao.closeDB();
		super.onDestroy();
	}
	
	@Override
	public boolean hasAdd() {
		return true;
	}

	@Override
	public boolean add(String name) {
		Book book = new Book(name);
		if (bookDao.get(book.getName()) != null) {
			Toast.makeText(getActivity(), name + " is exists.",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		bookDao.add(book);
		books = bookDao.list();
		adapter.notifyDataSetChanged();
		return true;
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
				convertView = getLayoutInflater(null).inflate(
						R.layout.book_item, parent, false);
			}
			TextView tvBook = (TextView) convertView.findViewById(R.id.tv_book);
			final Book book = books.get(position);
			tvBook.setText(book.getName());
			tvBook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!book.getName().equals(BookDao.UNKNOWN)) {
						Intent intent = new Intent(getActivity(),
								EditBookActivity.class);
						intent.putExtra("book", book);
						startActivity(intent);
					}
				}

			});
			return convertView;
		}

	};
}
