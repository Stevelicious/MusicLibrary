package com.academy;


public class DBRepositoryException extends RuntimeException {
    public DBRepositoryException(String s) {
        super(s);
    }

    public DBRepositoryException() {
        super();
    }

    public DBRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBRepositoryException(Throwable cause) {
        super(cause);
    }

    protected DBRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
