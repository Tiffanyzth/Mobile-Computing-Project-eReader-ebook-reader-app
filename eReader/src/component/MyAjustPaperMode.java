package component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RadioButton;
import shared.SetupShared;

import com.zzj.z_reader.R;

/**
 * change the page animation
 */
public class MyAjustPaperMode extends BaseDialog {

	public MyAjustPaperMode(Activity activity) {
		// TODO Auto-generated constructor stub
		super(activity);
	}

	@SuppressLint("InflateParams")
	public void show() {
		builder.setTitle("");
		View view = layoutInflater.inflate(R.layout.ajust_page_effect, null);
		final RadioButton rbPaperMode = (RadioButton) view
				.findViewById(R.id.rbPaperMode);
		final RadioButton rbSlideMode = (RadioButton) view
				.findViewById(R.id.rbSlideMode);
		if (0 == SetupShared.getEffectNum()) {
			rbSlideMode.setChecked(true);
		} else {
			rbPaperMode.setChecked(true);
		}
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (rbPaperMode.isChecked()) {
					SetupShared.setEffectNum(1);
				} else if (rbSlideMode.isChecked()) {
					SetupShared.setEffectNum(0);
				}
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

}
