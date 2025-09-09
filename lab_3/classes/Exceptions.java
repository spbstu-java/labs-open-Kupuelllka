package lab_3.classes;

import java.io.IOException;

/**
 * Пользовательские исключения для переводчика
 */
public class Exceptions {
    
    public static class DictionaryFileException extends IOException {
        public DictionaryFileException(String message) {
            super(message);
        }
        
        public DictionaryFileException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class DictionaryLoadException extends IOException {
        public DictionaryLoadException(String message) {
            super(message);
        }
        
        public DictionaryLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class TranslationException extends Exception {
        public TranslationException(String message) {
            super(message);
        }
        
        public TranslationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class InvalidInputException extends IllegalArgumentException {
        public InvalidInputException(String message) {
            super(message);
        }
        
        public InvalidInputException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
