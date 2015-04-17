package edu.bsu.cs222;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Book;
import edu.bsu.cs222.domain.Word;

public class ShareFragment extends BaseFragment {

	private WebView webView;
	private BookDao bookDao;
	private WordDao wordDao;
	private Button btnSend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_share, container, false);
		bookDao = new BookDao(getActivity());
		wordDao = new WordDao(getActivity());
		webView = (WebView) view.findViewById(R.id.webView);
		btnSend = (Button) view.findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				send();
			}
		});
		return view;
	}

	private String getHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		List<Book> books = bookDao.list();
		for (Book book : books) {
			sb.append("<h3>" + book.getName() + "</h3>\n");
			List<Word> words = wordDao.listByBookId(book.getId());
			for (Word word : words) {
				sb.append("<ul>");
				sb.append("<li>" + word.getName()
						+ "<font size='0.5' color='red'> - "
						+ word.getComment() + "</font></li>");
				sb.append("</ul>");
			}
		}
		sb.append("</html>\n");
		return sb.toString();
	}

	@Override
	public boolean hasAdd() {
		return false;
	}

	@Override
	public void active() {
		webView.loadData(getHtml(), "text/html", "utf-8");
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

	public static File saveToFile(String html) {
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(storageDir, "word.html");
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(html);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	private void send() {
		String[] subject = new String[] { "Reading Share" };
		String html = getHtml();
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, html);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(saveToFile(html)));
		startActivity(Intent.createChooser(intent, "Reading Share"));
	}
}
