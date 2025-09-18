package com.tvm.config;


import com.tvm.util.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // so your @PreAuthorize on controller methods works
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // reuse the one you copied from department-service

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/leaves/reminders/pending").hasAnyRole("ADMIN","HR")
                        // Apply (employee, hr, admin) - precise self-access enforced in @PreAuthorize
                        .requestMatchers(HttpMethod.POST, "/api/leaves/apply").hasAnyRole("EMPLOYEE","HR","ADMIN")

                        // Approvals (HR/Admin only)
                        .requestMatchers(HttpMethod.PATCH, "/api/leaves/*/approve", "/api/leaves/*/reject").hasAnyRole("HR","ADMIN")

                        // Employee’s own view (controller enforces self with @PreAuthorize)
                        .requestMatchers(HttpMethod.GET, "/api/leaves/me").hasRole("EMPLOYEE")

                        // HR/Admin can see any employee’s leaves & pending list
                        .requestMatchers(HttpMethod.GET, "/api/leaves/employee/**").hasAnyRole("HR","ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/leaves/pending").hasAnyRole("HR","ADMIN")

                        // Balance – both employee (self) & HR/Admin (any) -> enforced by @PreAuthorize
                        .requestMatchers(HttpMethod.GET, "/api/leaves/balance/**").hasAnyRole("EMPLOYEE","HR","ADMIN")

                        // Anything else must be authenticated
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

