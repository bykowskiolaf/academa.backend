package dev.bykowski.academa.models.User;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    INSTRUCTOR("INSTRUCTOR"),
    STUDENT("STUDENT");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}