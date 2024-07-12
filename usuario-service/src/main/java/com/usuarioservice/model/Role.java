package com.usuarioservice.model;

public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_CLIENTE("CLIENTE");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
