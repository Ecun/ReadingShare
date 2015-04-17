package edu.bsu.cs222;

import android.test.AndroidTestCase;
import edu.bsu.cs222.domain.Book;

public class TestBook extends AndroidTestCase {

	public void testFormat() throws Exception{
		String name=" java  program ";
		assertEquals("Java Program",Book.format(name));
	}

	public void testSetName() throws Exception{
		Book book=new Book(" java  program ");
		assertEquals("Java Program",book.getName());
	}

}
