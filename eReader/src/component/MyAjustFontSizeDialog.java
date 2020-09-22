package component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import shared.SetupShared;
import util.ToastUtil;
import android.widget.TextView;

import com.zzj.z_reader.R;

/**
 * choose font size
 */
public class MyAjustFontSizeDialog extends BaseDialog {
	private SeekBar sbFontSize;
	private TextView tvFontSize;
	private Activity activity;
	private final int MIN_SIZE = 10;
	private final int MAX_SIZE = 36;

	public MyAjustFontSizeDialog(Activity activity) {
		// TODO Auto-generated constructor stub
		super(activity);
		builder = new Builder(activity);
		this.activity = activity;
		layoutInflater = LayoutInflater.from(activity);
	}

	@SuppressLint("InflateParams")
	public void show() {
		builder.setTitle("change the font size");
		View view = layoutInflater.inflate(R.layout.ajust_font_size, null);
		sbFontSize = (SeekBar) view.findViewById(R.id.sbSetFontSize);
		tvFontSize = (TextView) view.findViewById(R.id.tvSetFontSize);
		int initSize = SetupShared.getFontSize();
		tvFontSize.setTextSize(initSize);
		sbFontSize.setMax(MAX_SIZE - MIN_SIZE);
		sbFontSize.setProgress(initSize - MIN_SIZE);
		sbFontSize.setOnSeekBarChangeListener(mChangeListener);
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SetupShared.setFontSize(sbFontSize.getProgress() + MIN_SIZE);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setView(view);
		builder.create().show();
	}

	OnSeekBarChangeListener mChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			tvFontSize.setTextSize(MIN_SIZE + progress);
			ToastUtil.show(activity, String.valueOf(MIN_SIZE + progress));
		}
	};

}
