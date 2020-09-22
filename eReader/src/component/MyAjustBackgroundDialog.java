package component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.GridView;

import com.zzj.z_reader.R;

import adapter.BackgroundAdapter;

/**
 * background
 */
public class MyAjustBackgroundDialog extends BaseDialog {

	private GridView gvBackground;
	private BackgroundAdapter adapter;

	public MyAjustBackgroundDialog(Activity activity) {
		// TODO Auto-generated constructor stub
		super(activity);
		adapter = new BackgroundAdapter(activity);
	}

	@SuppressLint("InflateParams")
	public void show() {
		builder.setTitle("choose a theme");
		gvBackground = (GridView) layoutInflater.inflate(
				R.layout.ajust_background, null);
		gvBackground.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		builder.setView(gvBackground);
		builder.create().show();
	}

}
