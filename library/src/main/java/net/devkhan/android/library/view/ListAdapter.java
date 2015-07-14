package net.devkhan.android.library.view;

import net.devkhan.android.library.callback.OnDataLoadListener;

import java.util.Collection;
import java.util.List;


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
