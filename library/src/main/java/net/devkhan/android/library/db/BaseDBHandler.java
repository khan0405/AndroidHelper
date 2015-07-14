package net.devkhan.android.library.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KHAN on 2015-07-08.
 */
public abstract class BaseDBHandler extends SQLiteOpenHelper {

    private Context context;

    public BaseDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public BaseDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RepositoryFactory.scanModels(context, db, getModelSanPackage());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RepositoryFactory.dropAllModels(context, db, getModelSanPackage());
        onCreate(db);
    }

    public abstract String[] getModelSanPackage();
}
