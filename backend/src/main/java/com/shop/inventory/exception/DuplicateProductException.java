package com.shop.inventory.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String id) {
        super("Product ID already exists: " + id);
    }
}
