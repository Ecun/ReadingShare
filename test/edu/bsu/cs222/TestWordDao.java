package edu.bsu.cs222;

import java.util.List;

import android.test.AndroidTestCase;
import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Word;

public class TestWordDao extends AndroidTestCase {

	private WordDao wordDao;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wordDao=new WordDao(getContext());
		wordDao.clear();
		wordDao.add(new Word(1,"for"));
		wordDao.add(new Word(1,"which"));
		wordDao.add(new Word(2,"each"));
		wordDao.add(new Word(2,"enum"));
	}
	
	@Override
	protected void tearDown() throws Exception {
		wordDao.clear();
		wordDao.closeDB();
		super.tearDown();
	}
	
	public void testClear() throws Exception{
		wordDao.clear();
		assertEquals(0, wordDao.list().size());
	}

	public void testGet() throws Exception{
		assertNotNull(wordDao.get("for"));
	}

	public void testAdd() throws Exception{
		wordDao.add(new Word(1,"while"));
		assertNotNull(wordDao.get("while"));
	}

	public void testEdit() throws Exception{
		Word word=wordDao.get("for");
		word.setName("ArrayList");
		wordDao.edit(word);
		assertNotNull(wordDao.get("ArrayList"));
	}

	public void testList() throws Exception{
		List<Word> words=wordDao.list();
		assertEquals(4,words.size());
	}

	public void testListByBookId() throws Exception{
		List<Word> words=wordDao.listByBookId(1);
		assertEquals(2,words.size());
	}

	public void testSearch() throws Exception{
		List<Word> words=wordDao.search("e");
		assertEquals(2,words.size());
	}

}
