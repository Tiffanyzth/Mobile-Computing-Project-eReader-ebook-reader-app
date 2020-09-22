package activity;

import java.util.ArrayList;
import java.util.List;

import com.zzj.z_reader.R;

import adapter.ShelfAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import bean.BookInfo;
import component.MyAjustBackgroundDialog;
import component.MyAjustFontColorDialog;
import component.MyAjustFontSizeDialog;
import component.MyAjustPaperMode;
import service.BookInfoService;
import service.BookInfoServiceImpl;
import shared.SetupShared;
import util.ToastUtil;

/**
 * the book shelf
 */
public class MainActivity extends Activity {

	private ListView lvShelf;
	private ShelfAdapter adapter;

	private MyAjustPaperMode myAjustPaperMode;
	private MyAjustFontSizeDialog myAjustFontSizeDialog;
	private MyAjustFontColorDialog myAjustFontColorDialog;
	private MyAjustBackgroundDialog myAjustBackgroundDialog;

	private List<BookInfo> bookInfos = new ArrayList<BookInfo>();
	private BookInfoService bookInfoService;

	private long exitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SetupShared.initSetupShared(getApplication());

		lvShelf = (ListView) findViewById(R.id.lvShelf);

		myAjustPaperMode = new MyAjustPaperMode(this);
		myAjustFontSizeDialog = new MyAjustFontSizeDialog(this);
		myAjustFontColorDialog = new MyAjustFontColorDialog(this);
		myAjustBackgroundDialog = new MyAjustBackgroundDialog(this);

		bookInfoService = new BookInfoServiceImpl(getBaseContext());

		update();

	}

	private void update() {
		bookInfos.clear();
		bookInfos.addAll(bookInfoService.getAllBookInfo());
		adapter = new ShelfAdapter(getBaseContext(), bookInfos,
				mShelfItemOnClick);
		lvShelf.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_open:
			Intent intent = new Intent(getBaseContext(), FileListActivity.class);
			startActivityForResult(intent, 1);
			return true;
		case R.id.set_font_color:
			myAjustFontColorDialog.show();
			break;
		case R.id.set_font_size:
			myAjustFontSizeDialog.show();
			break;
		case R.id.set_background:
			myAjustBackgroundDialog.show();
			break;
		case R.id.set_page_effect:
			myAjustPaperMode.show();
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	OnClickListener mShelfItemOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, MyReadActivity.class);
			intent.putExtra("filePath", bookInfos.get(v.getId()).path);
			startActivityForResult(intent, 1);
		}

	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 800) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				ToastUtil.show(this, "press again to exit");
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();
				System.exit(0);
			}
		}
		return true;
	}

}