package com.amr.project.security.handler;

import com.amr.project.model.entity.Role;
import com.amr.project.model.entity.User;
import com.amr.project.security.oauth.CustomOAuth2User;
import com.amr.project.service.abstracts.RoleService;
import com.amr.project.service.abstracts.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
// Проект
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final RoleService roleService;

    public OAuth2LoginSuccessHandler(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getName();
        User user = userService.findByEmail(email).orElse(null);


        if(user == null) {
            Role role = roleService.findByName("UNREGISTERED");
            Set<Role>roleSet = new HashSet<>();
            roleSet.add(role);
            user = User.builder()
                    .firstName(oAuth2User.getAttribute("name"))
                    .username(oAuth2User.getAttribute("email"))
                    .email(oAuth2User.getAttribute("email"))
                    .password("password")
                    .build();
            userService.persist(user);
            user.setRoles(roleSet);
            userService.update(user);
            response.sendRedirect("/user/" + user.getId());
        } else {
            response.sendRedirect("/");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
