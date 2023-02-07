package org.opendatadiscovery.oddrn.exception;

public class GenerateException extends Exception {
    public GenerateException(final String message) {
        super(message);
    }

    public GenerateException(final String message, final Throwable e) {
        super(message, e);
    }
}
