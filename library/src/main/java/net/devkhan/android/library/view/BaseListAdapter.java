package net.devkhan.android.library.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import net.devkhan.android.library.callback.OnDataLoadListener;
import net.devkhan.android.library.ui.UIHandler;
import net.devkhan.android.library.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class BaseListAdapter<T, I, V extends AbsListItemView<T>> extends BaseAdapter implements ListAdapter<T, I>, OnDataLoadListener {

	public static final int DEFAULT_PAGE_SIZE = 20;

	private List<T> items;

	private Context context;
	
	private I lastId;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private boolean loading = false;

	private boolean hasMore = true;
	
	private OnDataLoadListener dataLoadListener;

	public BaseListAdapter(Context context) {
		this(context, null);
	}

	public BaseListAdapter(Context context, OnDataLoadListener listener) {
		this.context = context;
		setOnDataLoadListener(listener);
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public OnDataLoadListener getOnDataLoadListener() {
		return dataLoadListener;
	}

	public void setOnDataLoadListener(OnDataLoadListener dataLoadListener) {
		this.dataLoadListener = dataLoadListener;
	}

	public Context getContext() {
		return context;
	}

	public I getLastId() {
		return lastId;
	}

	public void setLastId(I lastId) {
		this.lastId = lastId;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public int getCount() {
		return getItems().size();
	}

	@Override
	public T getItem(int position) {
		if (position > -1 && getItems().size() > position) {
			return getItems().get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public List<T> getItems() {
		if (items == null) {
			items = new ArrayList<T>();
		}
		return items;
	}
	
	@Override
	public void clearItems() {
		getItems().clear();
	}

	public void addItem(T item) {
		addItem(-1, item);
	}

	public void addItem(int index, T item) {
		if (item != null) {
			if (getItems().size() > index && index > -1) {
				getItems().add(index, item);
			}
			else {
				getItems().add(item);
			}
		}
	}
	
	@Override
	public void addItems(Collection<T> items) {
		addItems(false, -1, items);
	}

	@Override
	public void addItems(boolean isRefresh, Collection<T> items) {
		addItems(isRefresh, -1, items);
	}

	@Override
	public void addItems(int index, Collection<T> items) {
		addItems(false, index, items);
	}

	@Override
	public void addItems(boolean isRefresh, int index, Collection<T> items) {
		if (isRefresh) {
			clearItems();
		}
		if (CollectionUtil.notEmpty(items)) {
			if (getItems().size() > index && index > -1) {
				getItems().addAll(index, items);
			}
			else {
				getItems().addAll(items);
			}
		}
	}
	
	protected boolean isLoading() {
		return loading;
	}
	
	@Override
	public void refresh() {
		if (!loading) {
			loadItems(true, null, pageSize, this);
		}
	}

	@Override
	public void loadMore() {
		loadMore(lastId, DEFAULT_PAGE_SIZE);
	}
	
	@Override
	public void loadMore(I lastId, int size) {
		if (!loading) {
			loadItems(false, lastId, size, this);
		}
	}

	@Override
	public void cancel() {
		// do nothing...
	}

	@Override
	public boolean isEnded(int position) {
		return getCount() == position + 1 && !loading && hasMore;
	}
	
	@Override
	public void onDataLoadStart() {
		loading = true;
		if (dataLoadListener != null) {
			dataLoadListener.onDataLoadStart();
		}
	}
	
	@Override
	public void onDataLoadComplete() {
		loading = false;
		notifyDataSetChanged();
		
		if (dataLoadListener != null) {
			dataLoadListener.onDataLoadComplete();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		V view;
		if (convertView == null || !(convertView instanceof AbsListItemView)) {
			view = generateView(position, parent);
		}
		else {
			view = (V) convertView;
		}
		T item = getItem(position);
		view.setPosition(position);
		if (item != null) {
			view.setItem(item);
		}
		
		if (isEnded(position)) {
			loadMore();
		}
		
		return view;
	}
	
	public abstract V generateView(int position, ViewGroup parent);
	
	@Override
	public void notifyDataSetChanged() {
		UIHandler.postUiThread(new Runnable() {
			public void run() {
				BaseListAdapter.super.notifyDataSetChanged();
			}
		});
	}
}
