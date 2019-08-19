package com.cn.request.utils;

import android.util.Log;

/**
 * Date: 2019/7/3
 * <p>
 * Time: 12:19 PM
 * <p>
 * author: 鹿文龙
 */
public class HttpTrace {

    private static boolean isLogEnable = true;

    private static final int ERROR = 6;
    private static final int WARN = 5;
    private static final int INFO = 4;
    private static final int DEBUG = 3;
    private static final int VERBOSE = 2;

    private static String TAG = "RrokClient";

    /**
     * Log 日志开关
     *
     * @param isEnable
     */
    public static void debug(boolean isEnable) {
        isLogEnable = isEnable;
    }

    public static void i(String tag, String message) {
        trace(tag, message, INFO);
    }

    public static void w(String tag, String message) {
        trace(tag, message, WARN);
    }

    public static void e(String tag, String message) {
        trace(tag, message, ERROR);
    }

    public static void d(String tag, String message) {
        trace(tag, message, DEBUG);
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void w(String message) {
        w(TAG, message);
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void d(String message) {
        d(TAG, message);
    }

    public static void printStackTrace(Throwable t) {
        if (isLogEnable && t != null) t.printStackTrace();
    }

    public static void trace(String tag, String message, int level) {
        if (isLogEnable) {
            long sec = (System.currentTimeMillis() / 1000) % 1000;
            StringBuilder b = new StringBuilder("[")
                    .append(Thread.currentThread().getName()).append("] ")
                    .append("@").append(sec).append(" ").append(message);

            Log.println(level, tag, b.toString());
        }
    }
}
