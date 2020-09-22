package util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author zzj 
 * @date 2014-7-29
 */
public class ToastUtil {
	private static Toast mToast;
	
	/**
	 * @param context
	 * @param text
	 */
	public static void show(Context context, String text){
		if(mToast == null){
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	
	public static void cancel(){
		if(mToast != null){
			mToast.cancel();
		}
	}
	
}
