package com.ezrol.terry.minecraft.defaultworldgenerator.lib;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"unused,WeakerAccess", "SameParameterValue"})
public class Log {

    public static Logger modLog;

    public static void log(Level logLevel, Object object) {
        modLog.log(logLevel, String.valueOf(object));
    }

    public static void all(Object object) {
        log(Level.ALL, object);
    }

    public static void debug(Object object) {
        log(Level.INFO, object);
    }

    public static void trace(Object object) {
        log(Level.TRACE, object);
    }

    public static void fatal(Object object) {
        log(Level.FATAL, object);
    }

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void warn(Object object) {
        log(Level.WARN, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void off(Object object) {
        log(Level.OFF, object);
    }
}