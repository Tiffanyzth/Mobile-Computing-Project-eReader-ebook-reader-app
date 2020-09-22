package component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import bean.BackgroundSource;
import service.BookPageService;
import shared.SetupShared;
import util.ToastUtil;
import view.PageView;

/**
 * @author zzj 
 * @date 2014-7-29
 */

@SuppressLint({ "ClickableViewAccessibility", "WrongCall" })
public class MyPaperTextShow implements OnTouchListener {

	private Activity activity;

	private PageView mPageView;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private Canvas mCurPageCanvas, mNextPageCanvas;

	private BookPageService bookPageService;

	private Bitmap m_book_bg = null;
	private int mWidth = 700;
	private int mHeight = 1280;

	private int m_fontSize = 32;
	private int m_textColor = Color.BLACK;
	private int m_backColor = 0xffff9e85; 
	private int marginWidth = 15; 
	private int marginHeight = 20; 

	private int mLineCount; // the number of lines
	private float mVisibleHeight; // content height
	private float mVisibleWidth; // content width

	private Paint mPaint;

	public MyPaperTextShow(Activity activity) {
		// TODO Auto-generated constructor stub

		init(activity);
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.activity = activity;

		mPageView = new PageView(activity, mWidth, mHeight);
		activity.setContentView(mPageView);

		mCurPageBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mLineCount = (int) (mVisibleHeight / m_fontSize); // lines ti be shown

	}

	private void init(Activity activity) {

		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
		mHeight = metric.heightPixels;

		m_fontSize = SetupShared.getFontSize() * 2;
		m_textColor = SetupShared.getFontColor();
		int backgroundNum = SetupShared.getBackgroundNum();
		m_book_bg = BitmapFactory.decodeResource(activity.getResources(),
				BackgroundSource.background[backgroundNum]);

	}

	public void loadBook(String filePath) {
		try {
			bookPageService = new BookPageService(mPaint, mVisibleHeight,
					mVisibleWidth, mLineCount);
			bookPageService.openbook(filePath);
			onDraw(mCurPageCanvas);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ToastUtil.show(activity, "the book does not exist");
		}

		mPageView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageView.setOnTouchListener(this);
	}

	@SuppressLint("ClickableViewAccessibility")
	public PageView getPageView() {
		return this.mPageView;
	}

	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas c) {
		Vector<String> m_lines = bookPageService.getTextVector();
		if (m_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight;
			for (String strLine : m_lines) {
				y += m_fontSize;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}
		float fPercent = bookPageService.getPercent();
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";
		int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		// TODO Auto-generated method stub
		boolean ret = false;
		if (v == mPageView) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				mPageView.abortAnimation();
				mPageView.calcCornerXY(e.getX(), e.getY());

				onDraw(mCurPageCanvas);
				if (mPageView.DragToRight()) {
					try {
						bookPageService.prePage();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (bookPageService.isfirstPage()) {
						ToastUtil.show(activity, "this is the first page");
						return false;
					}
					onDraw(mNextPageCanvas);
				} else {
					try {
						bookPageService.nextPage();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (bookPageService.islastPage()) {
						ToastUtil.show(activity, "already the last page");
						return false;
					}
					onDraw(mNextPageCanvas);
				}
				mPageView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			}

			ret = mPageView.doTouchEvent(e);
			return ret;
		}
		return false;
	}
}
