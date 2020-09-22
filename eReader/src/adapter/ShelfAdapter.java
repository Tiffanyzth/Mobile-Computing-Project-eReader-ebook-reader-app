package adapter;

import java.util.ArrayList;
import java.util.List;

import com.zzj.z_reader.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import bean.BookInfo;

/**
 * shelf adapter
 */
@SuppressLint("ViewHolder")
public class ShelfAdapter extends BaseAdapter {
	
	public int[] size;//lines
	public int realTotalRow;
	public int showTotalRow;
	public int bookCount; 

	private List<BookInfo> bookInfos = new ArrayList<BookInfo>();
	private LayoutInflater layoutInflater;

	private OnClickListener mShelfItemOnClick;
	private OnCreateContextMenuListener mCreateMenuListener;

	public ShelfAdapter(Context context, List<BookInfo> bookInfos,
			OnClickListener mShelfItemOnClick) {
		this.layoutInflater = LayoutInflater.from(context);
		this.mShelfItemOnClick = mShelfItemOnClick;
		if (bookInfos != null) {
			this.bookInfos = bookInfos;
		}
		//get the number of books and divide into lines
		resize();
	}

	public void resize(){
		bookCount = bookInfos.size();
		if (bookCount % 3 > 0) {
			// 1 line to store 1,2 books
			realTotalRow = bookCount / 3 + 1;
		} else {
			realTotalRow = bookCount / 3;
		}
		if (realTotalRow < 4) {
			showTotalRow = 4;
		} else {
			showTotalRow = realTotalRow;
		}
		this.size = new int[showTotalRow];
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (size.length > 3) {
			return size.length;
		} else {
			return 3;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return size[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = layoutInflater.inflate(R.layout.shelf_list_item, null);
		if (position < realTotalRow) {
			int buttonNum = 3;
			if (position + 1 == realTotalRow) {
				buttonNum = bookCount % 3;
			}

			for (int i = 0; i < buttonNum; i++) {
				BookInfo bookInfo = null;
				Button button = null;
				switch (i) {
				case 0:
					bookInfo = bookInfos.get(position * 3);
					button = (Button) layout.findViewById(R.id.button_1);
					setBookInfo(button, bookInfo, position * 3);
					break;
				case 1:
					bookInfo = bookInfos.get(position * 3 + 1);
					button = (Button) layout.findViewById(R.id.button_2);
					setBookInfo(button, bookInfo, position * 3 + 1);
					break;
				case 2:
					bookInfo = bookInfos.get(position * 3 + 2);
					button = (Button) layout.findViewById(R.id.button_3);
					setBookInfo(button, bookInfo, position * 3 + 2);
					break;

				default:
					break;
				}
			}
		}
		return layout;
	}

	private void setBookInfo(Button button, BookInfo bookInfo, int id) {
		button.setVisibility(View.VISIBLE);
		button.setId(id);
		button.setText(bookInfo.name);
		if (mShelfItemOnClick != null) {
			button.setOnClickListener(mShelfItemOnClick);
		}
		if (mCreateMenuListener != null) {
			button.setOnCreateContextMenuListener(mCreateMenuListener);
		}
	}
}
