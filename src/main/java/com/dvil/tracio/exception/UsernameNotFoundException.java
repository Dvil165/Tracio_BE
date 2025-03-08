package com.dvil.tracio.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(Integer id) {
        super("Could not find user with id: " + id);
    }
}
