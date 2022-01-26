package com.amr.project.jwt.jwt_realization;

import com.amr.project.jwt.model.Role;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {

        return (Set<Role>) claims.get("roles", List.class).stream()
                .map(el -> new Gson().fromJson(el.toString(), HashMap.class).get("authority"))
                .map(el -> Role.valueOf(el.toString()))
                .collect(Collectors.toSet());

    }

}