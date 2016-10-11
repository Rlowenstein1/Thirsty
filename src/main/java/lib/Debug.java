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


    //these two functions taken from
    //stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java

    /**
     *
     * @return Gets a String representation of the method which called
     */
    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Debug.class.getName())
                    && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName() + "::" + ste.getMethodName();
                //return ste.getClassName();
            }
        }
        return null;
    }
    
    /**
     *
     * @return Gets a String representation of the parent of the caller
     */
    public static String getCallerCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Debug.class.getName())
                    && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName() + "::" + ste.getMethodName();
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param level Translates the level to a string representation
     * @return Returns a String representation of the level
     */
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
        default:
            return (Integer.toString(logLevel));
        }
    }

    /**
     *
     * @param level The level the message is 
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static synchronized void printf(int level, String format,
            Object... args) {
        if (level > logLevel) {
            return;
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        System.out.printf("[%s] [%s] %s: ", formattedDate, levelToString(level),
                getCallerClassName());
        System.out.printf(format, args);
        System.out.println();
        if (level == FATAL) {
            System.out.println("Exiting...\n");
        }
    }
    
    /**
     *
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static void log(String format, Object... args) {
        printf(LOG, format, args);
    }
    
    /**
     *
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static void warn(String format, Object... args) {
        printf(WARN, format, args);
    }
    
    /**
     *
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static void error(String format, Object... args) {
        printf(ERROR, format, args);
    }

    /**
     *
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static void fatal(String format, Object... args) {
        printf(FATAL, format, args);
    }
   
    /**
     *
     * @param format The format of the string to print Same as System.out.printf
     * @param args Optional args. Similar to System.out.printf
     */
    public static void debug(String format, Object... args) {
        printf(DEBUG, format, args);
    }

    /**
     *
     * @param level Sets the default log level. Only messages <= than this
     *      level will be printed
     */
    public static void setLogLevel(int level) {
        logLevel = level;
    }
}