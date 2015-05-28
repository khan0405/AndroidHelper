package org.khan.android.library.db;

import java.io.Closeable;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDao implements Closeable {
	
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
	
	protected <V> V executeWithTransaction(Callable<V> task) {
		SQLiteDatabase db = getWritableDB();
		try {
			db.beginTransaction();
			
			V obj = task.call();
			
			db.setTransactionSuccessful();
			
			return obj;
		} 
		catch (SQLException e) {
			
		}
		catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
		
		return null;
	}
}
