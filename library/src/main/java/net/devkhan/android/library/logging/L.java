package net.devkhan.android.library.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import android.util.Log;


public abstract class L {
	
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int ALL = 0;
	
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    
    private static int logLevel = ALL;
    
    public static void setLogLevel(int level) {
    	logLevel = level;
    }
    
    public static String getLogTag(Class<?> cls) {
    	return "CB_" + cls.getSimpleName();
    }

	public static String format(String format, Object...args) {
    	return String.format(format, args);
    }
    
    /**
     * Send a {@link #VERBOSE} log message.
     * @param cls Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(Class<?> cls, String msg) {
        return println(VERBOSE, getLogTag(cls), msg);
    }
    
    public static int v(String tag, String msg) {
        return println(VERBOSE, tag, msg);
    }
    public static int v(Class<?> cls, String msg, Throwable tr) {
        return println(VERBOSE, getLogTag(cls), msg + '\n' + getStackTraceString(tr));
    }
    public static int v(String tag, String msg, Throwable tr) {
        return println(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int d(Class<?> cls, String msg) {
        return println(DEBUG, getLogTag(cls), msg);
    }
    public static int d(String tag, String msg) {
        return println(DEBUG, tag, msg);
    }
    public static int d(Class<?> cls, String msg, Throwable tr) {
        return println(DEBUG, getLogTag(cls), msg + '\n' + getStackTraceString(tr));
    }
    public static int d(String tag, String msg, Throwable tr) {
        return println(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int i(Class<?> cls, String msg) {
        return println(INFO, getLogTag(cls), msg);
    }
    public static int i(String tag, String msg) {
        return println(INFO, tag, msg);
    }
    public static int i(Class<?> cls, String msg, Throwable tr) {
        return println(INFO, getLogTag(cls), msg + '\n' + getStackTraceString(tr));
    }
    public static int i(String tag, String msg, Throwable tr) {
        return println(INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(Class<?> cls, String msg) {
        return println(WARN, getLogTag(cls), msg);
    }
    public static int w(String tag, String msg) {
        return println(WARN, tag, msg);
    }
    public static int w(Class<?> cls, String msg, Throwable tr) {
        return println(WARN, getLogTag(cls), msg + '\n' + getStackTraceString(tr));
    }
    public static int w(String tag, String msg, Throwable tr) {
        return println(WARN, tag, msg + '\n' + getStackTraceString(tr));
    }
    public static int w(Class<?> cls, Throwable tr) {
        return println(WARN, getLogTag(cls), getStackTraceString(tr));
    }
    public static int w(String tag, Throwable tr) {
        return println(WARN, tag, getStackTraceString(tr));
    }

    public static int e(Class<?> cls, String msg) {
        return println(ERROR, getLogTag(cls), msg);
    }
    public static int e(String tag, String msg) {
        return println(ERROR, tag, msg);
    }
    public static int e(Class<?> cls, String msg, Throwable tr) {
        return println(ERROR, getLogTag(cls), msg + '\n' + getStackTraceString(tr));
    }
    public static int e(String tag, String msg, Throwable tr) {
        return println(ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static int println(int priority, String tag, String msg) {
        if (logLevel <= priority) {
        	return Log.println(priority, tag, msg);
        }
        return 0;
    }
}
