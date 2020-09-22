package service;

import java.util.List;

import bean.BookInfo;

public interface BookInfoService {

	/**
	 * Determine whether the file information has been stored
	 */
	public boolean isHaving(String filePath);

	/**
	 * Get the book information of the corresponding id
	 */
	public BookInfo getBookInfo(int id);

	/**
	 * Insert the file information
	 */
	public long insertBookInfo(String filename, String filePath);

	/**
	 * Get a list of all the books
	 */
	public List<BookInfo> getAllBookInfo();

	/**
	 * Delete book information whose path is filePath
	 */
	public void deleteBookInfo(String filePath);

	/**
	 * Delete the book information for the corresponding id
	 */
	public void deleteBookInfo(int id);

	/**
	 * Update the book information for the corresponding id
	 */
	public void updateBookInfo(int id, String bookName, String bookPath);

}
