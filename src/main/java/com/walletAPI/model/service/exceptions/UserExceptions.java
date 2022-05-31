package com.walletAPI.model.service.exceptions;

import java.util.Set;

public class UserExceptions extends RuntimeException {

    private Set<RuntimeException> exceptions;

    public UserExceptions() {
    }

    public UserExceptions(Set<RuntimeException> exceptions) {
        this.exceptions = exceptions;
    }

    public UserExceptions(String message) {
        super(message);
    }

    public UserExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExceptions(Throwable cause) {
        super(cause);
    }

    public Set<RuntimeException> add(RuntimeException e) {
        exceptions.add(e);
        return exceptions;
    }

    public Set<RuntimeException> remove(RuntimeException e) {
        exceptions.remove(e);
        return exceptions;
    }

    public Set<RuntimeException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Set<RuntimeException> exceptions) {
        this.exceptions = exceptions;
    }
}
