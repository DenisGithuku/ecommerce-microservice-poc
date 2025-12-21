package com.ecommerce.order.exception;

import java.util.Locale;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class object, String id) {
        super("Could not find " + object.getSimpleName().toLowerCase(Locale.ROOT) + " with that id");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}