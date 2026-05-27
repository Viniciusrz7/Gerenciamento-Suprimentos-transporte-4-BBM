package org.slf4j;

public class LoggerFactory {
    private static final Logger NOP = new Logger() {
        public boolean isDebugEnabled() { return false; }
        public boolean isInfoEnabled() { return false; }
        public boolean isWarnEnabled() { return false; }
        public boolean isErrorEnabled() { return false; }
        public boolean isTraceEnabled() { return false; }
        public void debug(String msg) {}
        public void debug(String msg, Object... args) {}
        public void info(String msg) {}
        public void info(String msg, Object... args) {}
        public void warn(String msg) {}
        public void warn(String msg, Object... args) {}
        public void error(String msg) {}
        public void error(String msg, Object... args) {}
        public void error(String msg, Throwable t) {}
        public void trace(String msg) {}
        public void trace(String msg, Object... args) {}
    };

    public static Logger getLogger(Class<?> clazz) { return NOP; }
    public static Logger getLogger(String name) { return NOP; }
}
