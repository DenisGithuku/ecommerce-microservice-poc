package com.ecommerce.order.exception;

public class DuplicateCartException extends RuntimeException {
    public DuplicateCartException(String name) {
        super("Order: " + name + " exists already!");
    }
}
