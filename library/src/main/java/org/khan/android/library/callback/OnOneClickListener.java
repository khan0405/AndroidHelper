package org.khan.android.library.callback;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnOneClickListener implements OnClickListener {
	
	public static long DEFAULT_DELAY_TIME = 300l;
	
	private long delayTime = 0l;
	
	public OnOneClickListener() {
		this(DEFAULT_DELAY_TIME);
	}

	public OnOneClickListener(long delayTime) {
		this.delayTime = delayTime;
	}

	@Override
	public void onClick(final View v) {
		if (delayTime > 0l) {
			v.setClickable(false);
			v.postDelayed(new Runnable() {
				@Override
				public void run() {
					v.setClickable(true);
				}
			}, delayTime);
			onClicked(v);
		}
		else {
			onClicked(v);
		}
	}
	
	public abstract void onClicked(View v);
}
