package com.amr.project.security;

import com.amr.project.security.handler.OAuth2LoginSuccessHandler;
import com.amr.project.security.handler.SuccessUserHandler;
import com.amr.project.service.impl.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomWebAuthenticationDetailsSource authenticationDetailsSource;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider(customAuthenticationProvider));
    }

    @Override
    // antMatchers() с указанием страниц и ролей пока не пишу, чтобы пока не мешали
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/").anonymous()
                .antMatchers("/messenger").hasAuthority("USER")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/moderator").hasAnyAuthority("MODERATOR", "ADMIN")
                .antMatchers("/messages").hasAnyAuthority("USER","MODERATOR","ADMIN")
                .antMatchers("/feedback").hasAnyAuthority("USER","MODERATOR","ADMIN")
                .antMatchers("/feedback/feedbacklist").hasAnyAuthority("MODERATOR", "ADMIN")
                .antMatchers("/order").hasAnyAuthority("USER", "ADMIN")
                .and().formLogin().successHandler(successUserHandler)
                .loginPage("/login").loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .and().rememberMe()
                .and().oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .and().cors();

        http.formLogin()
                .successHandler(successUserHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
        ;


        http.logout().permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and().csrf().disable();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomAuthenticationProvider authenticationProvider) {
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public HttpFirewall getHttpFirewall() {
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
//        strictHttpFirewall.setAllowSemicolon(true);
//        strictHttpFirewall.setAllowBackSlash(true);
        strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
        return strictHttpFirewall;
    }
}
