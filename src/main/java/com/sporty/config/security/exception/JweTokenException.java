package com.sporty.config.security.exception;

public class JweTokenException extends RuntimeException {
    public JweTokenException(String message) {
        super(message);
    }

    public JweTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
