package net.devkhan.android.library.util;

import android.content.Context;
import dalvik.system.DexFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class Reflection Utility.
 *
 * Created by KHAN on 2015-07-08.
 *
 */
public class ReflectionUtil {

    /**
     * Scan the classes from target package name.
     *
     * @param context The application context
     * @param packageName target package name
     * @return detected classes set.
     * @throws IOException
     */
    public static Set<Class<?>> getClasspathClasses(Context context, String packageName) throws IOException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        DexFile dex = new DexFile(context.getApplicationInfo().sourceDir);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<String> entries = dex.entries();
        while (entries.hasMoreElements()) {
            String entry = entries.nextElement();
            if (entry.toLowerCase().startsWith(packageName.toLowerCase())) {
                try {
                    classes.add(classLoader.loadClass(entry));
                }
                catch (ClassNotFoundException e) {
                    // do nothing...
                }
            }
        }
        return classes;
    }

    public static void setAllFieldFromClass(List<Field> fieldList, Class<?> cls) {
        Class<?> superClass = cls.getSuperclass();
        if (!superClass.equals(Object.class)) {
            setAllFieldFromClass(fieldList, superClass);
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
    }
}
