package com.ecommerce.order.exception;

public class DuplicateCartException extends RuntimeException {
    public DuplicateCartException(String userId) {
        super("Cart for user: " + userId + " exists already!");
    }
}
