package org.khan.android.library.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public interface NetworkListener<T> extends Listener<T>, ErrorListener {
	public abstract void loadStart();
	public abstract void onSuccess(T response);
	public abstract void onFailure(VolleyError error);
	public abstract void loadComplete();
}
