package com.amr.project.jwt.model;

import com.amr.project.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private String login;
    private Collection<Role> roles;

}
