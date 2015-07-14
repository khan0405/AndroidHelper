package net.devkhan.android.library.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network Management Class.
 *
 * Created by KHAN on 2015-07-14.
 */
public class NetworkManager {
    /**
     * 네트워크 상태 체크
     * @param context Application Context
     * @return return true when network connected.
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            for (NetworkInfo networkInfo : connectivityManager.getAllNetworkInfo()) {
                if (networkInfo.isConnected()) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }
}