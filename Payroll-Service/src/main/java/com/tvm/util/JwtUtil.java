package com.tvm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
//public class JwtUtil {
//
//    private static final String SECRET_KEY = "BOrzOsp369DFRU6WmvPOwWXk9onIr77I";
//
//    public String extractUsername(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    public String extractRole(String token) {
//        return (String) getAllClaimsFromToken(token).get("role");
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Claims claims = getAllClaimsFromToken(token);
//            Date expiration = claims.getExpiration();
//            return expiration == null || !expiration.before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private boolean isTokenExpired(String token) {
//        return getAllClaimsFromToken(token).getExpiration().before(new Date());
//    }
//}


@Component
public class JwtUtil {

    private static final String SECRET_KEY = "BOrzOsp369DFRU6WmvPOwWXk9onIr77I";

    // Generate token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().toArray()[0].toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ Extract role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
