package lib;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author tybrown
 */
public class Debug {

    public static final int FATAL = 1;
    public static final int ERROR = 2;
    public static final int LOG = 3;
    public static final int WARN = 4;
    public static final int DEBUG = 5;

    private static int logLevel = DEBUG;


    //these two functions taken from https://stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java
    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Debug.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName() + "::" + ste.getMethodName();
                //return ste.getClassName();
            }
        }
        return null;
    }
    
    public static String getCallerCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Debug.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName() + "::" + ste.getMethodName();
                }
            }
        }
        return null;
    }

    private static String levelToString(int level) {
        switch (level) {
            case LOG:
                return ("- LOG -");
            case WARN:
                return ("* WARN *");
            case ERROR:
                return ("! ERROR !");
            case FATAL:
                return ("!! FATAL !!");
            case DEBUG:
                return ("# DEBUG #");
        }
        return (Integer.toString(logLevel));
    }

    public synchronized static void printf(int level, String format, Object... args) {
        if (level > logLevel) {
            return;
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        System.out.printf("[%s] [%s] %s: ", formattedDate, levelToString(level), getCallerClassName());
        System.out.printf(format, args);
        System.out.println();
        if (level == FATAL) {
            System.out.println("Exiting...\n");
        }
    }
    
    public static void log(String format, Object... args) {
        printf(LOG, format, args);
    }
    
    public static void warn(String format, Object... args) {
        printf(WARN, format, args);
    }
    
    public static void error(String format, Object... args) {
        printf(ERROR, format, args);
    }

    public static void fatal(String format, Object... args) {
        printf(FATAL, format, args);
    }
   
    public static void debug(String format, Object... args) {
        printf(DEBUG, format, args);
    }

    public static void setLogLevel(int level) {
        logLevel = level;
    }
}
