package activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zzj.z_reader.R;

import adapter.FileListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import bean.FileListItem;
import service.BookInfoService;
import service.BookInfoServiceImpl;
import android.widget.ListView;

/**
 * the file browser
 */
public class FileListActivity extends Activity implements OnItemClickListener {

	private ListView lvFileList;
	private FileListAdapter adapter;

	private List<FileListItem> itemList = new ArrayList<FileListItem>();
	private File currentDirectory = new File("/");

	private BookInfoService bookInfoService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);

		lvFileList = (ListView) findViewById(R.id.lvFileList);
		bookInfoService = new BookInfoServiceImpl(getBaseContext());
		adapter = new FileListAdapter(getBaseContext(), itemList);
		lvFileList.setAdapter(adapter);
		lvFileList.setOnItemClickListener(this);
		// start with the root directory
		browseToRoot();
	}

	/**
	 * the root directory
	 */
	private void browseToRoot() {
		browseTo(new File("/"));
	}

	/**
	 * browser the directory or open the file
	 */
	private void browseTo(File file) {
		this.setTitle(file.getAbsolutePath());
		if (file.isDirectory()) {
			this.currentDirectory = file;
			File[] files = file.listFiles();
			if (files == null) {
				files = new File[] {};
			}
			// store the list
			fill(files);
		} else {
			if (checkEndsWithInStringArray(file.getName(), getResources().getStringArray(R.array.fileEndingText))) {
				// open files
				String filePath = this.currentDirectory.getAbsolutePath() + "/" + file.getName();
				// delete the type
				String fileName = file.getName();
				int pos = fileName.indexOf(".");
				fileName = fileName.substring(0,pos);
				
				Intent intent = new Intent(getBaseContext(),MyReadActivity.class);
				intent.putExtra("filePath", filePath);
				if (bookInfoService.isHaving(filePath)) {
					bookInfoService.deleteBookInfo(filePath);
				}
				bookInfoService.insertBookInfo(fileName, filePath);
				startActivity(intent);
			}

		}
	}

	/**
	 * up one level
	 */
	private void upOneLevel() {
		if (this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
	}

	/**
	 * set the resource of ListActivity
	 */
	private void fill(File[] files) {
		
		this.itemList.clear();

		// refresh
		this.itemList.add(new FileListItem(getString(R.string.current_dir), R.drawable.folder));
		// If it is not the root, add the previous directory
		if (this.currentDirectory.getParent() != null)
			this.itemList.add(new FileListItem(
					getString(R.string.up_one_level), R.drawable.uponelevel));

		int currentIcon = 0;
		// get each value of files ,pass to currentFile
		for (File currentFile : files) {
			
			// set the icon, file or folder
			if (currentFile.isDirectory()) {
				currentIcon = R.drawable.folder;
			} else {
				String fileName = currentFile.getName();
				if (checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingImage))) {
					currentIcon = R.drawable.image;
				} else if (checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingWebText))) {
					currentIcon = R.drawable.webtext;
				} else if (checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingPackage))) {
					currentIcon = R.drawable.packed;
				} else if (checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingAudio))) {
					currentIcon = R.drawable.audio;
				} else if (checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingVideo))) {
					currentIcon = R.drawable.video;
				} else {
					currentIcon = R.drawable.text;
				}
			}
			// only show file name not path
			int currentPathStringLenght = this.currentDirectory
					.getAbsolutePath().length();
			this.itemList.add(new FileListItem(currentFile
					.getAbsolutePath().substring(currentPathStringLenght),
					currentIcon));
		}
		//order with names
		Collections.sort(this.itemList);
		// refresh view
		adapter.notifyDataSetChanged();
	}

	/**
	 * determine file type
	 */
	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// get the file name selected
		String selectedFileString = this.itemList.get(position).fileName;

		if (selectedFileString.equals(getString(R.string.current_dir))) {
			// refresh
			this.browseTo(this.currentDirectory);
		} else if (selectedFileString.equals(getString(R.string.up_one_level))) {
			// up one level
			this.upOneLevel();
		} else {

			File clickedFile = null;
			clickedFile = new File(this.currentDirectory.getAbsolutePath()
					+ this.itemList.get(position).fileName);

			if (clickedFile != null)
				this.browseTo(clickedFile);
		}
	}

}
