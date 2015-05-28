package org.khan.android.library.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class IOUtil {
	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	public static String getImagePathFromUri(Context context, String uriString) {
		Uri uri = Uri.parse(uriString);
		return getImagePathFromUri(context, uri);
	}
	
	public static File getExternalDir(String path) {
		File dir = new File(Environment.getExternalStorageDirectory(), path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	private static String getImagePathFromUri(Context context, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor imageCursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (imageCursor == null) {
			return null;
		}
		int column_index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		imageCursor.moveToFirst();
		String s = imageCursor.getString(column_index);
		imageCursor.close();
		imageCursor =null;
		return s;
	}

	public static void copy(File origin, File dest) throws IOException {
		copy(new BufferedInputStream(new FileInputStream(origin)), new BufferedOutputStream(new FileOutputStream(dest)));
	}
	
	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		try {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (EOF != (n = inputStream.read(buffer))) {
				outputStream.write(buffer, 0, n);
			}
			outputStream.flush();
		}
		finally {
			outputStream.close();
			inputStream.close();
		}
	}
}
