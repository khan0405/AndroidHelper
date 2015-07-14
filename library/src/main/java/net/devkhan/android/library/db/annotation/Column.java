package net.devkhan.android.library.db.annotation;

import net.devkhan.android.library.db.DbType;

import java.lang.annotation.*;

/**
 * Created by KHAN on 2015-07-09.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String value() default "";
    DbType dbType() default DbType.NONE;
    boolean notNull() default false;
    boolean autoIncrement() default false;
}
