package net.devkhan.android.library.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.io.IOException;

/**
 * Created by KHAN on 2015-05-29.
 */
public interface ResultParser {
    enum ParseType {
        VALUE, LIST, MAP
    }

    <T, C> Response<T> parseNetworkResult(NetworkResponse response, ParseType parseType, Class<C> parseClass);
    <T, C> T parseResult(byte[] resultValue, ParseType parseType, Class<C> parseClass) throws IOException;
}
