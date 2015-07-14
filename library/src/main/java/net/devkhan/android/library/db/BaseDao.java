package net.devkhan.android.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Closeable;
import java.io.Serializable;
import java.util.concurrent.Callable;

public abstract class BaseDao<T, ID extends Serializable> implements Closeable {
	
	private SQLiteOpenHelper helper;
	
	private SQLiteDatabase readableDB;
	private SQLiteDatabase writableDB;

	public BaseDao(Context context) {
		helper = getDefaultDBHelper(context);
	}
	
	protected abstract SQLiteOpenHelper getDefaultDBHelper(Context context);
	
	protected SQLiteDatabase getReadableDB() {
		if (readableDB == null) {
			readableDB = helper.getReadableDatabase();
		}

		return readableDB;
	}
	
	protected SQLiteDatabase getWritableDB() {
		if (writableDB == null) {
			writableDB = helper.getWritableDatabase();
		}
		return writableDB;
	}
	
	
	public synchronized void close() {
		if (readableDB != null) {
			readableDB.close();
			readableDB = null;
		}
		
		if (writableDB != null) {
			writableDB.close();
			writableDB = null;
		}

		helper.close();
		helper = null;
	}

	protected static void executeWithTransaction(SQLiteDatabase db, Runnable task) {
		try {
			db.beginTransaction();
			task.run();
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
	}

	protected static <V> V executeWithTransaction(SQLiteDatabase db, Callable<V> task) {
		try {
			db.beginTransaction();
			V obj = task.call();
			db.setTransactionSuccessful();
			return obj;
		}
		catch (Exception e) {
			// do nothing..
			return null;
		}
		finally {
			db.endTransaction();
		}
	}
}
