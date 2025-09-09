package lab_3.classes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Централизованное управление логгированием
 */
public class AppLogger {
    private static final Logger instance = Logger.getLogger("TranslatorApp");
    
    static {
        // Базовая настройка логгера
        System.setProperty("java.util.logging.SimpleFormatter.format", 
                          "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }
    
    public static Logger getLogger() {
        return instance;
    }
    
    public static void info(String message) {
        instance.info(message);
    }
    
    public static void warning(String message) {
        instance.warning(message);
    }
    
    public static void severe(String message) {
        instance.severe(message);
    }
    
    public static void fine(String message) {
        instance.fine(message);
    }
    
    public static void log(Level level, String message, Throwable throwable) {
        instance.log(level, message, throwable);
    }
}