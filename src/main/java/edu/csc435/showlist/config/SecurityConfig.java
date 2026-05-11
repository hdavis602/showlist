package edu.csc435.showlist.config;

import edu.csc435.showlist.services.CustomOAuth2UserService;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        //configure csrf
        http.csrf(AbstractHttpConfigurer::disable);
        //configure auth
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/public/**").permitAll().anyRequest().authenticated());
        //configure oauth
        http.oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)));
        //configure logout
        http.logout(logout -> logout.logoutSuccessUrl("/"));
        //configure exceptions
        http.exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, authEx) -> res.sendError(401)));

        return http.build();
    }
}