package component;

import java.io.IOException;

import com.zzj.z_reader.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;
import bean.BackgroundSource;
import service.BookPageService;
import shared.SetupShared;
import util.ToastUtil;

/**
 * Read the file and return a string of characters to display per page
 */
@SuppressLint("InflateParams")
public class MySlideTextShow implements OnGestureListener {

	private Activity activity;

	private ViewFlipper viewFlipper;

	private TextView tvFreeView;
	private TextView tvBusyView;

	private View itemFreeView;
	private View itemBusyView;

	private BookPageService bookPageService;

	public MySlideTextShow(Activity activity) {
		this.activity = activity;
		viewFlipper = (ViewFlipper) activity.findViewById(R.id.viewflipper);
		LayoutInflater inflater = LayoutInflater.from(activity);
		for (int i = 0; i < 2; i++) {
			View view = inflater.inflate(R.layout.text_item, null);
			TextView mtv = (TextView) view.findViewById(R.id.tvTextShow);
			if (0 == i){
				tvFreeView = mtv;
				itemFreeView = view;
			} else {
				tvBusyView = mtv;
				itemBusyView = view;
			}
			viewFlipper.addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
	}

	public void init() {
		int textSize = SetupShared.getFontSize();
		int textColor = SetupShared.getFontColor();
		int backgroundNum = SetupShared.getBackgroundNum();
		int screenLight = SetupShared.getScreenLight();
		tvBusyView.setTextSize(textSize);
		tvBusyView.setTextColor(textColor);
		tvFreeView.setTextSize(textSize);
		tvFreeView.setTextColor(textColor);

		itemBusyView
				.setBackgroundResource(BackgroundSource.background[backgroundNum]);
		itemFreeView
				.setBackgroundResource(BackgroundSource.background[backgroundNum]);

		WindowManager.LayoutParams wl = activity.getWindow().getAttributes();
		wl.screenBrightness = screenLight;
		activity.getWindow().setAttributes(wl);
	}

	public void loadBook(String filePath) {
		try {
			bookPageService = new BookPageService(tvFreeView);
			bookPageService.openbook(filePath);
			tvFreeView.setText(bookPageService.getText());
			changePage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ToastUtil.show(activity, "The book does not exist");
		}
	}

	/**
	 * exchange the references of tvBusyView and tvFreeView
	 */
	private void changePage() {
		TextView mtv = tvBusyView;
		tvBusyView = tvFreeView;
		tvFreeView = mtv;
	}

	public ViewFlipper getViewFlipper(){
		return viewFlipper;
	}
	
	/**
	 * show last page
	 */
	public void previewPage(TextView tvTextShow) {
		try {
			bookPageService.prePage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (bookPageService.isfirstPage()) {
			return;
		}
		tvFreeView.setText(bookPageService.getText());
	}

	/**
	 * show next page
	 */
	public void nextPage(TextView tvTextShow) {
		try {
			bookPageService.nextPage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (bookPageService.islastPage()) {
			return;
		}
		tvFreeView.setText(bookPageService.getText());
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		if (e1.getX() - e2.getX() > 20) {
			nextPage(tvFreeView);

			if (bookPageService.islastPage()) {
				ToastUtil.show(activity, "already read the last page");
				return true;
			}

			// animation
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
					activity, R.anim.push_left_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
					activity, R.anim.push_left_out));
			this.viewFlipper.showNext();
			changePage();
			return true;
		} else if (e1.getX() - e2.getX() < -20) {

			previewPage(tvFreeView);
			if (bookPageService.isfirstPage()) {
				ToastUtil.show(activity, "this is the first page");
				return true;
			}
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
					activity, R.anim.push_right_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
					activity, R.anim.push_right_out));
			this.viewFlipper.showPrevious();
			changePage();
			return true;
		}
		return true;

	}

}
