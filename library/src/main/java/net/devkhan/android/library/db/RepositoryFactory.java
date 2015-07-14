package net.devkhan.android.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import net.devkhan.android.library.db.annotation.Column;
import net.devkhan.android.library.db.annotation.Entity;
import net.devkhan.android.library.db.annotation.Id;
import net.devkhan.android.library.db.annotation.IgnoreField;
import net.devkhan.android.library.logging.L;
import net.devkhan.android.library.util.CollectionUtil;
import net.devkhan.android.library.util.ReflectionUtil;
import net.devkhan.android.library.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static net.devkhan.android.library.util.StringUtil.notEmpty;

/**
 * auto create repository class.
 * Created by KHAN on 2015-07-08.
 */
public abstract class RepositoryFactory {
    private static final String LOG_TAG = L.getLogTag(RepositoryFactory.class);

    private static final String DEFAULT_MODEL_SCAN_PACKAGE = "org.khan.android.library.model";

    private static final String SQL_CREATE_DB = "CREATE TABLE %s (%s);";

    private static final String SEPARATOR = ",";
    private static final String BLANK = " ";

    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String AUTO_INCREMENT = "AUTOINCREMENT";
    private static final String NOT_NULL = "NOT NULL";

    private static final Map<Class<?>, DbType> TYPE_MAP;
    static {
        TYPE_MAP = new LinkedHashMap<Class<?>, DbType>();
        TYPE_MAP.put(boolean.class, DbType.INTEGER);
        TYPE_MAP.put(char.class, DbType.TEXT);
        TYPE_MAP.put(byte.class, DbType.INTEGER);
        TYPE_MAP.put(short.class, DbType.INTEGER);
        TYPE_MAP.put(int.class, DbType.INTEGER);
        TYPE_MAP.put(long.class, DbType.INTEGER);
        TYPE_MAP.put(float.class, DbType.REAL);
        TYPE_MAP.put(double.class, DbType.REAL);
        TYPE_MAP.put(byte[].class, DbType.BLOB);
        TYPE_MAP.put(Boolean.class, DbType.INTEGER);
        TYPE_MAP.put(Character.class, DbType.TEXT);
        TYPE_MAP.put(Byte.class, DbType.INTEGER);
        TYPE_MAP.put(Short.class, DbType.INTEGER);
        TYPE_MAP.put(Integer.class, DbType.INTEGER);
        TYPE_MAP.put(Long.class, DbType.INTEGER);
        TYPE_MAP.put(Float.class, DbType.REAL);
        TYPE_MAP.put(Double.class, DbType.REAL);
        TYPE_MAP.put(String.class, DbType.TEXT);
        TYPE_MAP.put(Date.class, DbType.INTEGER);
    }

    private RepositoryFactory() {}

    /**
     * Repository initializing. Create database, Create Table.
     * @param context application context.
     * @param modelPackages scanning base package. Scanning model/vo class
     */
    public static void scanModels(Context context, SQLiteDatabase db, String... modelPackages) {
        try {
            Set<Class<?>> classes = new HashSet<Class<?>>();
            if (modelPackages == null) {
                classes.addAll(ReflectionUtil.getClasspathClasses(context, DEFAULT_MODEL_SCAN_PACKAGE));
            }
            else {
                for (String modelPackage : modelPackages) {
                    classes.addAll(ReflectionUtil.getClasspathClasses(context, modelPackage));
                }
            }
            createDatabaseFromClass(db, classes);
        }
        catch (IOException e) {
            L.w(LOG_TAG, "vo scanning failed...", e);
        }
    }

