package com.amr.project.jwt.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    USER("USER"),
    MODERATOR("MODERATOR"),
    UNREGISTERED("UNREGISTERED");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }

}
