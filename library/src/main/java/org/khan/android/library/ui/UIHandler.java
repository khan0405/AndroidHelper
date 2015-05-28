package org.khan.android.library.ui;

import android.os.Handler;
import android.os.Looper;

public class UIHandler extends Handler {

	private static UIHandler instance;
	
	public static UIHandler getInstance() {
		if (instance == null)
			instance = new UIHandler();
		return instance;
	}
	
	public UIHandler() {
		this(Looper.getMainLooper());
	}

	public UIHandler(Callback callback) {
		this(Looper.getMainLooper(), callback);
	}

	public UIHandler(Looper looper, Callback callback) {
		super(looper, callback);
	}

	public UIHandler(Looper looper) {
		super(looper);
	}
	
	public static void postUiThread(Runnable task) {
		getInstance().post(task);
	}
	
	public static void postUiThreadDelayed(Runnable task, long delayMillis) {
		getInstance().postDelayed(task, delayMillis);
	}
	
	public static boolean isUIThread(Thread thread) {
		return thread == Looper.getMainLooper().getThread();
	}
}
