package com.buddy.exception;

public class FriendRequestNotFoundException extends RuntimeException {
    public FriendRequestNotFoundException() {
        super();
    }

    public FriendRequestNotFoundException(String message) {
        super(message);
    }

    public FriendRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendRequestNotFoundException(Throwable cause) {
        super(cause);
    }
}