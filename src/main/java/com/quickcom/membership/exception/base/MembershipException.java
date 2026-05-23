package com.quickcom.membership.exception.base;

public abstract class MembershipException extends RuntimeException {

    protected MembershipException(String message) {
        super(message);
    }
}
