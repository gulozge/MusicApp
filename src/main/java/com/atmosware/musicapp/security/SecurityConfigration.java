package com.atmosware.musicapp.security;

import com.atmosware.musicapp.entity.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfigration {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth-> auth
                                        .requestMatchers(
                                                "/swagger-ui.html/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/api/auth/**",
                                                "/api/users/registion",
                                                "/api/users/login",
                                                "/api/users/logout/**",
                                                "/api/admins/registion",
                                                "/api/admins/login",
                                                "/actuator/*")
                                        .permitAll()

                                        .requestMatchers (HttpMethod.GET,
                                                "/api/songs/**",
                                                "/api/artists/**",
                                                "/api/albums/**")
                                        .permitAll()

                                        .requestMatchers(HttpMethod.GET,
                                                "/api/admins/**",
                                                "/api/users/getAll",
                                                "/api/users/getById/**")
                                        .hasRole(Role.ADMIN.name())

                                        .requestMatchers(
                                                "/api/artists/**",
                                                "/api/albums/**",
                                                "/api/songs/**",
                                                "/api/admins/**")
                                        .hasRole(Role.ADMIN.name())

                                        .requestMatchers(
                                                "/api/users/**")
                                        .hasRole(Role.USER.name()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class );

        return http.build();
    }

}