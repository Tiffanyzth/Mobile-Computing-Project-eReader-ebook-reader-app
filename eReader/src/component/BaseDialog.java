package component;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.view.LayoutInflater;

/**
 * base dialog
 */
public abstract class BaseDialog {

	protected Builder builder;
	protected LayoutInflater layoutInflater;

	public BaseDialog(Activity activity) {
		builder = new Builder(activity);
		layoutInflater = LayoutInflater.from(activity);
	}

	public abstract void show();

}
