package lab_4.classes;

import java.util.*;

/**
 * Пользовательские исключения для работы со Stream API
 */
public class StreamExceptions {
    
    public static class EmptyCollectionException extends RuntimeException {
        public EmptyCollectionException(String message) {
            super(message);
        }
        
        public EmptyCollectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
        
        public InvalidInputException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class InvalidStreamOperationException extends RuntimeException {
        public InvalidStreamOperationException(String message) {
            super(message);
        }
        
        public InvalidStreamOperationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class StreamProcessingException extends RuntimeException {
        public StreamProcessingException(String message) {
            super(message);
        }
        
        public StreamProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class NoSuchElementStreamException extends NoSuchElementException {
        public NoSuchElementStreamException(String message) {
            super(message);
        }
        
        public NoSuchElementStreamException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}