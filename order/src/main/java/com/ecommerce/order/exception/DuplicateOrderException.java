package com.ecommerce.order.exception;

public class DuplicateOrderException extends RuntimeException {
    public DuplicateOrderException(String name) {
        super("Order: " + name + " exists already!");
    }
}

