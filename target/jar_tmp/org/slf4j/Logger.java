package org.slf4j;

public interface Logger {
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();
    boolean isTraceEnabled();
    void debug(String msg);
    void debug(String msg, Object... args);
    void info(String msg);
    void info(String msg, Object... args);
    void warn(String msg);
    void warn(String msg, Object... args);
    void error(String msg);
    void error(String msg, Object... args);
    void error(String msg, Throwable t);
    void trace(String msg);
    void trace(String msg, Object... args);
}
