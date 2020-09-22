package service;

import java.util.List;

import android.content.Context;
import bean.BookInfo;
import sqlite.DbHelper;

/**
 * A service that reads local data for book information
 */
public class BookInfoServiceImpl implements BookInfoService{

	private DbHelper dbHelper;

	public BookInfoServiceImpl(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new DbHelper(context);
	}

	@Override
	public boolean isHaving(String filePath) {
		return dbHelper.isHaving(filePath);
	}

	@Override
	public BookInfo getBookInfo(int id) {
		return dbHelper.getBookInfo(id);
	}

	@Override
	public long insertBookInfo(String filename, String filePath) {
		return dbHelper.insertBookInfo(filename, filePath);
	}

	@Override
	public List<BookInfo> getAllBookInfo() {
		return dbHelper.getAllBookInfo();
	}

	@Override
	public void deleteBookInfo(String filePath) {
		dbHelper.deleteBookInfo(filePath);
	}

	@Override
	public void deleteBookInfo(int id) {
		dbHelper.deleteBookInfo(id);
	}

	@Override
	public void updateBookInfo(int id, String bookName, String bookPath) {
		dbHelper.updateBookInfo(id, bookName, bookPath);
	}

}
