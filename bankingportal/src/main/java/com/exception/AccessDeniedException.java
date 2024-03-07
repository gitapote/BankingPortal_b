package com.exception;

public class AccessDeniedException extends RuntimeException {
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 1L;

    public AccessDeniedException(String message) {
        super(message);
    }
}
