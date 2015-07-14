package net.devkhan.android.library.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	
	public abstract String getFragmentTitle();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = getContentView();
		if (rootView == null) {
			int id = getContentViewResId();
			if (id > 0) {
				rootView = inflater.inflate(id, container, false);
			}
			else {
				throw new RuntimeException("getContentView() or getContentViewResId() not implement");
			}
		}
		
		
		setWidget(rootView);
		setData(getArguments());
		
		return rootView;
	}
	
	protected View getContentView() {
		return null;
	}
	
	protected int getContentViewResId() {
		return 0;
	}
	
	protected abstract void setWidget(View rootView);
	
	protected abstract void setData(Bundle extras);
	
	protected <V extends View> V setView(V view, int resId) {
		return setView(getView(), view, resId);
	}
	
	@SuppressWarnings("unchecked")
	protected <V extends View> V setView(View rootView, V view, int resId) {
		try {
			return (V) rootView.findViewById(resId);
		}
		catch (Exception e) {
			return null;
		}
	}
}
