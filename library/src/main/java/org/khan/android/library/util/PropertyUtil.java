package org.khan.android.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;


public abstract class PropertyUtil {

	/**
	 * <PRE>
	 * 프로퍼티 파일을 가져올 때 쓴다.
	 * Build script용으로서, 해당 파일은 빌드할 때 초기화되므로 배포시엔 상관 없지만, 
	 * SVN 등의 관리에는 민감할 수 있으므로 해당 파일은 삭제하지 말도록 하자.
	 * </PRE>
	 * 
	 * @param context 앱 컨텍스트
	 * @param propertyFilename 불러올 프로퍼티 파일명
	 * @return 외부 프로퍼티
	 * @author khan
	 * @since 2013. 12. 6.
	 */
	public static Properties getAssetProperties(Context context, String propertyFilename) {
		Properties p = null;
		AssetManager assetManager = context.getAssets();
		InputStream is = null;
		try {
			is = assetManager.open(propertyFilename);
			p = new Properties();
			p.load(is);
		}
		catch (IOException e) {
			// do nothing
		}
		finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				}
				catch (Exception e) {
				}
			}
		}
		return p;
	}

	/**
	 * <PRE>
	 * 외부 프로퍼티 파일을 가져올 때 쓴다.
	 * 해당 파일은 뒷구멍이므로, Asset등에 넣지 않도록 주의.
	 * <B>Asset에 넣을 거면, 코드 수정 후 사용하되 커밋하지 말것!!!!!!!!!!!!</B>
	 * </PRE>
	 * 
	 * @param propertyFilename 불러올 프로퍼티 파일명
	 * @return 외부 프로퍼티
	 * @author khan
	 * @since 2013. 12. 6.
	 */
	public static Properties getExternalProperties(String propertyPath, String propertyFilename) {
		File dir = new File(Environment.getExternalStorageDirectory(), propertyPath);
		File file = new File(dir, propertyFilename);
		Properties p = null;
		InputStream is = null;
		if (file.exists()) {
			try {
				is = new FileInputStream(file);
				p = new Properties();
				p.load(is);
			}
			catch (Exception e) {
				// do nothing
			}
			finally {
				if (is != null) {
					try {
						is.close();
						is = null;
					}
					catch (Exception e) {
					}
				}
			}
		}
		return p;
	}
}
