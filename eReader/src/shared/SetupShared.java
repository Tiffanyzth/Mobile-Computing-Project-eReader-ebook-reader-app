package shared;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author zzj 
 * @date 2014-7-29
 * store the settings
 */
public class SetupShared {

	public static final String FONT_SIZE = "font_zize";
	public static final String FONT_COLOR = "font_color";
	public static final String SCREEN_LIGHT = "screen_light";
	public static final String BACKGROUND = "background";
	public static final String PAGE_EFFECT = "page_effect";
	
	private static SharedPreferences sp = null;
	private static Editor editor = null;

	private SetupShared(){
		
	}
	
	public static final void initSetupShared(Application application) {
		sp = application.getSharedPreferences("SetupShared",
				Context.MODE_PRIVATE);
	}

	/**
	 * save String data
	 */
	public static final void saveData(String where, String strData) {
		editor = sp.edit();
		editor.putString(where, strData);
		editor.commit();
	}

	/**
	 * save data
	 */
	public static final void saveData(String where, int intData) {
		editor = sp.edit();
		editor.putInt(where, intData);
		editor.commit();
	}

	/**
	 * clear data
	 */
	public static void clear() {
		editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static void setFontSize(int fontSize) {
		// TODO Auto-generated method stub
		saveData(FONT_SIZE, fontSize);
	}

	public static int getFontSize() {
		// TODO Auto-generated method stub
		return sp.getInt(FONT_SIZE, 18);
	}

	public static void setFontColor(int FontColor) {
		// TODO Auto-generated method stub
		saveData(FONT_COLOR, FontColor);
	}

	public static int getFontColor() {
		// TODO Auto-generated method stub
		return sp.getInt(FONT_COLOR, 0xFF000000);
	}
	
	public static void setSeenLight(int screenLight) {
		// TODO Auto-generated method stub
		saveData(SCREEN_LIGHT, screenLight);
	}
	
	public static int getScreenLight() {
		// TODO Auto-generated method stub
		return sp.getInt(SCREEN_LIGHT, 120);// default 50%
	}

	public static void setBackgroundNum(int bgNum) {
		// TODO Auto-generated method stub
		saveData(BACKGROUND, bgNum);
	}

	public static int getBackgroundNum() {
		// TODO Auto-generated method stub
		return sp.getInt(BACKGROUND, 0);
	}

	public static void setEffectNum(int effectNum) {
		// TODO Auto-generated method stub
		saveData(PAGE_EFFECT, effectNum);
	}

	public static int getEffectNum() {
		// TODO Auto-generated method stub
		return sp.getInt(PAGE_EFFECT, 0);
	}

}
