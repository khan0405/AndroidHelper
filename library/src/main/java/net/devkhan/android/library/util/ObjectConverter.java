package net.devkhan.android.library.util;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static net.devkhan.android.library.db.RepositoryFactory.getDBFieldName;

/**
 * Created by KHAN on 2015-07-08.
 */
public class ObjectConverter {
    public static abstract class ExtractWorker {
        public ExtractWorker() {
        }
        abstract void extract(Field field, String name, Object value) throws IllegalStateException, IllegalAccessException;
    }

    public static Map<String, Object> toMap(Object vo) {
        final Map<String, Object> map = new HashMap<String, Object>();
        extractAllFields(vo.getClass(), vo, new ExtractWorker() {
            @Override
            void extract(Field field, String name, Object value) throws IllegalStateException, IllegalAccessException {
                map.put(name, value);
            }
        });
        return map;
    }

    public static ContentValues toContentValues(Object vo) {
        final ContentValues values = new ContentValues();
        extractAllFields(vo.getClass(), vo, new ExtractWorker() {
            @Override
            void extract(Field field, String name, Object value) throws IllegalStateException, IllegalAccessException {
                name = getDBFieldName(field);
                if (value == null) {
                    return;
                }

                if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
                    values.put(name, (Boolean) value);
                }
                if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
                    values.put(name, (Integer)value);
                }
                if (Short.class.equals(field.getType()) || short.class.equals(field.getType())) {
                    values.put(name, (Short)value);
                }
                if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
                    values.put(name, (Long)value);
                }
                if (Float.class.equals(field.getType()) || float.class.equals(field.getType())) {
                    values.put(name, (Float)value);
                }
                if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
                    values.put(name, (Double)value);
                }
                if (String.class.equals(field.getType())) {
                    values.put(name, (String)value);
                }
                if (Date.class.equals(field.getType())) {
                    values.put(name, ((Date)value).getTime());
                }
                if (byte[].class.equals(field.getType())) {
                    values.put(name, (byte[])value);
                }
            }
        });
        return values;
    }

    private static void extractAllFields(Class<?> cls, Object vo, ExtractWorker worker) {
        Class<?> superClass = cls.getSuperclass();
        if (!superClass.equals(Object.class)) {
            extractAllFields(superClass, vo, worker);
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);

                try {
                    String name = field.getName();
                    Object value = field.get(vo);
                    worker.extract(field, name, value);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                field.setAccessible(false);
            }
        }
    }

    public static <T> T getObjectFromCursor(Class<T> cls, Cursor c) {
        if (cls == null || c == null) {
            return null;
        }
        T target = null;
        try {
            target = cls.newInstance();
            List<Field> fields = new ArrayList<Field>();
            ReflectionUtil.setAllFieldFromClass(fields, cls);
            for (Field field : fields) {
                String name = getDBFieldName(field);
                Object val = null;
                if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
                    val = c.getInt(c.getColumnIndex(name)) > 0;
                }
                if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
                    val = c.getInt(c.getColumnIndex(name));
                }
                if (Short.class.equals(field.getType()) || short.class.equals(field.getType())) {
                    val = c.getShort(c.getColumnIndex(name));
                }
                if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
                    val = c.getLong(c.getColumnIndex(name));
                }
                if (Float.class.equals(field.getType()) || float.class.equals(field.getType())) {
                    val = c.getFloat(c.getColumnIndex(name));
                }
                if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
                    val = c.getDouble(c.getColumnIndex(name));
                }
                if (String.class.equals(field.getType())) {
                    val = c.getString(c.getColumnIndex(name));
                }
                if (Date.class.equals(field.getType())) {
                    val = new Date(c.getLong(c.getColumnIndex(name)));
                }
                if (byte[].class.equals(field.getType())) {
                    val = c.getBlob(c.getColumnIndex(name));
                }
                if (val == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(target, val);
                field.setAccessible(false);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return target;
    }
}
