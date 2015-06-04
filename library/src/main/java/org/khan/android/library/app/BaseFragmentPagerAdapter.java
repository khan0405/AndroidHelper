package org.khan.android.library.app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import org.khan.android.library.logging.L;
import org.khan.android.library.util.CollectionUtil;

import java.util.List;

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

	private BaseActivity activity;
	
	private List<Fragment> items;
	
	public BaseFragmentPagerAdapter(BaseActivity activity) {
		super(activity.getSupportFragmentManager());
		this.activity = activity;
	}
	
	Context getContext() {
		return activity;
	}
	
	protected abstract List<Fragment> generateItems();

	protected final List<Fragment> getItems() {
		if (items == null) {
			items = generateItems();
		}
		return items;
	};
	
	@Override
	public Fragment getItem(int position) {
		L.i(getClass(), String.format("getItem(%s)", position));
		List<Fragment> items = getItems();
		if (CollectionUtil.notEmpty(items)) {
			if (position >= 0 && position < items.size()) {
				return items.get(position);
			}
		}
		return null;
	}

	@Override
	public int getCount() {
		List<Fragment> items = getItems();
		if (CollectionUtil.notEmpty(items)) {
			return items.size();
		}
		return 0;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		Fragment fragment = getItem(position);
		if (fragment != null && fragment instanceof BaseFragment) {
			return ((BaseFragment) fragment).getFragmentTitle();
		}
		return "Page " + (position + 1);
	}
}
