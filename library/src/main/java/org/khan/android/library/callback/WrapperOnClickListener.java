package org.khan.android.library.callback;

import android.view.View;
import android.view.View.OnClickListener;

public class WrapperOnClickListener implements OnClickListener {

	private OnClickListener listener;
	
	public WrapperOnClickListener() {
		this(null);
	}
	
	public WrapperOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onClick(v);
		}
	}
}
