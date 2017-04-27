package vision.genesis.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import vision.genesis.ui.common.BaseActivity;


public class ContextUtils {

	@NonNull
	public static BaseActivity getBaseActivity(Context context) {
		Context baseContext = context;
		while (!(baseContext instanceof BaseActivity)) {
			if (baseContext instanceof ContextWrapper) {
				baseContext = ((ContextWrapper) baseContext).getBaseContext();
			} else {
				throw new IllegalArgumentException("Context " + context + " is not BaseActivity and not contain it.");
			}
		}
		return (BaseActivity) baseContext;
	}
}
