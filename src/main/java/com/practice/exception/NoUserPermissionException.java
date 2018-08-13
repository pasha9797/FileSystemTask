package com.practice.exception;

public class NoUserPermissionException extends Exception {
    String username, permission;

    public NoUserPermissionException(String username, String permission) {
        super(username + ' ' + permission);
        this.username = username;
        this.permission = permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