    public static void dropAllModels(Context context, SQLiteDatabase db, String... modelPackages) {
        try {
            Set<Class<?>> classes = new HashSet<Class<?>>();
            if (modelPackages == null) {
                classes.addAll(ReflectionUtil.getClasspathClasses(context, DEFAULT_MODEL_SCAN_PACKAGE));
            }
            else {
                for (String modelPackage : modelPackages) {
                    classes.addAll(ReflectionUtil.getClasspathClasses(context, modelPackage));
                }
            }
            for (Class<?> cls : classes) {
                db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getTableName(cls)));
            }
        }
        catch (IOException e) {
            L.w(LOG_TAG, "vo scanning failed...", e);
        }
    }

    static void createDatabaseFromClass(SQLiteDatabase db, Set<Class<?>> classes) {
        if (CollectionUtil.notEmpty(classes)) {
            for (Class<?> clazz : classes) {
                String tableName = getTableName(clazz);

                StringBuffer sb = new StringBuffer();
                setFieldDefinition(sb, clazz);

                if (sb.length() > 0) {
                    L.e(LOG_TAG, String.format(SQL_CREATE_DB, tableName, sb.toString().replaceFirst(SEPARATOR, BLANK)));
                    db.execSQL(String.format(SQL_CREATE_DB, tableName, sb.toString().replaceFirst(SEPARATOR, BLANK)));
                }
            }
        }
    }

    private static void setFieldDefinition(StringBuffer sb, Class<?> cls) {
        List<Field> fields = new ArrayList<Field>();
        ReflectionUtil.setAllFieldFromClass(fields, cls);

        for (Field field : fields) {
            IgnoreField ignore = field.getAnnotation(IgnoreField.class);
            if (ignore != null) {
                continue;
            }

            field.setAccessible(true);

            String name;
            String options = "";
            DbType dbType;

            Id id = field.getAnnotation(Id.class);
            Column col = field.getAnnotation(Column.class);
            if (id != null) {
                options = getFieldOptions(true, id.notNull(), id.autoIncrement());
                dbType = getFieldType(field.getType(), id.dbType());
            }
            else if (col != null && notEmpty(col.value())) {
                options = getFieldOptions(false, col.notNull(), col.autoIncrement());
                dbType = getFieldType(field.getType(), col.dbType());
            }
            else {
                dbType = getFieldType(field.getType(), null);
            }

            name = getDBFieldName(field, id, col);

            sb.append(SEPARATOR).append(BLANK).append(name).append(BLANK).append(dbType).append(BLANK).append(options);
            field.setAccessible(false);
        }
    }

    private static String getTableName(Class<?> clazz) {
        String tableName;
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null && StringUtil.notEmpty(entity.value())) {
            tableName = entity.value();
        }
        else {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    private static String getFieldOptions(boolean primaryKey, boolean notNull, boolean autoIncrement) {
        StringBuilder options = new StringBuilder();
        if (primaryKey) {
            options.append(BLANK).append(PRIMARY_KEY);
        }
        if (autoIncrement) {
            options.append(BLANK).append(AUTO_INCREMENT);
        }
        if (notNull) {
            options.append(BLANK).append(NOT_NULL);
        }
        return options.toString();
    }

    private static DbType getFieldType(Class<?> fieldType, DbType dbType) {
        DbType result;
        if (dbType != null && DbType.AUTO != dbType) {
            result = dbType;
        }
        else {
            result = TYPE_MAP.containsKey(fieldType) ? TYPE_MAP.get(fieldType) : dbType;
        }
        return result;
    }

    public static String getDBFieldName(Field field) {
        Id idField = field.getAnnotation(Id.class);
        Column colField = field.getAnnotation(Column.class);
        return getDBFieldName(field, idField, colField);
    }

    public static String getDBFieldName(Field field, Id idField, Column colField) {
        String name = null;

        if (idField != null) {
            if (StringUtil.notEmpty(idField.value())) {
                name = idField.value();
            }
        }
        else if (colField != null) {
            if (StringUtil.notEmpty(colField.value())) {
                name = colField.value();
            }
        }

        if (StringUtil.isEmpty(name)) {
            name = field.getName();
        }

        return name;
    }
}
