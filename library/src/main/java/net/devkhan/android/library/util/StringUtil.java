package net.devkhan.android.library.util;

import android.util.Base64;

public class StringUtil {
	
	public static boolean isEmpty(CharSequence s) {
		return s == null || "".equals(s.toString().trim());
	}
	
	public static boolean notEmpty(CharSequence s) {
		return s != null && !"".equals(s.toString().trim());
	}
	
	public static String nvl(String s) {
		return isEmpty(s) ? "" : s.trim();
	}
	
	public static boolean validateForLength(CharSequence s, int length) {
		if (notEmpty(s)) {
			return s.toString().trim().length() <= length;
		}
		return false;
	}

	public static String getBase64encode(String content) {
		if (notEmpty(content)) {
			return Base64.encodeToString(content.getBytes(), Base64.NO_WRAP);
		}
		return "";
	}

	public static String getBase64decode(String content) {
		if (notEmpty(content)) {
			return new String(Base64.decode(content, Base64.NO_WRAP));
		}
		return "";
	}
}
