package net.devkhan.android.library.db.annotation;

import java.lang.annotation.*;

/**
 * Created by KHAN on 2015-07-09.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    String value() default "";
}
