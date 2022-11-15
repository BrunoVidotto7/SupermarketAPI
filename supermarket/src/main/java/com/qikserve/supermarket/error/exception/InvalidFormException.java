package com.qikserve.supermarket.error.exception;

public class InvalidFormException extends RuntimeException {

    public InvalidFormException() {
        super();
    }

    public InvalidFormException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidFormException(final String message) {
        super(message);
    }

    public InvalidFormException(final Throwable cause) {
        super(cause);
    }

}
