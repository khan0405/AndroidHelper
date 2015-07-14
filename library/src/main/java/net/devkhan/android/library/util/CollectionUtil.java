package net.devkhan.android.library.util;

import java.util.Collection;

public class CollectionUtil {
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}
	public static boolean notEmpty(Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}
}
