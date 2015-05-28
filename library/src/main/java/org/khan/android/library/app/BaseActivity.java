package org.khan.android.library.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import org.khan.android.library.logging.L;
import org.khan.android.library.ui.UIHandler;

public abstract class BaseActivity extends FragmentActivity implements Callback {
	
	private Handler handler = null;
	
	protected Context getContext() {
		return this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(getWindowFeature());
		super.onCreate(savedInstanceState);
		
		_setWidget();
		setData(getIntent());
	}
	
	private void _setWidget() {
		View rootView = getContentView();
		if (rootView == null) {
			setContentView(getContentViewResId());
		}
		else {
			setContentView(rootView);
		}
		
		setWidget();
	}
	
	protected int getWindowFeature() {
		return Window.FEATURE_NO_TITLE;
	}
	
	protected View getContentView() {
		return null;
	}
	
	protected abstract int getContentViewResId();
	
	protected abstract void setWidget();
	
	protected abstract void setData(Intent intent);
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		setData(intent);
	}

	// View 관련
	/**
	 * findViewById 대체용. 
	 * 
	 * @param view 매핑할 뷰
	 * @param resId 리소스 id
	 * @return 생성된 뷰
	 * @author khan
	 * @since 2013. 12. 29.
	 */
	protected <V extends View> V setView(V view, int resId) {
		return setView(null, view, resId);
	}

	/**
	 * findViewById 대체용. 
	 * 
	 * @param rootView 매핑할 뷰의 상위 뷰
	 * @param view 매핑할 뷰
	 * @param resId 리소스 id
	 * @return 생성된 뷰
	 * @author khan
	 * @since 2013. 12. 29.
	 */
	@SuppressWarnings("unchecked")
	protected <V extends View> V setView(View rootView, V view, int resId) {
		try {
			if (rootView != null) {
				return (V) rootView.findViewById(resId);
			}
			else {
				return (V) findViewById(resId);
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/* handler 관련 */
	public Handler getHandler() {
		if (handler == null) {
			handler = getDefaultHandler();
		}
		return handler;
	}
	
	public Handler getDefaultHandler() {
		return new UIHandler(this);
	}
	
	protected void post(Runnable runnable) {
		getHandler().post(runnable);
	}
	
	protected void postAtFrontOfQueue(Runnable runnable) {
		getHandler().postAtFrontOfQueue(runnable);
	}
	
	protected void postAtTime(Runnable runnable, long uptimeMillis) {
		getHandler().postAtTime(runnable, uptimeMillis);
	}
	
	protected void postAtTime(Runnable runnable, Object token, long uptimeMillis) {
		getHandler().postAtTime(runnable, token, uptimeMillis);
	}
	
	protected void postDelayed(Runnable runnable, long delayMillis) {
		getHandler().postDelayed(runnable, delayMillis);
	}
	
	protected void sendMessage(Message msg) {
		getHandler().sendMessage(msg);
	}
	
	protected void sendMessageDelayed(Message msg, long delayMillis) {
		getHandler().sendMessageDelayed(msg, delayMillis);
	}
	
	protected void sendEmptyMessage(int what) {
		getHandler().sendEmptyMessage(what);
	}
	
	protected Message obtainMessage() {
		return Message.obtain(getHandler());
	}
	
	protected Message obtainMessage(int what) {
		return Message.obtain(getHandler(), what);
	}
	
	protected Message obtainMessage(int what, Object obj) {
		return Message.obtain(getHandler(), what, obj);
	}
	
	protected Message obtainMessage(int what, int arg1) {
		return Message.obtain(getHandler(), what, arg1);
	}
	
	protected Message obtainMessage(int what, int arg1, int arg2) {
		return Message.obtain(getHandler(), what, arg1, arg2);
	}
	
	protected Message obtainMessage(int what, int arg1, int arg2, Object obj) {
		return Message.obtain(getHandler(), what, arg1, arg2, obj);
	}
	
	protected Message obtainMessage(Message org) {
		return Message.obtain(org);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	/* logging */
	protected void logv(String msg) {
		L.v(getLogTag(), msg);
	}
	
	protected void logv(String format, Object... args) {
		L.v(getLogTag(), String.format(format, args));
	}
	
	protected void logv(String msg, Throwable e) {
		L.v(getLogTag(), msg, e);
	}
	
	protected void logd(String msg) {
		L.d(getLogTag(), msg);
	}
	
	protected void logd(String format, Object... args) {
		L.d(getLogTag(), String.format(format, args));
	}
	
	protected void logd(String msg, Throwable e) {
		L.d(getLogTag(), msg, e);
	}
	
	protected void logi(String msg) {
		L.i(getLogTag(), msg);
	}
	
	protected void logi(String format, Object... args) {
		L.i(getLogTag(), String.format(format, args));
	}
	
	protected void logi(String msg, Throwable e) {
		L.i(getLogTag(), msg, e);
	}
	
	protected void logw(String msg) {
		L.w(getLogTag(), msg);
	}
	
	protected void logw(String format, Object... args) {
		L.w(getLogTag(), String.format(format, args));
	}
	
	protected void logw(String msg, Throwable e) {
		L.w(getLogTag(), msg, e);
	}
	
	protected void loge(String msg) {
		L.e(getLogTag(), msg);
	}
	
	protected void loge(String format, Object... args) {
		L.e(getLogTag(), String.format(format, args));
	}
	
	protected void loge(String msg, Throwable e) {
		L.e(getLogTag(), msg, e);
	}
	
	protected String getLogTag() {
		return L.getLogTag(getClass());
	}
}
