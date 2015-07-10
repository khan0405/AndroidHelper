package org.khan.android.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.khan.android.library.db.BaseDBHandler;

/**
 * Created by KHAN on 2015-07-09.
 */
public class DBHandler extends BaseDBHandler {
    public static final String DB_NAME = "test_db";
    public static final int DB_VERSION = 1;
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public String[] getModelSanPackage() {
        return new String[]{"org.khan.android.sample.model"};
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        
    }
}
