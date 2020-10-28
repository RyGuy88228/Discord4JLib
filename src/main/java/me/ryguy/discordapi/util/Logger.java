package me.ryguy.discordapi.util;

import java.util.logging.Level;

public interface Logger {
    default java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getGlobal();
    }
    default void info(String msg) {
        getLogger().info(msg);
    }
    default void info(String msg, Object... obj) {
        getLogger().log(Level.INFO, msg, obj);
    }
    default void warn(String msg) {
        getLogger().warning(msg);
    }
    default void warn(String msg, Object... obj) {
        getLogger().log(Level.WARNING, msg, obj);
    }
    default void log(Level logLevel, String msg) {
        getLogger().log(logLevel, msg);
    }
    default void log(Level logLevel, String msg, Object... obj) {
        getLogger().log(logLevel, msg, obj);
    }
}
