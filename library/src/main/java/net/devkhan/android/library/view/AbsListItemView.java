package net.devkhan.android.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class AbsListItemView<T> extends RelativeLayout {

	private View mainView;

	private T item;
	
	public AbsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public AbsListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AbsListItemView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		setWidget();
		setEvent();
	}
	
	public void setItem(T item) {
		this.item = item;
		bindData(item);
	}

	public T getItem() {
		return item;
	}

	public void setPosition(int position) {
		// do nothing...
	}
	
	protected abstract int getViewResourceId();

	protected View createMainView() {
		return null;
	}

	private void setWidget() {
		int resourceId = getViewResourceId();
		if (resourceId != 0) {
			mainView = LayoutInflater.from(getContext()).inflate(resourceId, this, true);
		}
		else {
			mainView = createMainView();
		}
		setSubWidget(mainView);
	}

	protected abstract void setSubWidget(View mainView);

	protected abstract void setEvent();

	protected abstract void bindData(T data);

	public View getMainView() {
		return mainView;
	}

	@SuppressWarnings("unchecked")
	protected <V extends View> V setView(V view, int resId) {
		return (V) findViewById(resId);
	}
}
