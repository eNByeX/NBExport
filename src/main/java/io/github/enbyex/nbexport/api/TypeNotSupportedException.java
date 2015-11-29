package io.github.enbyex.nbexport.api;

/**
 * Exception thrown when an exporter factory doesn't support the given type.
 *
 * @author soniex2
 */
public class TypeNotSupportedException extends Exception {
    public TypeNotSupportedException() {
    }

    public TypeNotSupportedException(String message) {
        super(message);
    }
}
