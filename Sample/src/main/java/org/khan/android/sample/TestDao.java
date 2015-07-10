package org.khan.android.sample;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import org.khan.android.library.db.AbsGenericDao;
import org.khan.android.sample.model.Test;

/**
 * Created by KHAN on 2015-07-09.
 */
public class TestDao extends AbsGenericDao<Test, Integer> {

    public TestDao(Context context) {
        super(context, Test.class);
    }

    @Override
    protected SQLiteOpenHelper getDefaultDBHelper(Context context) {
        return new DBHandler(context);
    }


}
