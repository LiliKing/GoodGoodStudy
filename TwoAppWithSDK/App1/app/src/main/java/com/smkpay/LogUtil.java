package com.smkpay;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。
 */
public class LogUtil {
    public static String customTagPrefix = "hzsmk"; // 自定义Tag的前缀，可以是作者名
    // 是否允许打印所有类型的日志，设置为false则不打印
    private static boolean isAllowAllLog = false;
    // 允许打印日志的类型，设置为false则不打印
    private static boolean isAllowD = false;
    private static boolean isAllowE = false;
    private static boolean isAllowI = false;
    private static boolean isAllowV = false;
    private static boolean isAllowW = false;
    private static boolean isAllowWtf = false;
    private static boolean isSaveLog = false; // 是否保存日志到SD卡或内部存储路径中
    private static String logPath; // 若isSaveLog为true，则保证日志到该目录下
    /**
     * 自定义的logger
     */
    public static CustomLogger customLogger;

    private LogUtil() {
    }

    /**
     * 设置是否允许打印所有类型的日志，true打印，false则不打印
     */
    public static void setAllowAllLog(boolean isAllowAllLog) {
        LogUtil.isAllowAllLog = isAllowAllLog;
        isAllowD = isAllowAllLog;
        isAllowE = isAllowAllLog;
        isAllowI = isAllowAllLog;
        isAllowV = isAllowAllLog;
        isAllowW = isAllowAllLog;
        isAllowWtf = isAllowAllLog;
    }

    public static void setAllowD(boolean isAllowD) {
        LogUtil.isAllowD = isAllowD;
    }

    public static void setAllowE(boolean isAllowE) {
        LogUtil.isAllowE = isAllowE;
    }

    public static void setAllowI(boolean isAllowI) {
        LogUtil.isAllowI = isAllowI;
    }

    public static void setAllowV(boolean isAllowV) {
        LogUtil.isAllowV = isAllowV;
    }

    public static void setAllowW(boolean isAllowW) {
        LogUtil.isAllowW = isAllowW;
    }

    public static void setAllowWtf(boolean isAllowWtf) {
        LogUtil.isAllowWtf = isAllowWtf;
    }

    public static void setLogPath(Context mContext) {
        if (isSDAvailable()) {
            // 保存log到SD卡中
            logPath = Environment.getExternalStorageDirectory().getPath()
                    + mContext.getPackageName() + "/logs";
        } else {
            // 保存log到内部存储路径/data/data/packageName中
            logPath = "/data/data/" + mContext.getPackageName() + "/logs";
        }
    }

    public static String getLogPath() {
        return logPath;
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        return tag;
    }

    public static void d(String msg) {
        if (!isAllowD) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, msg);
        } else {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (!isAllowD) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, msg, tr);
        } else {
            Log.d(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (!isAllowE) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, msg);
        } else {
            Log.e(tag, msg);
        }
        if (isSaveLog) {
            // point(PATH_LOG_INFO, tag, msg);
            if (logPath != null) {
                point(logPath, tag, msg);
            }
        }
    }

    public static void e(String msg, Throwable tr) {
        if (!isAllowE) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, msg, tr);
        } else {
            Log.e(tag, msg, tr);
        }
        if (isSaveLog) {
            // point(PATH_LOG_INFO, tag, tr.getMessage());
            if (logPath != null) {
                point(logPath, tag, tr.getMessage());
            }
        }
    }

    public static void i(String msg) {
        if (!isAllowI) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, msg);
        } else {
            Log.i(tag, msg);
        }

    }

    public static void i(String msg, Throwable tr) {
        if (!isAllowI) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, msg, tr);
        } else {
            Log.i(tag, msg, tr);
        }

    }

    public static void v(String msg) {
        if (!isAllowV) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, msg);
        } else {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (!isAllowV) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, msg, tr);
        } else {
            Log.v(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (!isAllowW) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, msg);
        } else {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (!isAllowW) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, msg, tr);
        } else {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!isAllowW) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String msg) {
        if (!isAllowWtf) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, msg);
        } else {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if (!isAllowWtf) {
            return;
        }
        if (null == msg || "".equals(msg)) {
            msg = ";";
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, msg, tr);
        } else {
            Log.wtf(tag, msg, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!isAllowWtf) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
            Log.wtf(tag, tr);
        }
    }

    private static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
                .getStackTrace();
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void point(String path, String tag, String msg) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("",
                Locale.SIMPLIFIED_CHINESE);
        dateFormat.applyPattern("yyyy");
        path = path + dateFormat.format(date) + "/";
        dateFormat.applyPattern("MM");
        path += dateFormat.format(date) + "/";
        dateFormat.applyPattern("dd");
        path += dateFormat.format(date) + ".log";
        dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
        String time = dateFormat.format(date);
        File file = new File(path);
        if (!file.exists()) {
            createDipPath(path);
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(time + " " + tag + " " + msg + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }

    public static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    public interface CustomLogger {
        void d(String tag, String msg);

        void d(String tag, String msg, Throwable tr);

        void e(String tag, String msg);

        void e(String tag, String msg, Throwable tr);

        void i(String tag, String msg);

        void i(String tag, String msg, Throwable tr);

        void v(String tag, String msg);

        void v(String tag, String msg, Throwable tr);

        void w(String tag, String msg);

        void w(String tag, String msg, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String msg);

        void wtf(String tag, String msg, Throwable tr);

        void wtf(String tag, Throwable tr);
    }
}
