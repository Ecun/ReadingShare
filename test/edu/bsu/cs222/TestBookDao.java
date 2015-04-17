package edu.bsu.cs222;

import java.util.List;

import android.test.AndroidTestCase;
import edu.bsu.cs222.dao.BookDao;
import edu.bsu.cs222.domain.Book;

public class TestBookDao extends AndroidTestCase {

	private BookDao bookDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bookDao = new BookDao(getContext());
		bookDao.clear();
		bookDao.add(new Book("c"));
		bookDao.add(new Book("c++"));
		bookDao.add(new Book("java"));
		bookDao.add(new Book("python"));
	}

	@Override
	protected void tearDown() throws Exception {
		bookDao.clear();
		bookDao.closeDB();
		super.tearDown();
	}

	public void testClear() throws Exception {
		bookDao.clear();
		assertEquals(1, bookDao.list().size());
	}

	public void testGet() throws Exception {
		assertNotNull(bookDao.get("Java"));
	}

	public void testAdd() throws Exception {
		bookDao.add(new Book("test"));
		assertNotNull(bookDao.get("Test"));
	}

	public void testEdit() throws Exception {
		Book book = bookDao.get("Java");
		book.setName("delphi");
		bookDao.edit(book);
		assertNotNull(bookDao.get("Delphi"));
	}

	public void testDelete() throws Exception {
		Book book = bookDao.get("Java");
		bookDao.delete(book.getId());
		assertNull(bookDao.get("Java"));
	}

	public void testList() throws Exception {
		List<Book> books = bookDao.list();
		assertEquals(5, books.size());
	}

	public void testSearch() throws Exception {
		List<Book> books = bookDao.search("c");
		assertEquals(2, books.size());
	}

}
