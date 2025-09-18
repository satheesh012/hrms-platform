package com.tvm.config;


import com.tvm.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // so your @PreAuthorize in controllers works
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Process payroll (single/all) – only HR/Admin
                        //.requestMatchers(HttpMethod.POST, "/api/payroll/process/**", "/api/payroll/process-all").hasAnyRole("HR", "ADMIN")

                        // Employees can view their own payroll via @PreAuthorize in controller,
                        // but at path level we still allow EMPLOYEE/HR/ADMIN
                        //.requestMatchers(HttpMethod.GET, "/api/payroll/employee/**").hasAnyRole("EMPLOYEE", "HR", "ADMIN")
                        //.requestMatchers(HttpMethod.GET, "/api/payroll/me").hasRole("EMPLOYEE")
                        .requestMatchers("/api/payroll/process/**","/api/payroll/process-all").hasAnyRole("ADMIN","HR")
                        .requestMatchers("/api/payroll/employee/**").hasAnyRole("ADMIN","HR","EMPLOYEE")
                        .requestMatchers("/api/payroll/*/payslip").hasAnyRole("ADMIN","HR","EMPLOYEE")
                        // Anything else -> must be authenticated
                        .anyRequest().authenticated()
                );

        // Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

