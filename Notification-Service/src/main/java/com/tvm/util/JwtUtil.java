package com.tvm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "BOrzOsp369DFRU6WmvPOwWXk9onIr77I";

    public String extractUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) getAllClaimsFromToken(token).get("role");
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration == null || !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }
}
