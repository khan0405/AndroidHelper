package org.khan.android.library.db;

import android.content.Context;
import android.database.Cursor;
import org.khan.android.library.db.annotation.Entity;
import org.khan.android.library.db.annotation.Id;
import org.khan.android.library.db.annotation.IgnoreField;
import org.khan.android.library.util.ObjectConverter;
import org.khan.android.library.util.ReflectionUtil;
import org.khan.android.library.util.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.khan.android.library.db.RepositoryFactory.getDBFieldName;

/**
 * Abstract Generic Dao class.
 * Mapping the class and table.
 *
 * Created by KHAN on 2015-07-09.
 */
public abstract class AbsGenericDao<T extends BaseModel, ID extends Serializable> extends BaseDao implements GenericDao<T, ID> {
    private static final String SEPARATOR = ",";
    private static final String SELECTION_ID_FORMAT = "%s=?";
    private Class<T> baseClass;
    private String tableName;
    private String[] allColumns;
    private String idFieldName;

    public AbsGenericDao(Context context, Class<T> baseClass) {
        super(context);
        initDao(baseClass);
    }

    private void initDao(Class<T> baseClass) {
        this.baseClass = baseClass;
        initTableName();
        initAllColumns();
    }

    protected void initTableName() {
        Entity entity = baseClass.getAnnotation(Entity.class);
        if (entity != null && StringUtil.notEmpty(entity.value())) {
            tableName = entity.value();
        }
        else {
            tableName = baseClass.getSimpleName();
        }
    }

    protected void initAllColumns() {
        List<String> columns = new ArrayList<String>();
        List<Field> fields = new ArrayList<Field>();
        ReflectionUtil.setAllFieldFromClass(fields, baseClass);
        for (Field field : fields) {
            IgnoreField ignore = field.getAnnotation(IgnoreField.class);
            if (ignore == null) {
                columns.add(getDBFieldName(field));
                Id id = field.getAnnotation(Id.class);
                if (id != null) {
                    idFieldName = getDBFieldName(field);
                }

            }
        }
        allColumns = columns.toArray(new String[columns.size()]);
    }

    protected String getTableName() {
        return tableName;
    }

    protected String[] getAllColumns() {
        return allColumns;
    }

    protected String getIdFieldName() {
        return idFieldName;
    }

    private String getSelectionId() {
        return String.format(SELECTION_ID_FORMAT, getIdFieldName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> S save(S entity) {
        ID id = (ID) entity.getId();
        if (exists(id)) {
            // update
            getWritableDB().update(getTableName(), entity.toContentValues(), getSelectionId(), new String[]{id.toString()});
        }
        else {
            // insert
            long rowId = getWritableDB().insert(getTableName(), null, entity.toContentValues());
            List<T> list = findAll("rowid = ?", new String[]{String.valueOf(rowId)});
            if (list.size() > 0) {
                entity.setId(list.get(0).getId());
            }
        }
        return entity;
    }

    public <S extends T> List<S> saveAll(final List<S> entities) {
        if (entities == null) {
            return null;
        }
        return executeWithTransaction(getWritableDB(), new Callable<List<S>>(){
            @Override
            public List<S> call() throws Exception {
                List<S> result = new ArrayList<S>();
                for (S entity : entities) {
                    result.add(save(entity));
                }
                return result;
            }
        });
    }

    @Override
    public boolean exists(ID id) {
        return count(getSelectionId(), new String[]{String.valueOf(id)}) > 0;
    }

    @Override
    public T findOne(ID id) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return findAll(null, null);
    }

    @Override
    public List<T> findAll(List<ID> ids) {
        if (ids == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("id IN (");
        List<String> args = new ArrayList<String>();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(SEPARATOR);
            sb.append("?");
            args.add(ids.toString());
        }
        sb.append(")");
        return findAll(sb.toString(), args.toArray(new String[args.size()]));
    }

    public List<T> findAll(String selection, String[] selectionArgs) {
        List<T> result = new ArrayList<T>();
        Cursor c = null;
        try {
            c = getReadableDB().query(getTableName(), getAllColumns(), selection, selectionArgs, null, null, null);
            if (c.moveToFirst()) {
                do {
                    result.add(getObjectFromCursor(c));
                } while(c.moveToNext());
            }
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }

    @Override
    public long count() {
        return count(null, null);
    }

    @Override
    public long count(String selection, String[] selectionArgs) {
        long count = 0l;
        Cursor c = null;
        try {
            c = getReadableDB().query(getTableName(), new String[]{"COUNT(*)"}, selection, selectionArgs, null, null, null);
            if (c.moveToFirst()) {
                count = c.getLong(0);
            }
            return count;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public boolean delete(ID id) {
        return id != null && getWritableDB().delete(getTableName(), getSelectionId(), new String[]{id.toString()}) > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean delete(T entity) {
        return delete((ID) entity.getId());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean delete(final List<? extends T> entities) {
        Boolean result = executeWithTransaction(getWritableDB(), new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean result = true;
                for (T entity : entities) {
                    if (!delete((ID) entity.getId())) {
                        result = false;
                    }
                }
                return result;
            }
        });
        if (result == null) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean deleteAll() {
        return getWritableDB().delete(getTableName(), null, null) > 0;
    }

    protected T getObjectFromCursor(Cursor c) {
        return ObjectConverter.getObjectFromCursor(baseClass, c);
    }
}
