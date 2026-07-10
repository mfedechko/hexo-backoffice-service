package com.gpn.leads.exception;

public class LeadNotFoundException extends RuntimeException {

    public LeadNotFoundException(final String message) {
        super(message);
    }
}
