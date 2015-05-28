package org.khan.android.library.view;

import java.util.Collection;
import java.util.List;

import org.khan.android.libraries.callback.OnDataLoadListener;


public interface ListAdapter<T, I> {
	List<T> getItems();
	void clearItems();
	void addItems(Collection<T> items);
	void addItems(boolean isRefresh, Collection<T> items);
	void addItems(int index, Collection<T> items);
	void addItems(boolean isRefresh, int index, Collection<T> items);
	void refresh();
	void loadMore();
	void loadMore(I lastId, int size);
	void loadItems(final boolean isRefresh, I lastId, int size, final OnDataLoadListener dataLoadListener);
	void cancel();
	boolean isEnded(int position);
}
