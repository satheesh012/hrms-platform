package com.tvm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "BOrzOsp369DFRU6WmvPOwWXk9onIr77I"; // EXACTLY same as auth-service

    public String extractEmployeeId(String token) {
        return extractAllClaims(token).getSubject(); // subject = employeeId
    }

//    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
//    }

    public String extractUsername(String token) {
        return (String) extractAllClaims(token).get("username");
    }

    public String extractRole(String token) {
        Object role = extractAllClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // will throw if invalid/expired/signature mismatch
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)   // 0.9.1 API
                .parseClaimsJws(token)
                .getBody();
    }
}
