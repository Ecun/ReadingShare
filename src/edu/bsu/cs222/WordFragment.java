package edu.bsu.cs222;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Book;
import edu.bsu.cs222.domain.Word;

public class WordFragment extends BaseFragment {
	
	private ListView lvWord;
	private Spinner spinBook;
	private List<Word> words;
	private List<Book> books;

	private BookDao bookDao;
	private WordDao wordDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_word, container, false);
		
		bookDao = new BookDao(getActivity());
		wordDao = new WordDao(getActivity());

		spinBook = (Spinner) view.findViewById(R.id.spin_book);
		spinBook.setAdapter(bookAdapter);
		spinBook.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				searchWord();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		lvWord = (ListView) view.findViewById(R.id.lv_word);
		lvWord.setAdapter(wordAdapter);
		
		return view;
	}

	private void searchWord(){
		if(spinBook.getSelectedItemPosition()==0){
			words = wordDao.list();			
		}else{
			words=wordDao.listByBookId((int) spinBook.getSelectedItemId());
		}
		wordAdapter.notifyDataSetChanged();		
	}
	
	@Override
	public void active() {
		books=bookDao.list();
		books.add(0, new Book(-1,"choose your book"));
		bookAdapter.notifyDataSetChanged();
		
		if(spinBook.getSelectedItemPosition()==-1){
			spinBook.setSelection(0);
		}
		searchWord();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		active();
	}
	
	@Override
	public void onDestroy() {
		bookDao.closeDB();
		wordDao.closeDB();
		super.onDestroy();
	}
	
	@Override
	public boolean hasAdd() {
		return true;
	}

	@Override
	public boolean add(String name) {
		int bookId=(int) spinBook.getSelectedItemId();
		if(bookId==-1){
			bookId=bookDao.get(BookDao.UNKNOWN).getId();
		}
		Word word = new Word(bookId,name);
		if (wordDao.get(word.getName()) != null) {
			Toast.makeText(getActivity(), name + " is exists.",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		wordDao.add(word);
		searchWord();
		return true;
	}
	
	private ListAdapter wordAdapter = new ListAdapter() {
		@Override
		public int getCount() {
			if (words == null) {
				return 0;
			}
			return words.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater(null).inflate(
						R.layout.word_item, parent, false);
			}
			TextView tvName = (TextView) convertView
					.findViewById(R.id.tv_name);
			TextView tvBook = (TextView) convertView
					.findViewById(R.id.tv_book);
			TextView tvComment = (TextView) convertView
					.findViewById(R.id.tv_comment);
			ImageView ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			final Word word = words.get(position);
			tvName.setText(word.getName());
			if (word.getFlag() == 0) {
				ivIcon.setImageResource(R.drawable.star_empty);
			} else {
				ivIcon.setImageResource(R.drawable.star);
			}
			Book book=bookDao.get(word.getBookId());
			tvBook.setText("Book: " + book.getName());
			if (TextUtils.isEmpty(word.getComment())) {
				tvComment.setVisibility(View.GONE);
				tvComment.setText("");
			} else {
				tvComment.setVisibility(View.VISIBLE);
				tvComment.setText("Comment: " + word.getComment());
			}
			OnClickListener editListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							EditWordActivity.class);
					intent.putExtra("word", word);
					startActivity(intent);
				}

			};
			tvName.setOnClickListener(editListener);
			tvBook.setOnClickListener(editListener);
			tvComment.setOnClickListener(editListener);
			ivIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					word.setFlag(1 - word.getFlag());
					wordDao.edit(word);
					searchWord();
					lvWord.setSelection(words.indexOf(word));
				}

			});
			return convertView;
		}

	};
	
	private ListAdapter bookAdapter = new ListAdapter() {
		@Override
		public int getCount() {
			if (books == null) {
				return 0;
			}
			return books.size();
		}

		@Override
		public long getItemId(int position) {
			Book book=books.get(position);
			return book.getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater(null).inflate(
						R.layout.book_item_small, parent, false);
			}
			TextView tvBook = (TextView) convertView.findViewById(R.id.tv_book);
			final Book book = books.get(position);
			tvBook.setText(book.getName());
			return convertView;
		}

	};
	
}
