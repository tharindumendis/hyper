package com.pos.hyper.exception;

import org.aspectj.bridge.Message;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}